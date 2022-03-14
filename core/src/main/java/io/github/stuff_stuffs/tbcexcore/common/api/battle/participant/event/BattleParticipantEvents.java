package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.event;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.event.damage.BattleParticipantPostDamageEvent;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.event.damage.BattleParticipantPreDamageEvent;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.event.move.BattleParticipantPostSetPosEvent;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.event.move.BattleParticipantPreSetPosEvent;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.event.team.BattleParticipantPostChangeTeamEvent;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.event.team.BattleParticipantPreChangeTeamEvent;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexutil.common.event.EventKey;
import io.github.stuff_stuffs.tbcexutil.common.event.SimpleEvent;

public final class BattleParticipantEvents {
    public static final EventKey<BattleParticipantPreDamageEvent, BattleParticipantPreDamageEvent.Mut> BATTLE_PARTICIPANT_PRE_DAMAGE_EVENT = EventKey.get(BattleParticipantPreDamageEvent.class, BattleParticipantPreDamageEvent.Mut.class);
    public static final EventKey<BattleParticipantPostDamageEvent, BattleParticipantPostDamageEvent.Mut> BATTLE_PARTICIPANT_POST_DAMAGE_EVENT = EventKey.get(BattleParticipantPostDamageEvent.class, BattleParticipantPostDamageEvent.Mut.class);

    public static final EventKey<BattleParticipantPreChangeTeamEvent, BattleParticipantPreChangeTeamEvent.Mut> BATTLE_PARTICIPANT_PRE_CHANGE_TEAM_EVENT = EventKey.get(BattleParticipantPreChangeTeamEvent.class, BattleParticipantPreChangeTeamEvent.Mut.class);
    public static final EventKey<BattleParticipantPostChangeTeamEvent, BattleParticipantPostChangeTeamEvent.Mut> BATTLE_PARTICIPANT_POST_CHANGE_TEAM_EVENT = EventKey.get(BattleParticipantPostChangeTeamEvent.class, BattleParticipantPostChangeTeamEvent.Mut.class);

    public static final EventKey<BattleParticipantPreSetPosEvent, BattleParticipantPreSetPosEvent.Mut> BATTLE_PARTICIPANT_PRE_SET_POS_EVENT = EventKey.get(BattleParticipantPreSetPosEvent.class, BattleParticipantPreSetPosEvent.Mut.class);
    public static final EventKey<BattleParticipantPostSetPosEvent, BattleParticipantPostSetPosEvent.Mut> BATTLE_PARTICIPANT_POST_SET_POS_EVENT = EventKey.get(BattleParticipantPostSetPosEvent.class, BattleParticipantPostSetPosEvent.Mut.class);

    public static void init() {
        BattleParticipantStateView.BATTLE_PARTICIPANT_EVENT_INIT.register(initializer -> {
            initializer.add(BATTLE_PARTICIPANT_PRE_DAMAGE_EVENT, (enter, exit) -> new SimpleEvent<>(BattleParticipantPreDamageEvent::convert, BattleParticipantPreDamageEvent.invokerFactory(), BattleParticipantPreDamageEvent.Mut.class, enter, exit));
            initializer.add(BATTLE_PARTICIPANT_POST_DAMAGE_EVENT, (enter, exit) -> new SimpleEvent<>(BattleParticipantPostDamageEvent::convert, BattleParticipantPostDamageEvent.invokerFactory(), BattleParticipantPostDamageEvent.Mut.class, enter, exit));

            initializer.add(BATTLE_PARTICIPANT_PRE_CHANGE_TEAM_EVENT, (enter, exit) -> new SimpleEvent<>(BattleParticipantPreChangeTeamEvent::convert, BattleParticipantPreChangeTeamEvent.invokerFactory(), BattleParticipantPreChangeTeamEvent.Mut.class, enter, exit));
            initializer.add(BATTLE_PARTICIPANT_POST_CHANGE_TEAM_EVENT, (enter, exit) -> new SimpleEvent<>(BattleParticipantPostChangeTeamEvent::convert, BattleParticipantPostChangeTeamEvent.invokerFactory(), BattleParticipantPostChangeTeamEvent.Mut.class, enter, exit));

            initializer.add(BATTLE_PARTICIPANT_PRE_SET_POS_EVENT, (enter, exit) -> new SimpleEvent<>(BattleParticipantPreSetPosEvent::convert, BattleParticipantPreSetPosEvent.invoker(), BattleParticipantPreSetPosEvent.Mut.class, enter, enter));
            initializer.add(BATTLE_PARTICIPANT_POST_SET_POS_EVENT, (enter, exit) -> new SimpleEvent<>(BattleParticipantPostSetPosEvent::convert, BattleParticipantPostSetPosEvent.invoker(), BattleParticipantPostSetPosEvent.Mut.class, enter, exit));
        });
    }

    private BattleParticipantEvents() {
    }
}
