package io.github.stuff_stuffs.tbcexcore.common.api.battle.event.leave;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;
import io.github.stuff_stuffs.tbcexutil.common.event.InvokerFactory;

public interface BattlePostLeaveEvent {
    void afterLeave(BattleParticipantStateView state, Tracer<ActionTrace> tracer);

    static Mut convert(final BattlePostLeaveEvent event) {
        return event::afterLeave;
    }

    static InvokerFactory<Mut> invokerFactory() {
        return (listeners, enter, exit) -> (state, tracer) -> {
            enter.run();
            for (final Mut listener : listeners) {
                listener.afterLeave(state, tracer);
            }
            exit.run();
        };
    }

    interface Mut {
        void afterLeave(BattleParticipantState state, Tracer<ActionTrace> tracer);
    }
}
