package io.github.stuff_stuffs.tbcexcore.client.render.hud;

import io.github.stuff_stuffs.tbcexcore.client.render.hud.widget.BattleHudEnergyWidget;
import io.github.stuff_stuffs.tbcexcore.client.render.screen.BattleHudContext;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.Battle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStats;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexcore.mixin.api.BattleWorldHolder;
import io.github.stuff_stuffs.tbcexgui.client.screen.TBCExHud;
import io.github.stuff_stuffs.tbcexgui.client.widget.PositionedWidget;
import io.github.stuff_stuffs.tbcexgui.client.widget.panel.RootPanelWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;

public class BattleHudScreen extends TBCExHud {
    private final BattleHandle handle;
    private final PlayerEntity entity;
    private final BattleHudContext.Impl context;

    public BattleHudScreen(final BattleHandle handle, final PlayerEntity entity) {
        super(new RootPanelWidget(false), "In Battle");
        this.handle = handle;
        this.entity = entity;
        //TODO
        context = new BattleHudContext.Impl(10, 5);
        ((RootPanelWidget) root).addChild(PositionedWidget.wrap(new BattleHudEnergyWidget(0.5, 0.025, context, new BattleParticipantHandle(handle, entity.getUuid()), entity.world), 0.25, 0.975));
    }

    @Override
    public void render(final MatrixStack matrices, final double mouseX, final double mouseY, final float tickDelta) {
        final Battle battle = ((BattleWorldHolder) entity.world).tbcex$getBattleWorld().getBattle(handle);
        if (battle != null) {
            final BattleParticipantHandle participantHandle = new BattleParticipantHandle(handle, entity.getUuid());
            final BattleParticipantStateView participant = battle.getState().getParticipant(participantHandle);
            context.setTotalEnergy(participant.getStat(BattleParticipantStats.MAX_ENERGY));
            context.setEnergy(participant.getEnergy());
        }
        super.render(matrices, mouseX, mouseY, tickDelta);
        context.setPotentialEnergyCost(0);
    }

    public boolean matches(final BattleHandle handle) {
        return this.handle.equals(handle);
    }

    public BattleHudContext getContext() {
        return context;
    }
}
