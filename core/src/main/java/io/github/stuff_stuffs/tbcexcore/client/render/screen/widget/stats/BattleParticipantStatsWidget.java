package io.github.stuff_stuffs.tbcexcore.client.render.screen.widget.stats;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.Battle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStat;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexcore.mixin.api.BattleWorldHolder;
import io.github.stuff_stuffs.tbcexgui.client.api.GuiContext;
import io.github.stuff_stuffs.tbcexgui.client.api.GuiRenderMaterial;
import io.github.stuff_stuffs.tbcexgui.client.widget.AbstractWidget;
import net.minecraft.world.World;

import java.util.function.ToDoubleFunction;

public class BattleParticipantStatsWidget extends AbstractWidget {
    private final BattleParticipantHandle participantHandle;
    private final World world;
    private final BattleParticipantStatsDisplay display;

    public BattleParticipantStatsWidget(final BattleParticipantHandle participantHandle, final World world) {
        this.participantHandle = participantHandle;
        this.world = world;
        display = new BattleParticipantStatsDisplay(participantHandle, world);
    }

    @Override
    public void render(final GuiContext context) {
        context.enterSection(getDebugName());

        final double width = getScreenWidth();
        final double height = getScreenHeight();

        int c = NON_HOVER.pack(127);
        context.getEmitter().rectangle(-(width-1)/2.0, -(height-1)/2.0, width, height, c,c,c,c).renderMaterial(GuiRenderMaterial.POS_COLOUR_TRANSLUCENT).emit();

        context.pushTranslate(0.5, 0.5, 1);
        display.render(context, width, height);
        context.popGuiTransform();

        context.exitSection(getDebugName());
    }

    private ToDoubleFunction<BattleParticipantStat> getStatRetriever() {
        final Battle battle = ((BattleWorldHolder) world).tbcex$getBattleWorld().getBattle(participantHandle.getParent());
        if (battle == null) {
            return null;
        }
        final BattleParticipantStateView participant = battle.getState().getParticipant(participantHandle);
        if (participant == null) {
            return null;
        }
        return participant::getStat;
    }

    @Override
    public String getDebugName() {
        return "BattleStats";
    }
}
