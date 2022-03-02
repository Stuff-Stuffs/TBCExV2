package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleStateView;
import io.github.stuff_stuffs.tbcexutil.common.event.map.EventMap;

public interface BattleParticipantStateView {
    BattleParticipantStateHandle getHandle();

    EventMap getEventMap();

    BattleStateView getBattleState();
}
