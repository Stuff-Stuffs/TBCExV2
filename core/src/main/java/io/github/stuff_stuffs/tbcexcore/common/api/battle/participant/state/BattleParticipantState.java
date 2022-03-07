package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipantTeam;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffectContainer;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.health.BattleParticipantHealthContainer;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantInventory;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStatContainer;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleState;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;
import io.github.stuff_stuffs.tbcexutil.common.event.map.MutEventMap;

public interface BattleParticipantState extends BattleParticipantStateView {
    @Override
    MutEventMap getEventMap();

    @Override
    BattleState getBattleState();

    @Override
    BattleParticipantInventory getInventory();

    BattleParticipantStatContainer getStatContainer();

    @Override
    BattleParticipantEffectContainer getEffectContainer();

    @Override
    BattleParticipantHealthContainer getHealthContainer();

    boolean setTeam(BattleParticipantTeam team, Tracer<ActionTrace> tracer);
}
