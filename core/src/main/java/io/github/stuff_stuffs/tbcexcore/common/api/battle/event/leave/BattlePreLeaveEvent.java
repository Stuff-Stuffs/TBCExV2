package io.github.stuff_stuffs.tbcexcore.common.api.battle.event.leave;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexutil.common.event.InvokerFactory;

public interface BattlePreLeaveEvent {
    void beforeLeave(BattleParticipantStateView state);

    static Mut convert(final BattlePreLeaveEvent event) {
        return state -> {
            event.beforeLeave(state);
            return true;
        };
    }

    static InvokerFactory<Mut> invokerFactory() {
        return (listeners, enter, exit) -> state -> {
            boolean s = true;
            enter.run();
            for (final Mut listener : listeners) {
                s &= listener.beforeLeave(state);
            }
            exit.run();
            return s;
        };
    }

    interface Mut {
        boolean beforeLeave(BattleParticipantState state);
    }
}
