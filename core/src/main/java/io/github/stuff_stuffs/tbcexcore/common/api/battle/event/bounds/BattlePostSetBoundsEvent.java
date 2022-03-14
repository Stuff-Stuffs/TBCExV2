package io.github.stuff_stuffs.tbcexcore.common.api.battle.event.bounds;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleState;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleStateView;
import io.github.stuff_stuffs.tbcexutil.common.BattleBounds;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;
import io.github.stuff_stuffs.tbcexutil.common.event.InvokerFactory;

public interface BattlePostSetBoundsEvent {
    void onSetBounds(BattleStateView state, BattleBounds oldBounds, Tracer<ActionTrace> tracer);

    static Mut convert(final BattlePostSetBoundsEvent event) {
        return event::onSetBounds;
    }

    static InvokerFactory<Mut> invokerFactory() {
        return (listeners, enter, exit) -> (state, oldBounds, tracer) -> {
            enter.run();
            for (final Mut listener : listeners) {
                listener.onSetBounds(state, oldBounds, tracer);
            }
            exit.run();
        };
    }

    interface Mut {
        void onSetBounds(BattleState state, BattleBounds oldBounds, Tracer<ActionTrace> tracer);
    }
}
