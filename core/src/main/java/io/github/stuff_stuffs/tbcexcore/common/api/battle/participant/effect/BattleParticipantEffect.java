package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;

public interface BattleParticipantEffect {
    BattleParticipantEffectType<?> getType();

    void init(BattleParticipantState state);

    void deinit();
}
