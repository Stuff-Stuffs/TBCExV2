package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.event.damage;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.damage.BattleParticipantDamagePacket;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;
import io.github.stuff_stuffs.tbcexutil.common.event.InvokerFactory;

public interface BattleParticipantPreDamageEvent {
    void beforeDamage(BattleParticipantStateView state, BattleParticipantDamagePacket damage, Tracer<ActionTrace> tracer);

    static Mut convert(final BattleParticipantPreDamageEvent event) {
        return (state, damage, tracer) -> {
            event.beforeDamage(state, damage, tracer);
            return damage;
        };
    }

    static InvokerFactory<Mut> invokerFactory() {
        return (listeners, enter, exit) -> (state, damage, tracer) -> {
            enter.run();
            for (final Mut listener : listeners) {
                damage = listener.beforeDamage(state, damage, tracer);
            }
            exit.run();
            return damage;
        };
    }

    interface Mut {
        BattleParticipantDamagePacket beforeDamage(BattleParticipantState state, BattleParticipantDamagePacket damage, Tracer<ActionTrace> tracer);
    }
}
