package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.event.move;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;
import io.github.stuff_stuffs.tbcexutil.common.event.InvokerFactory;
import net.minecraft.util.math.BlockPos;

public interface BattleParticipantPostSetPosEvent {
    void onSetPos(BattleParticipantStateView state, BlockPos oldPos, Tracer<ActionTrace> tracer);

    static Mut convert(final BattleParticipantPostSetPosEvent event) {
        return event::onSetPos;
    }

    static InvokerFactory<Mut> invoker() {
        return (listeners, enter, exit) -> (state, oldPos, tracer) -> {
            enter.run();
            for (final Mut listener : listeners) {
                listener.onSetPos(state, oldPos, tracer);
            }
            exit.run();
        };
    }

    interface Mut {
        void onSetPos(BattleParticipantState state, BlockPos oldPos, Tracer<ActionTrace> tracer);
    }
}
