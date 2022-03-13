package io.github.stuff_stuffs.tbcexcore.client.screen.widget.inventory;

import io.github.stuff_stuffs.tbcexcore.client.render.BattleParticipantItemRenderer;
import io.github.stuff_stuffs.tbcexcore.client.render.TBCExCoreRenderRegistries;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantInventoryHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItemStack;
import io.github.stuff_stuffs.tbcexgui.client.api.GuiContext;
import io.github.stuff_stuffs.tbcexgui.client.api.GuiInputContext;
import io.github.stuff_stuffs.tbcexgui.client.api.text.OrderedTextUtil;
import io.github.stuff_stuffs.tbcexgui.client.api.text.TextDrawer;
import io.github.stuff_stuffs.tbcexgui.client.api.text.TextDrawers;
import io.github.stuff_stuffs.tbcexgui.client.widget.AbstractWidget;
import io.github.stuff_stuffs.tbcexutil.common.DoubleQuaternion;
import io.github.stuff_stuffs.tbcexutil.common.Rect2d;
import io.github.stuff_stuffs.tbcexutil.common.Vec2d;
import net.minecraft.text.*;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.function.Function;

public class BattleInventoryPreviewWidget {
    private static final Vec3d ROTATION_AXIS = new Vec3d(0, 1, 0);
    private static final Vec3d X_DRAG_AXIS = new Vec3d(0, -1, 0);
    private static final Vec3d Y_DRAG_AXIS = new Vec3d(1, 0, 0);
    private final double widthFactor;
    private @Nullable BattleParticipantInventoryHandle selected;
    private Function<BattleParticipantInventoryHandle, @Nullable BattleParticipantItemStack> stackGetter = i -> null;
    private DoubleQuaternion rotation = DoubleQuaternion.IDENTITY;
    private long lastMillis = 0;
    private boolean rotating = true;

    public BattleInventoryPreviewWidget(final double widthFactor) {
        this.widthFactor = widthFactor;
    }

    public void setSelected(final BattleParticipantInventoryHandle selected, final Function<BattleParticipantInventoryHandle, @Nullable BattleParticipantItemStack> stackGetter) {
        this.selected = selected;
        this.stackGetter = stackGetter;
        rotation = DoubleQuaternion.IDENTITY;
        rotating = true;
    }

    public void render(final GuiContext context, final double screenWidth, final double screenHeight) {
        context.enterSection(getDebugName());

        final Rect2d rect = new Rect2d(0, 0, widthFactor * screenWidth, screenHeight);
        AbstractWidget.processEvents(context, event -> {
            if (event instanceof GuiInputContext.MouseClick click) {
                final Vec2d mouseCursor = context.transformMouseCursor(new Vec2d(click.mouseX, click.mouseY));
                if (rect.isIn(mouseCursor.x, mouseCursor.y) && click.button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                    rotating = !rotating;
                    return true;
                }
            } else if (event instanceof GuiInputContext.MouseDrag drag) {
                final Vec2d mouseCursor = context.transformMouseCursor(new Vec2d(drag.mouseX, drag.mouseY));
                if (rect.isIn(mouseCursor.x, mouseCursor.y) && drag.button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                    rotation = new DoubleQuaternion(Y_DRAG_AXIS, drag.deltaY * 360 * 0.001, true).multiply(rotation);
                    rotation = new DoubleQuaternion(X_DRAG_AXIS, drag.deltaX * 360 * 0.001, true).multiply(rotation);
                    return true;
                }
            }
            return false;
        });

        if (lastMillis != 0) {
            final long current = Util.getMeasuringTimeMs();
            final double seconds = (current - lastMillis) / 1000.0;
            final double speed = 0.05;
            if (rotating) {
                rotation = rotation.multiply(new DoubleQuaternion(ROTATION_AXIS, (speed * seconds) * 360, true));
            }
            lastMillis = current;
        } else {
            lastMillis = Util.getMeasuringTimeMs();
        }

        if (selected != null) {
            final BattleParticipantItemStack stack = stackGetter.apply(selected);
            if (stack != null) {
                context.pushTranslate(0.125 * Math.sqrt(2) * 1.1 * screenWidth, 0.125 * Math.sqrt(2), 10);
                final double min = Math.min(screenWidth, screenHeight);
                context.pushScale(0.20 * min, 0.20 * min, 0.20 * min);
                context.pushRotate(rotation.toFloatQuat());
                final BattleParticipantItemRenderer renderer = TBCExCoreRenderRegistries.getItemRenderer(stack.getItem().getType());

                final String section = "ItemRenderer:" + stack.getItem().getType();
                context.enterSection(section);
                renderer.renderInGui(context, stack, null);
                context.exitSection(section);

                context.popGuiTransform();
                context.popGuiTransform();
                context.popGuiTransform();

                final double padding = 0.005;
                final MutableText name = stack.getItem().getName().copy();
                name.setStyle(Style.EMPTY.withColor(stack.getItem().getRarity().getRarityClass().getColour().pack(255)));
                name.append(new LiteralText("["));
                name.append(stack.getItem().getRarity().getAsText());
                name.append(new LiteralText("]"));
                final OrderedTextUtil.LengthSplitHeuristic splitHeuristic = OrderedTextUtil.simpleLengthSplitHeuristic(screenWidth * widthFactor * (1 - padding * 2)*32, 8, 4);
                final List<OrderedText> texts = OrderedTextUtil.lengthSplit(name.asOrderedText(), splitHeuristic);

                context.pushTranslate(padding * screenWidth * widthFactor, 1 / 3.0 * screenHeight, 10);
                context.renderTooltipBackground(0, 0, screenWidth * widthFactor * (1 - padding * 2), 2 / 3.0 * screenHeight);
                context.pushTranslate(screenWidth * widthFactor/2.0,0.025,1);

                TextDrawer drawer = TextDrawers.oneShot(TextDrawers.HorizontalJustification.CENTER, TextDrawers.VerticalJustification.BOTTOM, 0xffffffff, 0, false);
                final int size = texts.size();
                double off = 0;
                for (int i = 0; i < size; i++) {
                    OrderedText text = texts.get(i);
                    context.pushTranslate(0,off/256.0, 0);
                    drawer.draw(screenWidth * widthFactor * (1 - padding * 2), 9/256.0, text, context);
                    context.popGuiTransform();
                    off += context.getTextRenderer().getHeight(text);
                }

                context.popGuiTransform();
                context.popGuiTransform();
            }
        }

        context.exitSection(getDebugName());
    }

    private String getDebugName() {
        return "BattleInventoryPreview";
    }
}
