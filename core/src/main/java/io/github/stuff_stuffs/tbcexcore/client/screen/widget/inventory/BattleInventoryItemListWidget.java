package io.github.stuff_stuffs.tbcexcore.client.screen.widget.inventory;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantInventoryHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItemStack;
import io.github.stuff_stuffs.tbcexgui.client.api.GuiContext;
import io.github.stuff_stuffs.tbcexgui.client.api.GuiInputContext;
import io.github.stuff_stuffs.tbcexgui.client.api.GuiRenderMaterial;
import io.github.stuff_stuffs.tbcexgui.client.api.text.TextDrawer;
import io.github.stuff_stuffs.tbcexgui.client.api.text.TextDrawers;
import io.github.stuff_stuffs.tbcexgui.client.widget.AbstractWidget;
import io.github.stuff_stuffs.tbcexgui.client.widget.Widget;
import io.github.stuff_stuffs.tbcexutil.common.Rect2d;
import io.github.stuff_stuffs.tbcexutil.common.Vec2d;
import io.github.stuff_stuffs.tbcexutil.common.colour.Colour;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.function.Function;

public class BattleInventoryItemListWidget {
    private static final double BUTTON_HEIGHT = 1 / 16.0;
    private static final double BUTTON_SPACING = 1 / 64.0;
    private final double widthFactor;
    private List<BattleParticipantInventoryHandle> handles = List.of();
    private Function<BattleParticipantInventoryHandle, BattleParticipantItemStack> stackGetter = i -> null;
    private double position;
    private int selected = -1;

    public BattleInventoryItemListWidget(double widthFactor) {
        this.widthFactor = widthFactor;
    }

    public void setHandles(final List<BattleParticipantInventoryHandle> handles, final Function<BattleParticipantInventoryHandle, BattleParticipantItemStack> stackGetter) {
        this.handles = handles;
        this.stackGetter = stackGetter;
        selected = -1;
    }

    public @Nullable BattleParticipantInventoryHandle getSelectedHandle() {
        if (selected < 0 || selected >= handles.size()) {
            return null;
        }
        return handles.get(selected);
    }

    public void setPosition(final double position) {
        if (position < 0) {
            this.position = 0;
        } else {
            this.position = Math.min(position, BUTTON_HEIGHT * (handles.size() - 1));
        }
    }

    public void render(final GuiContext context, final double screenWidth, final double screenHeight) {
        context.enterSection(getDebugName());
        final double width = screenWidth * widthFactor;
        final double height = screenHeight;
        final Rect2d rect = new Rect2d(0, 0, width, height);
        AbstractWidget.processEvents(context, event -> {
            if (event instanceof GuiInputContext.MouseScroll scroll) {
                final Vec2d mouseCursor = context.transformMouseCursor(new Vec2d(scroll.mouseX, scroll.mouseY));
                if (rect.isIn(mouseCursor.x, mouseCursor.y)) {
                    final Vec2d amount = new Vec2d(0, scroll.amount);
                    final Vec2d d = context.transformMouseCursor(amount);
                    setPosition(position + d.length() / 8.0 * Math.signum(scroll.amount));
                    return true;
                }
            }
            return false;
        });

        final int c1 = Widget.HOVER.pack(224);
        context.getEmitter().rectangle(0, 0, width, BUTTON_HEIGHT, c1, c1, c1, c1).renderMaterial(GuiRenderMaterial.POS_COLOUR_TRANSLUCENT).emit();
        final TextDrawer t = TextDrawers.oneShot(TextDrawers.HorizontalJustification.CENTER, TextDrawers.VerticalJustification.CENTER, Colour.WHITE.pack(), 0, false);
        context.pushTranslate(width / 3.0, BUTTON_HEIGHT / 2.0, 0.1);
        t.draw(width / 3.0, BUTTON_HEIGHT, new LiteralText("Name").asOrderedText(), context);
        context.popGuiTransform();
        context.pushTranslate(2 * width / 3.0, BUTTON_HEIGHT / 2.0, 0.1);
        t.draw(width / 3.0, BUTTON_HEIGHT, new LiteralText("Count").asOrderedText(), context);
        context.popGuiTransform();

        context.pushScissor(0, (float) (BUTTON_HEIGHT + BUTTON_SPACING), (float) width, (float) height);
        context.pushTranslate(0, -position, 0);


        final int size = handles.size();
        for (int i = 0; i < size; i++) {
            renderEntry(context, i, width, height);
        }
        context.popGuiTransform();
        context.popGuiTransform();

        context.exitSection(getDebugName());
    }

    private void renderEntry(final GuiContext context, final int i, final double width, final double height) {
        context.pushTranslate(0, (i + 1) * (BUTTON_HEIGHT + BUTTON_SPACING), 0);
        final Vec2d mouseCursor = context.transformMouseCursor();
        final Rect2d r = new Rect2d(0, 0, width, BUTTON_HEIGHT);
        AbstractWidget.processEvents(context, event -> {
            if (event instanceof GuiInputContext.MouseClick click) {
                final Vec2d mouse = context.transformMouseCursor(new Vec2d(click.mouseX, click.mouseY));
                if (click.button == GLFW.GLFW_MOUSE_BUTTON_LEFT && r.isIn(mouse.x, mouse.y)) {
                    selected = i;
                    return true;
                }
            }
            return false;
        });
        final BattleParticipantInventoryHandle handle = handles.get(i);
        final BattleParticipantItemStack stack = stackGetter.apply(handle);

        final int c = (r.isIn(mouseCursor.x, mouseCursor.y) || i == selected) ? Widget.HOVER.pack(255) : (i & 1) == 0 ? Widget.NON_HOVER.pack(192) : Widget.HOVER.pack(192);
        context.getEmitter().rectangle(0, 0, width, BUTTON_HEIGHT, c, c, c, c).renderMaterial(GuiRenderMaterial.POS_COLOUR_TRANSLUCENT).emit();

        if (stack == null) {

        } else {
            final TextDrawer textDrawer = TextDrawers.oneShot(TextDrawers.HorizontalJustification.CENTER, TextDrawers.VerticalJustification.CENTER, Colour.WHITE.pack(), 0, false);
            context.pushTranslate(width / 3.0, BUTTON_HEIGHT / 2.0, 0.1);
            final Style style = Style.EMPTY.withColor(stack.getItem().getRarity().getRarityClass().getColour().pack(255));
            final MutableText copy = stack.getItem().getName().copy();
            copy.setStyle(style);
            textDrawer.draw(width / 3.0, BUTTON_HEIGHT, copy.asOrderedText(), context);
            context.popGuiTransform();

            context.pushTranslate(2 * width / 3.0, BUTTON_HEIGHT / 2.0, 0.1);
            textDrawer.draw(width / 3.0, BUTTON_HEIGHT, new LiteralText("" + stack.getCount()).asOrderedText(), context);
            context.popGuiTransform();
        }

        context.popGuiTransform();
    }

    public String getDebugName() {
        return "BattleInventoryItemListWidget";
    }
}
