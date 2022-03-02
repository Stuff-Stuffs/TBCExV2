package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleState;
import io.github.stuff_stuffs.tbcexutil.common.event.map.MutEventMap;

public interface BattleParticipantState extends BattleParticipantStateView {
    @Override
    MutEventMap getEventMap();

    @Override
    BattleState getBattleState();
}
