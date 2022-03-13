package io.github.stuff_stuffs.tbcexgui.client.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import io.github.stuff_stuffs.tbcexgui.client.api.*;
import io.github.stuff_stuffs.tbcexgui.client.impl.render.DeferredEmittedQuad;
import io.github.stuff_stuffs.tbcexgui.client.impl.render.GuiQuadEmitterImpl;
import io.github.stuff_stuffs.tbcexgui.client.impl.render.MutableGuiQuadImpl;
import io.github.stuff_stuffs.tbcexgui.client.impl.render.ScissorData;
import io.github.stuff_stuffs.tbcexgui.client.impl.render.adapter.GuiVcpItemAdapter;
import io.github.stuff_stuffs.tbcexgui.client.impl.render.adapter.GuiVcpTextAdapter;
import io.github.stuff_stuffs.tbcexgui.client.render.GuiRenderLayers;
import io.github.stuff_stuffs.tbcexgui.client.render.NinePatch;
import io.github.stuff_stuffs.tbcexgui.client.render.TooltipRenderer;
import io.github.stuff_stuffs.tbcexgui.common.TBCExGui;
import io.github.stuff_stuffs.tbcexutil.common.LoggerUtil;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import io.github.stuff_stuffs.tbcexutil.common.Vec2d;
import it.unimi.dsi.fastutil.Stack;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class GuiContextImpl implements GuiContext {
    private static final Comparator<Pair<DeferredEmittedQuad, StencilState>> SORTER = Comparator.<Pair<DeferredEmittedQuad, StencilState>>comparingDouble(p -> p.getFirst().depth()).thenComparing(Pair::getSecond, StencilState.COMPARATOR);
    private final String guiName;
    private final List<GuiTransform> transforms;
    private final Queue<DeferredEmittedQuad> quadStorage = new ArrayDeque<>();
    private final List<Pair<DeferredEmittedQuad, StencilState>> inUse = new ArrayList<>();
    private final List<DeferredEmittedQuad> tooltipQuads = new ArrayList<>();
    private final Stack<StencilState> stencilStack = new ReferenceArrayList<>();
    private final VertexConsumerProvider.Immediate vertexConsumers;
    private final GuiVcpTextAdapter textAdapter;
    private final GuiVcpItemAdapter itemAdapter;
    private final GuiQuadEmitterImpl emitter;
    private final GuiInputContextImpl inputContext;
    private final GuiTextRendererImpl textRenderer;
    private final List<DebugState> debugStates = new ArrayList<>();
    private boolean tooltipMode = false;
    private float tickDelta;
    private int deferredCounter;

    public GuiContextImpl(final VertexConsumerProvider.Immediate vertexConsumers, final String guiName) {
        this.guiName = guiName;
        this.vertexConsumers = vertexConsumers;
        transforms = new ArrayList<>();
        textAdapter = new GuiVcpTextAdapter(this);
        itemAdapter = new GuiVcpItemAdapter(this);
        emitter = new GuiQuadEmitterImpl(this);
        inputContext = new GuiInputContextImpl();
        textRenderer = new GuiTextRendererImpl(MinecraftClient.getInstance().textRenderer, this);
    }

    public void setup(final MatrixStack stack, final float tickDelta, final double mouseX, final double mouseY, final List<GuiInputContext.InputEvent> events) {
        this.tickDelta = tickDelta;
        transforms.clear();
        while (!stencilStack.isEmpty()) {
            stencilStack.pop();
        }
        pushMatrixMultiply(stack.peek().getPositionMatrix());
        pushTranslate(0, 0, -100);
        inputContext.setup(mouseX, mouseY, events);
        if (!debugStates.isEmpty()) {
            LoggerUtil.LOGGER.error("Unbalanced debug states in gui {}", guiName);
            debugStates.clear();
        }
    }

    @Override
    public void pushGuiTransform(final GuiTransform transform) {
        transforms.add(transform);
        if (transform instanceof ScissorData data) {
            final MutableGuiQuadImpl quad = new MutableGuiQuadImpl();
            quad.pos(0, data.x, data.y);
            quad.pos(1, data.x, data.y + data.height);
            quad.pos(2, data.x + data.width, data.y + data.height);
            quad.pos(3, data.x + data.width, data.y);
            transformQuad(quad, true);
            stencilStack.push(new StencilState(stencilStack.isEmpty() ? null : stencilStack.top(), quad, deferredCounter++));
        }
    }

    @Override
    public void popGuiTransform() {
        if (transforms.remove(transforms.size() - 1) instanceof ScissorData) {
            stencilStack.pop();
        }
    }

    @Override
    public Vec2d transformMouseCursor(Vec2d mouseCursor) {
        for (final GuiTransform transform : transforms) {
            mouseCursor = transform.transformMouseCursorToGui(mouseCursor);
        }
        return mouseCursor;
    }

    @Override
    public float getTickDelta() {
        return tickDelta;
    }

    @Override
    public GuiInputContext getInputContext() {
        return inputContext;
    }

    @Override
    public GuiQuadEmitterImpl getEmitter() {
        return emitter;
    }

    @Override
    public GuiTextRenderer getTextRenderer() {
        return textRenderer;
    }

    @Override
    public void renderItem(final ItemStack stack, final int light, final ModelTransformation.Mode mode) {
        final MatrixStack matrices = itemAdapter.getStack();
        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, mode, light, OverlayTexture.DEFAULT_UV, matrices, getItemAdapter(), 42);
    }

    @Override
    public void addTooltip(final List<OrderedText> components) {
        tooltipMode = true;
        final Vec2d mouse = transformMouseCursor();
        TooltipRenderer.render(components, mouse.x, mouse.y, this);
        tooltipMode = false;
    }

    @Override
    public void renderTooltipBackground(final double x, final double y, final double width, final double height) {
        final GuiRenderMaterial material = GuiRenderMaterialFinder.finder().ignoreLight(true).ignoreTexture(false).texture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).translucent(true).depthTest(false).find();
        NinePatch.render(TooltipRenderer.getTooltipSpriteMap(), x, y, width, height, 0.025, 0.025, 1, this, material);
    }

    @Override
    public void enterSection(final String section) {
        if (TBCExGui.DEBUG) {
            debugStates.add(new DebugState(section, transforms.size()));
        }
    }

    @Override
    public void exitSection(final String section) {
        if (TBCExGui.DEBUG) {
            if (debugStates.isEmpty()) {
                throw new TBCExException("Called exitSection too many times!");
            }
            final DebugState state = debugStates.remove(debugStates.size() - 1);
            if (!state.widgetName.equals(section)) {
                LoggerUtil.LOGGER.error("Entered section {} but did not exit", state.widgetName);
            }
            if (state.transformStackSize != transforms.size()) {
                final StringBuilder builder = new StringBuilder(256);
                for (final DebugState debugState : debugStates) {
                    builder.append(debugState.widgetName);
                    builder.append('{');
                    builder.append(debugState.transformStackSize);
                    builder.append("}:");
                }
                builder.append(state.widgetName);
                LoggerUtil.LOGGER.error("Unbalanced transform stack in section {} started with {} transforms ended with {}", builder.toString(), state.transformStackSize, transforms.size());
            }
        }
    }

    public boolean transformQuad(final MutableGuiQuad quad) {
        return transformQuad(quad, false);
    }

    public boolean transformQuad(final MutableGuiQuad quad, final boolean force) {
        for (int i = transforms.size() - 1; i >= 0; i--) {
            if (!transforms.get(i).transform(quad) && !force) {
                return false;
            }
        }
        return true;
    }

    public DeferredEmittedQuad acquireDeferred() {
        final DeferredEmittedQuad quad;
        if (quadStorage.isEmpty()) {
            quad = new DeferredEmittedQuad();
        } else {
            quad = quadStorage.poll();
        }
        if (tooltipMode) {
            tooltipQuads.add(quad);
        } else {
            inUse.add(Pair.of(quad, stencilStack.isEmpty() ? null : stencilStack.top()));
        }
        return quad;
    }

    public void draw() {
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        inUse.sort(SORTER);
        StencilState stencilState;
        int index = 0;
        final int maxIndex = inUse.size();
        while (index < maxIndex) {
            stencilState = inUse.get(index).getSecond();
            setupStencil(stencilState);
            while (index < maxIndex && stencilState == inUse.get(index).getSecond()) {
                inUse.get(index).getFirst().emit(vertexConsumers);
                index++;
            }
            vertexConsumers.draw();
        }
        for (final Pair<DeferredEmittedQuad, StencilState> pair : inUse) {
            quadStorage.add(pair.getFirst());
        }
        inUse.clear();
        GL11.glDisable(GL11.GL_STENCIL_TEST);
        tooltipQuads.forEach(quad -> quad.emit(vertexConsumers));
        quadStorage.addAll(tooltipQuads);
        tooltipQuads.clear();
        vertexConsumers.draw();
    }

    private void setupStencil(@Nullable final StencilState state) {
        if (state != null) {
            RenderSystem.stencilMask(0xFF);
            RenderSystem.clearStencil(0);
            RenderSystem.clear(GL11.GL_STENCIL_BUFFER_BIT, MinecraftClient.IS_SYSTEM_MAC);
            RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_INCR, GL11.GL_INCR);
            RenderSystem.stencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
            StencilState stencilState = state;
            VertexConsumer consumer = vertexConsumers.getBuffer(GuiRenderLayers.STENCIL_LAYER);
            while (stencilState != null) {
                for (int i = 0; i < 4; i++) {
                    consumer.vertex(stencilState.quad.x(i), stencilState.quad.y(i), stencilState.quad.depthByIndex(i));
                    consumer.next();
                }
                stencilState = stencilState.parent;
            }
            if (MinecraftClient.getInstance().options.graphicsMode == GraphicsMode.FABULOUS) {
                stencilState = state;
                consumer = vertexConsumers.getBuffer(GuiRenderLayers.STENCIL_LAYER_TRANSLUCENT);
                while (stencilState != null) {
                    for (int i = 0; i < 4; i++) {
                        consumer.vertex(stencilState.quad.x(i), stencilState.quad.y(i), stencilState.quad.depthByIndex(i));
                        consumer.next();
                    }
                    stencilState = stencilState.parent;
                }
            }
            vertexConsumers.draw();
            RenderSystem.stencilMask(0x00);
            RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
            RenderSystem.stencilFunc(GL11.GL_EQUAL, state.depth + 1, 0xFF);
        } else {
            RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
            RenderSystem.stencilFunc(GL11.GL_ALWAYS, 0, 0xFF);
        }
    }

    public GuiVcpTextAdapter getTextAdapter() {
        return textAdapter;
    }

    public GuiVcpItemAdapter getItemAdapter() {
        return itemAdapter;
    }

    private static final class StencilState {
        public static final Comparator<StencilState> COMPARATOR = Comparator.nullsFirst(Comparator.<StencilState>comparingInt(state -> state.depth).thenComparingInt(state -> state.counter));
        private final @Nullable StencilState parent;
        private final MutableGuiQuadImpl quad;
        private final int depth;
        private final int counter;

        private StencilState(@Nullable final StencilState parent, final MutableGuiQuadImpl quad, final int counter) {
            this.parent = parent;
            this.quad = quad;
            this.counter = counter;
            if (parent == null) {
                depth = 0;
            } else {
                depth = parent.depth + 1;
            }
        }
    }

    private static final class DebugState {
        private final String widgetName;
        private final int transformStackSize;

        private DebugState(final String widgetName, final int transformStackSize) {
            this.widgetName = widgetName;
            this.transformStackSize = transformStackSize;
        }
    }
}
