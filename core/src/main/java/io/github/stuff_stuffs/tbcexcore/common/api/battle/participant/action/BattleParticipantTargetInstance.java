package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.action;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.action.target.TargetType;

public interface BattleParticipantTargetInstance {
    TargetType<?> getType();

    double getDistance();
}
