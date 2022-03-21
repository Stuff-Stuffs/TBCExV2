package io.github.stuff_stuffs.tbcexcore.client.render.screen.widget.stats;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.Battle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStat;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStats;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexcore.mixin.api.BattleWorldHolder;
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
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.text.DecimalFormat;
import java.util.List;
import java.util.OptionalDouble;

public class BattleParticipantStatsDisplay {
    private static final DecimalFormat FORMAT = new DecimalFormat("0.00");
    private static final TextDrawer STAT_NAME_DRAWER = TextDrawers.oneShot(TextDrawers.HorizontalJustification.LEFT, TextDrawers.VerticalJustification.TOP, Colour.WHITE.pack(), 0, false);
    private static final TextDrawer STAT_NAME_DRAWER_HOVER = TextDrawers.oneShot(TextDrawers.HorizontalJustification.LEFT, TextDrawers.VerticalJustification.TOP, Colour.WHITE.pack(), 0, true);

    private static final double HEIGHT_FACTOR = 0.5;
    private static final double ENTRY_WIDTH_FACTOR = 1 / 4.0;
    private static final double ENTRY_HEIGHT = 1 / 16.0;
    private static final double ENTRY_SPACING = 1 / 64.0;
    private final BattleParticipantHandle participantHandle;
    private final World world;
    private final List<BattleParticipantStat> statsToDisplay;
    private double position;

    public BattleParticipantStatsDisplay(final BattleParticipantHandle participantHandle, final World world) {
        this.participantHandle = participantHandle;
        this.world = world;
        statsToDisplay = BattleParticipantStats.REGISTRY.stream().filter(BattleParticipantStat::isVisible).toList();
    }

    private void setPosition(final double position) {
        if (position < 0) {
            this.position = 0;
        } else {
            this.position = Math.min(position, ENTRY_HEIGHT * (statsToDisplay.size() - 1));
        }
    }

    public void render(final GuiContext context, final double screenWidth, final double screenHeight) {
        context.enterSection(getDebugName());

        final double width = screenWidth * ENTRY_WIDTH_FACTOR;
        final double height = screenHeight * HEIGHT_FACTOR;
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

        context.pushScissor(0, (float) (ENTRY_HEIGHT + ENTRY_SPACING), (float) width, (float) height);
        context.pushTranslate(0, -position, 0);


        final int size = statsToDisplay.size();
        for (int i = 0; i < size; i++) {
            renderEntry(context, i, width, height);
        }
        context.popGuiTransform();
        context.popGuiTransform();

        context.exitSection(getDebugName());
    }

    //TODO exact info
    private void renderEntry(final GuiContext context, final int i, final double width, final double height) {
        context.pushTranslate(0, (i + 1) * (ENTRY_HEIGHT + ENTRY_SPACING), 0);

        final Vec2d mouseCursor = context.transformMouseCursor();
        final Rect2d r = new Rect2d(0, 0, width, ENTRY_HEIGHT);

        final boolean isHovered = r.isIn(mouseCursor.x, mouseCursor.y);
        final int c = isHovered ? Widget.HOVER.pack(255) : (i & 1) == 0 ? Widget.NON_HOVER.pack(192) : Widget.HOVER.pack(192);
        context.getEmitter().rectangle(0, 0, width, ENTRY_HEIGHT, c, c, c, c).renderMaterial(GuiRenderMaterial.POS_COLOUR_TRANSLUCENT).emit();

        final BattleParticipantStat stat = statsToDisplay.get(i);
        context.pushTranslate(0, 0, 0.1);
        (isHovered ? STAT_NAME_DRAWER_HOVER : STAT_NAME_DRAWER).draw(width * 0.5 * 0.95, ENTRY_HEIGHT, stat.getDisplayName().asOrderedText(), context);
        context.popGuiTransform();

        final OptionalDouble statValue = getStatValue(stat);
        final Text valueText;
        if (statValue.isEmpty()) {
            valueText = new LiteralText("Error");
        } else {

            final double val = statValue.getAsDouble();
            final Style style = Style.EMPTY.withColor(val == 0 ? 0xFF7F7F7F : (val < 0 ? 0xFF7F0000 : 0xFF007F00));
            valueText = new LiteralText("" + FORMAT.format(val)).setStyle(style);
        }
        context.pushTranslate(width * 0.5, 0, 0.1);
        (isHovered ? STAT_NAME_DRAWER_HOVER : STAT_NAME_DRAWER).draw(width * 0.5 * 0.95, ENTRY_HEIGHT, valueText.asOrderedText(), context);
        context.popGuiTransform();


        context.popGuiTransform();
    }

    private OptionalDouble getStatValue(final BattleParticipantStat stat) {
        final Battle battle = ((BattleWorldHolder) world).tbcex$getBattleWorld().getBattle(participantHandle.getParent());
        if (battle == null) {
            return OptionalDouble.empty();
        }
        final BattleParticipantStateView participant = battle.getState().getParticipant(participantHandle);
        if (participant == null) {
            return OptionalDouble.empty();
        }
        return OptionalDouble.of(participant.getStat(stat));
    }

    public String getDebugName() {
        return "BattleStatsDisplay";
    }
}
