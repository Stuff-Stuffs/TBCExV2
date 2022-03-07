package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.action.BattleParticipantAction;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;

import java.util.List;

public interface BattleParticipantEffect {
    BattleParticipantEffectType<?, ?> getType();

    void init(BattleParticipantState state, Tracer<ActionTrace> tracer);

    void deinit(Tracer<ActionTrace> tracer);

    default List<BattleParticipantAction> getActions() {
        return List.of();
    }
}
