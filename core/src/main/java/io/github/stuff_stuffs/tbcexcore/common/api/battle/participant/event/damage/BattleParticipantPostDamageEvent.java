package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.event.damage;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.damage.BattleParticipantDamagePacket;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexutil.common.event.InvokerFactory;

public interface BattleParticipantPostDamageEvent {
    void postDamage(BattleParticipantStateView state, BattleParticipantDamagePacket damage);

    static Mut convert(final BattleParticipantPostDamageEvent event) {
        return event::postDamage;
    }

    static InvokerFactory<Mut> invokerFactory() {
        return (listeners, enter, exit) -> (state, damage) -> {
            enter.run();
            for (final Mut listener : listeners) {
                listener.postDamage(state, damage);
            }
            exit.run();
        };
    }

    interface Mut {
        void postDamage(BattleParticipantState state, BattleParticipantDamagePacket damage);
    }
}
