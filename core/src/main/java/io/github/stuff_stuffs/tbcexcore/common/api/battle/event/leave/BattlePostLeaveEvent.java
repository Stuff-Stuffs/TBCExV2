package io.github.stuff_stuffs.tbcexcore.common.api.battle.event.leave;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexutil.common.event.InvokerFactory;

public interface BattlePostLeaveEvent {
    void afterLeave(BattleParticipantStateView state);

    static Mut convert(final BattlePostLeaveEvent event) {
        return event::afterLeave;
    }

    static InvokerFactory<Mut> invokerFactory() {
        return (listeners, enter, exit) -> state -> {
            enter.run();
            for (final Mut listener : listeners) {
                listener.afterLeave(state);
            }
            exit.run();
        };
    }

    interface Mut {
        void afterLeave(BattleParticipantState state);
    }
}
