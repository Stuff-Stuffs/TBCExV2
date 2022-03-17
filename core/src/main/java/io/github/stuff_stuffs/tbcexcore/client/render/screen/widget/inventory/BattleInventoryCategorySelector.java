package io.github.stuff_stuffs.tbcexcore.client.render.screen.widget.inventory;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantInventoryHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItemCategory;
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
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class BattleInventoryCategorySelector {
    private static final double BUTTON_SPACING = 1 / 32.0;
    private static final double BUTTON_HEIGHT = 1 / 8.0;
    private final List<Entry> entries;
    private final double widthFactor;
    private final double heightFactor;
    private double position;
    private BattleParticipantItemCategory category;

    public BattleInventoryCategorySelector(final double widthFactor, final double heightFactor) {
        this.heightFactor = heightFactor;
        entries = new ArrayList<>();
        this.widthFactor = widthFactor;
        int i = 0;
        for (final BattleParticipantItemCategory category : BattleParticipantItemCategory.getAllCategories()) {
            entries.add(new Entry(category, widthFactor, BUTTON_HEIGHT, i++));
        }
        setPosition(0);
    }

    public void filter(final List<BattleParticipantInventoryHandle> handles, final Function<BattleParticipantInventoryHandle, BattleParticipantItemStack> stackGetter) {
        if (category == null) {
            return;
        }
        handles.removeIf(h -> !stackGetter.apply(h).getItem().getCategory().contains(category));
    }

    private void setPosition(final double position) {
        this.position = Math.max(Math.min(position, (BUTTON_HEIGHT + BUTTON_SPACING) * entries.size()), (BUTTON_HEIGHT + BUTTON_SPACING));
    }

    public void render(final GuiContext context, final double width, final double height) {
        context.enterSection(getDebugName());
        final Rect2d rect = new Rect2d(0, height * heightFactor, widthFactor * width, height);
        AbstractWidget.processEvents(context, event -> {
            if (event instanceof GuiInputContext.MouseScroll scroll) {
                final Vec2d mouseCursor = context.transformMouseCursor(new Vec2d(scroll.mouseX, scroll.mouseY));
                if (rect.isIn(mouseCursor.x, mouseCursor.y)) {
                    final Vec2d amount = new Vec2d(0, scroll.amount);
                    final Vec2d transformedScroll = context.transformMouseCursor(amount);
                    setPosition(position + transformedScroll.length() * Math.signum(scroll.amount));
                    return true;
                }
            }
            return false;
        });

        context.pushTranslate(0, height, 0);
        for (int i = 0; i < entries.size(); i++) {
            final double curPos = i * (BUTTON_HEIGHT + BUTTON_SPACING);
            double scaleFactor = Math.abs(position - curPos);
            scaleFactor = scaleFactor * scaleFactor * 0.5;
            context.pushTranslate(-scaleFactor * width, (curPos - position) * height, 0.01);
            entries.get(i).render(context, width, height);
            context.popGuiTransform();
        }
        context.popGuiTransform();

        context.exitSection(getDebugName());
    }

    public String getDebugName() {
        return "CategorySelector";
    }

    private final class Entry {
        private static final int NON_HOVER_COLOUR = Widget.NON_HOVER.pack(192);
        private static final int HOVER_COLOUR = Widget.HOVER.pack(224);
        private static final GuiRenderMaterial MATERIAL = GuiRenderMaterial.POS_COLOUR_TRANSLUCENT;
        private static final TextDrawer TEXT_DRAWER = TextDrawers.oneShot(TextDrawers.HorizontalJustification.CENTER, TextDrawers.VerticalJustification.CENTER, Colour.WHITE.pack(), 0, true);
        private final BattleParticipantItemCategory category;
        private final double width;
        private final double height;
        private final int index;

        private Entry(final BattleParticipantItemCategory category, final double width, final double height, final int index) {
            this.category = category;
            this.width = width;
            this.height = height;
            this.index = index;
        }

        public void render(final GuiContext context, final double screenWidth, final double screenHeight) {
            final double width = this.width * screenWidth * 0.75;
            final double height = this.height * screenHeight;
            final Rect2d rect = new Rect2d(0, 0, width, height);
            AbstractWidget.processEvents(context, event -> {
                if (event instanceof GuiInputContext.MouseClick click) {
                    final Vec2d mouseCursor = context.transformMouseCursor(new Vec2d(click.mouseX, click.mouseY));
                    if (click.button == GLFW.GLFW_MOUSE_BUTTON_LEFT && rect.isIn(mouseCursor.x, mouseCursor.y)) {
                        if (BattleInventoryCategorySelector.this.category != category) {
                            BattleInventoryCategorySelector.this.category = category;
                        } else {
                            BattleInventoryCategorySelector.this.category = null;
                        }
                        setPosition((index + 1) * (this.height + BUTTON_SPACING));
                        return true;
                    }
                }
                return false;
            });
            final Vec2d mouseCursor = context.transformMouseCursor();
            final int c = category == BattleInventoryCategorySelector.this.category || rect.isIn(mouseCursor.x, mouseCursor.y) ? HOVER_COLOUR : NON_HOVER_COLOUR;
            context.pushTranslate(this.width * screenWidth * 0.25, 0, 0);
            context.getEmitter().rectangle(0, 0, width, height, c, c, c, c).renderMaterial(MATERIAL).emit();
            context.pushTranslate(width / 2.0, height / 2.0, 0.01);
            TEXT_DRAWER.draw(width * 0.95, height, category.getName().asOrderedText(), context);
            context.popGuiTransform();
            context.popGuiTransform();
        }
    }
}
