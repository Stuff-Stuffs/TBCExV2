package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.event.move;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;
import io.github.stuff_stuffs.tbcexutil.common.event.InvokerFactory;
import net.minecraft.util.math.BlockPos;

public interface BattleParticipantPreSetPosEvent {
    void onSetPos(BattleParticipantStateView state, BlockPos newPos, Tracer<ActionTrace> tracer);

    static Mut convert(final BattleParticipantPreSetPosEvent event) {
        return (state, newPos, tracer) -> {
            event.onSetPos(state, newPos, tracer);
            return true;
        };
    }

    static InvokerFactory<Mut> invoker() {
        return (listeners, enter, exit) -> (state, newPos, tracer) -> {
            boolean b = true;
            enter.run();
            for (final Mut listener : listeners) {
                b &= listener.onSetPos(state, newPos, tracer);
            }
            exit.run();
            return b;
        };
    }

    interface Mut {
        boolean onSetPos(BattleParticipantState state, BlockPos newPos, Tracer<ActionTrace> tracer);
    }
}
