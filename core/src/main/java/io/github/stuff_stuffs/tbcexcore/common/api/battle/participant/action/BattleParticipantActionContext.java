package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.action;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleStateView;

public interface BattleParticipantActionContext {
    BattleStateView getState();

    BattleParticipantHandle getUser();
}
