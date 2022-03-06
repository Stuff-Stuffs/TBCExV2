package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.event.damage;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.damage.BattleParticipantDamagePacket;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexutil.common.event.InvokerFactory;

public interface BattleParticipantPreDamageEvent {
    void beforeDamage(BattleParticipantStateView state, BattleParticipantDamagePacket damage);

    static Mut convert(final BattleParticipantPreDamageEvent event) {
        return (state, damage) -> {
            event.beforeDamage(state, damage);
            return damage;
        };
    }

    static InvokerFactory<Mut> invokerFactory() {
        return (listeners, enter, exit) -> (state, damage) -> {
            enter.run();
            for (final Mut listener : listeners) {
                damage = listener.beforeDamage(state, damage);
            }
            exit.run();
            return damage;
        };
    }

    interface Mut {
        BattleParticipantDamagePacket beforeDamage(BattleParticipantState state, BattleParticipantDamagePacket damage);
    }
}
