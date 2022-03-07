package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.event.damage;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.damage.BattleParticipantDamagePacket;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;
import io.github.stuff_stuffs.tbcexutil.common.event.InvokerFactory;

public interface BattleParticipantPostDamageEvent {
    void postDamage(BattleParticipantStateView state, BattleParticipantDamagePacket damage, Tracer<ActionTrace> tracer);

    static Mut convert(final BattleParticipantPostDamageEvent event) {
        return event::postDamage;
    }

    static InvokerFactory<Mut> invokerFactory() {
        return (listeners, enter, exit) -> (state, damage, tracer) -> {
            enter.run();
            for (final Mut listener : listeners) {
                listener.postDamage(state, damage, tracer);
            }
            exit.run();
        };
    }

    interface Mut {
        void postDamage(BattleParticipantState state, BattleParticipantDamagePacket damage, Tracer<ActionTrace> tracer);
    }
}
