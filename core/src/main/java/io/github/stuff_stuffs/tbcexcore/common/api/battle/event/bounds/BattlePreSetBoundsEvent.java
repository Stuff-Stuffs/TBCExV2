package io.github.stuff_stuffs.tbcexcore.common.api.battle.event.bounds;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleState;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleStateView;
import io.github.stuff_stuffs.tbcexutil.common.BattleBounds;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;
import io.github.stuff_stuffs.tbcexutil.common.event.InvokerFactory;

public interface BattlePreSetBoundsEvent {
    void onSetBounds(BattleStateView state, BattleBounds newBounds, Tracer<ActionTrace> tracer);

    static Mut convert(final BattlePreSetBoundsEvent event) {
        return (state, newBounds, tracer) -> {
            event.onSetBounds(state, newBounds, tracer);
            return true;
        };
    }

    static InvokerFactory<Mut> invokerFactory() {
        return (listeners, enter, exit) -> (state, newBounds, tracer) -> {
            boolean b = true;
            enter.run();
            for (final Mut listener : listeners) {
                b &= listener.onSetBounds(state, newBounds, tracer);
            }
            exit.run();
            return b;
        };
    }

    interface Mut {
        boolean onSetBounds(BattleState state, BattleBounds newBounds, Tracer<ActionTrace> tracer);
    }
}
