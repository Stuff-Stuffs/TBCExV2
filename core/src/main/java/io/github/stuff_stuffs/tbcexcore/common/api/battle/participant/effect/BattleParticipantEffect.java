package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.action.BattleParticipantAction;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;

import java.util.List;

public interface BattleParticipantEffect {
    BattleParticipantEffectType<?, ?> getType();

    void init(BattleParticipantState state);

    void deinit();

    default List<BattleParticipantAction> getActions() {
        return List.of();
    }
}
