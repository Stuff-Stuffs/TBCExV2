package io.github.stuff_stuffs.tbcexcore.common.api.battle.event.join;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;
import io.github.stuff_stuffs.tbcexutil.common.event.InvokerFactory;

public interface BattlePreJoinEvent {
    void beforeJoin(BattleParticipantStateView state, Tracer<ActionTrace> tracer);

    static Mut convert(final BattlePreJoinEvent event) {
        return (state, tracer) -> {
            event.beforeJoin(state, tracer);
            return true;
        };
    }

    static InvokerFactory<Mut> invokerFactory() {
        return (listeners, enter, exit) -> (state, tracer) -> {
            boolean s = true;
            enter.run();
            for (final Mut mut : listeners) {
                s &= mut.beforeJoin(state, tracer);
            }
            exit.run();
            return s;
        };
    }

    interface Mut {
        boolean beforeJoin(BattleParticipantState state, Tracer<ActionTrace> tracer);
    }
}
