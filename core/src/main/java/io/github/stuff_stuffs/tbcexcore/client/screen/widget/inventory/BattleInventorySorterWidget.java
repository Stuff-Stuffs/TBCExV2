package io.github.stuff_stuffs.tbcexcore.client.screen.widget.inventory;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantInventoryHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItemRarity;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItemStack;
import io.github.stuff_stuffs.tbcexgui.client.api.GuiContext;
import io.github.stuff_stuffs.tbcexgui.client.api.GuiInputContext;
import io.github.stuff_stuffs.tbcexgui.client.api.GuiRenderMaterial;
import io.github.stuff_stuffs.tbcexgui.client.api.text.TextDrawer;
import io.github.stuff_stuffs.tbcexgui.client.api.text.TextDrawers;
import io.github.stuff_stuffs.tbcexgui.client.widget.AbstractWidget;
import io.github.stuff_stuffs.tbcexutil.common.Rect2d;
import io.github.stuff_stuffs.tbcexutil.common.Vec2d;
import io.github.stuff_stuffs.tbcexutil.common.colour.Colour;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class BattleInventorySorterWidget {
    private static final List<Sorter> STACK_SORTERS = List.of(
            new Sorter() {
                @Override
                public Comparator<BattleParticipantItemStack> getComparator() {
                    return Comparator.comparing(i -> i.getItem().getName().asString());
                }

                @Override
                public Text getName() {
                    return new LiteralText("Alphabetical");
                }
            },
            new Sorter() {
                @Override
                public Comparator<BattleParticipantItemStack> getComparator() {
                    return Comparator.comparing(BattleParticipantItemStack::getCount);
                }

                @Override
                public Text getName() {
                    return new LiteralText("Count");
                }
            },
            new Sorter() {
                @Override
                public Comparator<BattleParticipantItemStack> getComparator() {
                    return Comparator.comparing(i -> i.getItem().getRarity(), BattleParticipantItemRarity.COMPARATOR);
                }

                @Override
                public Text getName() {
                    return new LiteralText("Rarity");
                }
            }
    );
    private static final double BUTTON_SPACING = 1 / 32.0;
    private static final double BUTTON_HEIGHT = 1 / 8.0;
    private static final double WIDTH = 0.125;
    private final List<Entry> entries;
    private final double widthFactor;
    private final double heightFactor;
    private double position;
    private int sortMode = Integer.MIN_VALUE;

    public BattleInventorySorterWidget(final double widthFactor, final double heightFactor) {
        this.widthFactor = widthFactor;
        this.heightFactor = heightFactor;
        entries = new ArrayList<>();
        int i = 0;
        for (final Sorter sorter : STACK_SORTERS) {
            entries.add(new Entry(sorter, i++, WIDTH, BUTTON_HEIGHT));
        }
    }

    private void setPosition(final double position) {
        if (position < 0) {
            this.position = 0;
        } else {
            this.position = Math.min(position, BUTTON_HEIGHT * entries.size());
        }
    }

    public void sort(final List<BattleParticipantInventoryHandle> handles, final Function<BattleParticipantInventoryHandle, BattleParticipantItemStack> stackGetter) {
        if (sortMode == Integer.MIN_VALUE) {
            return;
        }
        final Comparator<BattleParticipantInventoryHandle> comparator = Comparator.comparing(stackGetter,
                sortMode < 0 ?
                        entries.get(Math.abs(sortMode)).sorter.getComparator().reversed() :
                        entries.get(Math.abs(sortMode)).sorter.getComparator()
        );

        handles.sort(comparator);
    }

    public void render(final GuiContext context, final double screenWidth, final double screenHeight) {
        context.enterSection(getDebugName());

        final Rect2d rect = new Rect2d(0, 0, screenWidth * widthFactor, screenHeight * heightFactor);
        AbstractWidget.processEvents(context, event -> {
            if (event instanceof GuiInputContext.MouseScroll scroll) {
                final Vec2d mouseCursor = context.transformMouseCursor(new Vec2d(scroll.mouseX, scroll.mouseY));
                if (rect.isIn(mouseCursor.x, mouseCursor.y)) {
                    final Vec2d amount = new Vec2d(0, scroll.amount);
                    final Vec2d d = context.transformMouseCursor(amount);
                    setPosition(position + d.length() * Math.signum(scroll.amount));
                    return true;
                }
            }
            return false;
        });

        for (int i = 0; i < entries.size(); i++) {
            final double curPos = i * (BUTTON_HEIGHT + BUTTON_SPACING);
            double scaleFactor = Math.abs(position - curPos);
            scaleFactor = scaleFactor * scaleFactor;
            context.pushTranslate(-scaleFactor * screenWidth, (curPos - position) * screenHeight, 0.01);
            entries.get(i).render(context, screenWidth, screenHeight);
            context.popGuiTransform();
        }

        context.exitSection(getDebugName());
    }

    public String getDebugName() {
        return "BattleInventorySorter";
    }

    private final class Entry {
        private static final TextDrawer TEXT_DRAWER = TextDrawers.oneShot(TextDrawers.HorizontalJustification.CENTER, TextDrawers.VerticalJustification.CENTER, Colour.WHITE.pack(), 0, false);
        private static final TextDrawer HOVERED_TEXT_DRAWER = TextDrawers.oneShot(TextDrawers.HorizontalJustification.CENTER, TextDrawers.VerticalJustification.CENTER, Colour.WHITE.pack(), 0, true);
        private static final int HOVER = AbstractWidget.HOVER.pack(224);
        private static final int NON_HOVER = AbstractWidget.NON_HOVER.pack(192);
        private final Sorter sorter;
        private final int index;
        private final double width;
        private final double height;
        private State state = State.NONE;

        private Entry(final Sorter sorter, final int index, final double width, final double height) {
            this.sorter = sorter;
            this.index = index;
            this.width = width;
            this.height = height;
        }

        private void render(final GuiContext context, final double screenWidth, final double screenHeight) {
            final double width = this.width * screenWidth * 0.75;
            final double height = this.height * screenHeight;
            final Rect2d rect = new Rect2d(0, 0, width, height);
            AbstractWidget.processEvents(context, event -> {
                if (event instanceof GuiInputContext.MouseClick click) {
                    if (click.button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                        final Vec2d mouseCursor = context.transformMouseCursor(new Vec2d(click.mouseX, click.mouseY));
                        if (rect.isIn(mouseCursor.x, mouseCursor.y)) {
                            state = state.next();
                            if (state != State.NONE) {
                                if (sortMode == index | sortMode == -index) {
                                    sortMode = -sortMode;
                                } else {
                                    sortMode = index;
                                }
                            } else {
                                sortMode = Integer.MIN_VALUE;
                            }
                            setPosition(index * width);
                            return true;
                        }
                    }
                }
                return false;
            });
            final Vec2d mouseCursor = context.transformMouseCursor();
            final boolean hovered = state != State.NONE || rect.isIn(mouseCursor.x, mouseCursor.y);
            final int c = hovered ? HOVER : NON_HOVER;
            context.pushTranslate(this.width * screenWidth * 0.25, 0, 0);
            context.getEmitter().rectangle(0, 0, width, height, c, c, c, c).renderMaterial(GuiRenderMaterial.POS_COLOUR_TRANSLUCENT).emit();
            context.pushTranslate(width / 2.0, height / 2.0, 0.01);
            final Text text = switch (state) {
                case NONE -> sorter.getName();
                case NORMAL -> new LiteralText("\u1A08").append(sorter.getName());
                case REVERSED -> new LiteralText("\u1A06").append(sorter.getName());
            };
            if (hovered) {
                HOVERED_TEXT_DRAWER.draw(width * 0.95, height, text.asOrderedText(), context);
            } else {
                TEXT_DRAWER.draw(width * 0.95, height, text.asOrderedText(), context);
            }
            context.popGuiTransform();
            context.popGuiTransform();
        }

        private enum State {
            NONE,
            NORMAL,
            REVERSED;

            public State next() {
                return switch (this) {
                    case NONE -> NORMAL;
                    case NORMAL -> REVERSED;
                    case REVERSED -> NONE;
                };
            }
        }
    }

    private interface Sorter {
        Comparator<BattleParticipantItemStack> getComparator();

        Text getName();
    }
}
