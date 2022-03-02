package io.github.stuff_stuffs.tbcexcore.common.api.battle.state;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateHandle;
import io.github.stuff_stuffs.tbcexutil.common.event.map.EventMap;

public interface BattleStateView {
    EventMap getEventMap();

    BattleParticipantStateView getParticipant(BattleParticipantStateHandle handle);
}
