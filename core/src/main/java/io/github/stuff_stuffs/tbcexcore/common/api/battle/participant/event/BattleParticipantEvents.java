package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.event;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.event.damage.BattleParticipantPostDamageEvent;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.event.damage.BattleParticipantPreDamageEvent;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexutil.common.event.EventKey;
import io.github.stuff_stuffs.tbcexutil.common.event.SimpleEvent;

public final class BattleParticipantEvents {
    public static final EventKey<BattleParticipantPreDamageEvent, BattleParticipantPreDamageEvent.Mut> BATTLE_PARTICIPANT_PRE_DAMAGE_EVENT = EventKey.get(BattleParticipantPreDamageEvent.class, BattleParticipantPreDamageEvent.Mut.class);
    public static final EventKey<BattleParticipantPostDamageEvent, BattleParticipantPostDamageEvent.Mut> BATTLE_PARTICIPANT_POST_DAMAGE_EVENT = EventKey.get(BattleParticipantPostDamageEvent.class, BattleParticipantPostDamageEvent.Mut.class);

    public static void init() {
        BattleParticipantStateView.BATTLE_PARTICIPANT_EVENT_INIT.register(initializer -> {
            initializer.add(BATTLE_PARTICIPANT_PRE_DAMAGE_EVENT, (enter, exit) -> new SimpleEvent<>(BattleParticipantPreDamageEvent::convert, BattleParticipantPreDamageEvent.invokerFactory(), BattleParticipantPreDamageEvent.Mut.class, enter, exit));
            initializer.add(BATTLE_PARTICIPANT_POST_DAMAGE_EVENT, (enter, exit) -> new SimpleEvent<>(BattleParticipantPostDamageEvent::convert, BattleParticipantPostDamageEvent.invokerFactory(), BattleParticipantPostDamageEvent.Mut.class, enter, exit));
        });
    }

    private BattleParticipantEvents() {
    }
}
