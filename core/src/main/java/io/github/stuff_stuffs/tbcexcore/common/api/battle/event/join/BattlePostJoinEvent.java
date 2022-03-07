package io.github.stuff_stuffs.tbcexcore.common.api.battle.event.join;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;
import io.github.stuff_stuffs.tbcexutil.common.event.InvokerFactory;

public interface BattlePostJoinEvent {
    void afterJoin(BattleParticipantStateView state, Tracer<ActionTrace> tracer);

    static Mut convert(final BattlePostJoinEvent event) {
        return event::afterJoin;
    }

    static InvokerFactory<Mut> invokerFactory() {
        return (listeners, enter, exit) -> (state, tracer) -> {
            enter.run();
            for (final Mut listener : listeners) {
                listener.afterJoin(state, tracer);
            }
            exit.run();
        };
    }

    interface Mut {
        void afterJoin(BattleParticipantState state, Tracer<ActionTrace> tracer);
    }
}
