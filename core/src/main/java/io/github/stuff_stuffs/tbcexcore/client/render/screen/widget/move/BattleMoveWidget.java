package io.github.stuff_stuffs.tbcexcore.client.render.screen.widget.move;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.stuff_stuffs.tbcexcore.client.TBCExCoreClient;
import io.github.stuff_stuffs.tbcexcore.client.network.BattleActionAttemptSender;
import io.github.stuff_stuffs.tbcexcore.client.render.BoxInfo;
import io.github.stuff_stuffs.tbcexcore.client.render.screen.BattleHudContext;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipantPathGatherer;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattlePath;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.action.ParticipantWalkBattleAction;
import io.github.stuff_stuffs.tbcexgui.client.api.GuiContext;
import io.github.stuff_stuffs.tbcexgui.client.api.GuiInputContext;
import io.github.stuff_stuffs.tbcexgui.client.widget.AbstractWidget;
import io.github.stuff_stuffs.tbcexutil.client.ClientUtil;
import io.github.stuff_stuffs.tbcexutil.client.RenderUtil;
import io.github.stuff_stuffs.tbcexutil.common.colour.Colour;
import io.github.stuff_stuffs.tbcexutil.common.colour.FloatRgbColour;
import io.github.stuff_stuffs.tbcexutil.common.colour.HsvColour;
import io.github.stuff_stuffs.tbcexutil.common.colour.IntRgbColour;
import io.github.stuff_stuffs.tbcexutil.common.path.Movement;
import io.github.stuff_stuffs.tbcexutil.common.path.MovementTypes;
import io.github.stuff_stuffs.tbcexutil.common.path.Path;
import io.github.stuff_stuffs.tbcexutil.common.path.PathProcessor;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Util;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

//TODO get settings from battle participant
public class BattleMoveWidget extends AbstractWidget {
    public static final Colour PATH_COLOUR = new IntRgbColour(0, 255, 0);
    private static final Quaternion[] QUATERNION_DIRECTIONS = Util.make(new Quaternion[6], arr -> {
        Direction[] directions = Direction.values();
        for (int i = 0; i < directions.length; i++) {
            arr[i] = directions[i].getRotationQuaternion();
        }
    });

    private final Settings settings;
    private final Supplier<@Nullable BattleParticipantStateView> stateGetter;
    private final World world;
    private final BattleHudContext hudContext;
    private final Consumer<Boolean> passEventsSetter;
    private boolean passEventsState = true;
    private List<Endpoint> endpoints = List.of();
    private List<BattlePath> paths = List.of();
    private boolean pathRebuildNeeded = true;
    private static VertexBuffer pathBuffer;

    public BattleMoveWidget(final Supplier<@Nullable BattleParticipantStateView> stateGetter, final World world, final BattleHudContext hudContext, final Consumer<Boolean> passEventsSetter) {
        this.stateGetter = stateGetter;
        this.world = world;
        this.hudContext = hudContext;
        this.passEventsSetter = passEventsSetter;
        passEventsSetter.accept(true);
        settings = new Settings();
        settings.setMovementTypes(Set.of(MovementTypes.WALK_TAG, MovementTypes.FALL_TAG, MovementTypes.JUMP_TAG));
        settings.setProcessors(Set.of(PathProcessor.fallDamageProcessor(4, 10)));
        settings.setPostProcessor(BattleParticipantPathGatherer.DEFAULT);
    }

    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            final Vec3d mouseVector = ClientUtil.getMouseVector();
            final Vec3d eyePos = MinecraftClient.getInstance().cameraEntity.getClientCameraPosVec(1);
            final Vec3d endPos = eyePos.add(mouseVector.multiply(64));
            double closestDist = Double.POSITIVE_INFINITY;
            Endpoint closest = null;
            BattlePath closestPath = null;
            for (int i = 0; i < endpoints.size(); i++) {
                final Endpoint endPoint = endpoints.get(i);
                final Optional<Vec3d> raycast = endPoint.box.raycast(eyePos, endPos);
                if (raycast.isPresent()) {
                    final double sq = raycast.get().squaredDistanceTo(eyePos);
                    final BattlePath path = paths.get(i);
                    if (sq < closestDist && path.getTotalCost() <= hudContext.getEnergy()) {
                        closest = endPoint;
                        closestDist = sq;
                        closestPath = paths.get(i);
                    }
                }
            }
            if (closest != null) {
                BattleActionAttemptSender.send(hudContext.getHandle().getParent(), new ParticipantWalkBattleAction(hudContext.getHandle(), closestPath));
                return true;
            }
        }
        return false;
    }

    @Override
    public void render(final GuiContext context) {
        context.enterSection(getDebugName());

        processEvents(context, event -> {
            if (event instanceof GuiInputContext.KeyPress keyPress) {
                if (keyPress.keyCode == GLFW.GLFW_KEY_TAB) {
                    passEventsState = !passEventsState;
                    passEventsSetter.accept(passEventsState);
                    return true;
                }
            } else if (event instanceof GuiInputContext.MouseClick click) {
                return mouseClicked(click.mouseX, click.mouseY, click.button);
            }
            return false;
        });

        if (pathRebuildNeeded) {
            rebuild();
        }

        if (!pathRebuildNeeded) {
            final Vec3d mouseVector = ClientUtil.getMouseVector();
            final Vec3d eyePos = MinecraftClient.getInstance().cameraEntity.getClientCameraPosVec(context.getTickDelta());
            final Vec3d endPos = eyePos.add(mouseVector.multiply(64));
            double closestDist = Double.POSITIVE_INFINITY;
            Endpoint closest = null;
            int index = -1;
            for (int i = 0; i < endpoints.size(); i++) {
                final Endpoint endPoint = endpoints.get(i);
                final Optional<Vec3d> raycast = endPoint.box.raycast(eyePos, endPos);
                if (raycast.isPresent()) {
                    final double sq = raycast.get().squaredDistanceTo(eyePos);
                    final BattlePath path = paths.get(i);
                    if (sq < closestDist && path.getTotalCost() <= hudContext.getEnergy()) {
                        closest = endPoint;
                        closestDist = sq;
                        index = i;
                    }
                }
            }
            if (closest != null) {
                final BattlePath path = paths.get(index);
                TBCExCoreClient.addRenderPrimitive(renderPath(path.getPath()));
                TBCExCoreClient.addBoxInfo(new BoxInfo(closest.box, 1, 1, 0, 1));
                hudContext.setPotentialEnergyCost(path.getTotalCost());
            }
            TBCExCoreClient.addRenderPrimitive(worldRenderContext -> {
                if (MinecraftClient.isFabulousGraphicsOrBetter()) {
                    MinecraftClient.getInstance().worldRenderer.getTranslucentFramebuffer().beginWrite(false);
                }
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                RenderSystem.enableCull();
                RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);

                final Vec3d pos = worldRenderContext.camera().getPos();
                final MatrixStack stack = worldRenderContext.matrixStack();
                stack.push();
                stack.translate(-pos.x, -pos.y, -pos.z);
                pathBuffer.setShader(stack.peek().getPositionMatrix(), worldRenderContext.projectionMatrix(), GameRenderer.getPositionColorShader());
                stack.pop();

                RenderSystem.disableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableCull();
                if (MinecraftClient.isFabulousGraphicsOrBetter()) {
                    MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
                }
            });
        }

        context.exitSection(getDebugName());
    }

    private static Consumer<WorldRenderContext> renderPath(final Path path) {
        return context -> {
            final MatrixStack matrices = context.matrixStack();
            matrices.push();
            final Vec3d pos = context.camera().getPos();
            matrices.translate(-pos.x, -pos.y, -pos.z);
            final VertexConsumer vertexConsumer = context.consumers().getBuffer(RenderLayer.LINES);
            for (final Movement movement : path.getMovements()) {
                final Vec3d start = Vec3d.ofCenter(movement.getStartPos());
                Vec3d prev = start;
                for (int i = 0; i < 7; i++) {
                    final Vec3d next = movement.interpolate(start, movement.getLength() * i / 8.0);
                    RenderUtil.lineNormal(RenderUtil.colour(RenderUtil.position(vertexConsumer, prev, matrices), PATH_COLOUR, 255), prev, next, matrices).next();
                    RenderUtil.lineNormal(RenderUtil.colour(RenderUtil.position(vertexConsumer, next, matrices), PATH_COLOUR, 255), prev, next, matrices).next();
                    prev = next;
                }
                final Vec3d next = movement.interpolate(start, movement.getLength());
                RenderUtil.lineNormal(RenderUtil.colour(RenderUtil.position(vertexConsumer, prev, matrices), PATH_COLOUR, 255), prev, next, matrices).next();
                RenderUtil.lineNormal(RenderUtil.colour(RenderUtil.position(vertexConsumer, next, matrices), PATH_COLOUR, 255), prev, next, matrices).next();
            }
            matrices.pop();
        };
    }

    private void rebuild() {
        final BattleParticipantStateView state = stateGetter.get();
        if (state != null) {
            pathRebuildNeeded = false;
            final BattleParticipantPathGatherer.Builder builder = BattleParticipantPathGatherer.builder();
            for (final TagKey<MovementTypes.RegisteredMovementType> tagKey : settings.movementTypes) {
                final Optional<RegistryEntryList.Named<MovementTypes.RegisteredMovementType>> entryList = MovementTypes.REGISTRY.getEntryList(tagKey);
                if (entryList.isPresent()) {
                    for (final RegistryEntry<MovementTypes.RegisteredMovementType> entry : entryList.get()) {
                        builder.addMovementType(entry.value());
                    }
                }
            }
            for (final PathProcessor processor : settings.processors) {
                builder.addProcessor(processor);
            }
            paths = builder.build(settings.postProcessor).gather(state, world);

            if (pathBuffer != null) {
                pathBuffer.close();
            }
            pathBuffer = new VertexBuffer();

            final BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            final MatrixStack matrixStack = new MatrixStack();
            endpoints = new ArrayList<>(paths.size());

            for (int i = 0, pathsSize = paths.size(); i < pathsSize; i++) {
                final BattlePath path = paths.get(i);
                endpoints.add(new Endpoint(new Box(path.getPos(path.getSize() - 1)), i));
            }

            for (int i = 0; i < endpoints.size(); i++) {
                final Endpoint endPoint = endpoints.get(i);
                final double cost = paths.get(i).getTotalCost();
                final float percent = (float) Math.min(Math.max(hudContext.getEnergy() - cost, 0) / hudContext.getTotalEnergy(), 1);
                final FloatRgbColour colour = new FloatRgbColour(new HsvColour(MathHelper.lerp(percent, 0, 244), 1, 1).pack(255));
                matrixStack.push();
                final Vec3d center = endPoint.box.getCenter();
                matrixStack.translate(center.x, center.y, center.z);
                for (final Quaternion quaternionDirection : QUATERNION_DIRECTIONS) {
                    matrixStack.push();
                    matrixStack.multiply(quaternionDirection);
                    matrixStack.translate(0, 0.25, 0);
                    final Matrix4f model = matrixStack.peek().getPositionMatrix();
                    bufferBuilder.vertex(model, -0.25f, 0, -0.25f).color(colour.r, colour.g, colour.b, 0.25f).next();
                    bufferBuilder.vertex(model, -0.25f, 0, 0.25f).color(colour.r, colour.g, colour.b, 0.25f).next();
                    bufferBuilder.vertex(model, 0.25f, 0, 0.25f).color(colour.r, colour.g, colour.b, 0.25f).next();
                    bufferBuilder.vertex(model, 0.25f, 0, -0.25f).color(colour.r, colour.g, colour.b, 0.25f).next();
                    matrixStack.pop();
                }
                matrixStack.pop();
            }
            bufferBuilder.end();
            pathBuffer.submitUpload(bufferBuilder);
        }
    }

    @Override
    public String getDebugName() {
        return "BattleMove";
    }

    private final class Settings {
        private Set<TagKey<MovementTypes.RegisteredMovementType>> movementTypes = Set.of();
        private Set<PathProcessor> processors = Set.of();
        private BattleParticipantPathGatherer.PathPostProcessor postProcessor = BattleParticipantPathGatherer.DEFAULT;

        public void setMovementTypes(final Set<TagKey<MovementTypes.RegisteredMovementType>> movementTypes) {
            pathRebuildNeeded = true;
            this.movementTypes = movementTypes;
        }

        public void setProcessors(final Set<PathProcessor> processors) {
            pathRebuildNeeded = true;
            this.processors = processors;
        }

        public void setPostProcessor(final BattleParticipantPathGatherer.PathPostProcessor postProcessor) {
            pathRebuildNeeded = true;
            this.postProcessor = postProcessor;
        }
    }

    private static final class Endpoint {
        private final Box box;
        private final int index;

        private Endpoint(final Box box, final int index) {
            this.box = box;
            this.index = index;
        }
    }
}
