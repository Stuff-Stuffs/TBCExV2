package io.github.stuff_stuffs.tbcexcore.common.api.battle.event;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.event.join.BattlePostJoinEvent;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.event.join.BattlePreJoinEvent;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.event.leave.BattlePostLeaveEvent;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.event.leave.BattlePreLeaveEvent;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleState;
import io.github.stuff_stuffs.tbcexutil.common.event.EventKey;
import io.github.stuff_stuffs.tbcexutil.common.event.SimpleEvent;

public final class BattleEvents {
    public static final EventKey<BattlePreJoinEvent, BattlePreJoinEvent.Mut> BATTLE_PRE_JOIN_EVENT = EventKey.get(BattlePreJoinEvent.class, BattlePreJoinEvent.Mut.class);
    public static final EventKey<BattlePostJoinEvent, BattlePostJoinEvent.Mut> BATTLE_POST_JOIN_EVENT = EventKey.get(BattlePostJoinEvent.class, BattlePostJoinEvent.Mut.class);
    public static final EventKey<BattlePreLeaveEvent, BattlePreLeaveEvent.Mut> BATTLE_PRE_LEAVE_EVENT = EventKey.get(BattlePreLeaveEvent.class, BattlePreLeaveEvent.Mut.class);
    public static final EventKey<BattlePostLeaveEvent, BattlePostLeaveEvent.Mut> BATTLE_POST_LEAVE_EVENT = EventKey.get(BattlePostLeaveEvent.class, BattlePostLeaveEvent.Mut.class);

    public static void init() {
        BattleState.BATTLE_EVENT_INIT.register(initializer -> {
            initializer.add(BATTLE_PRE_JOIN_EVENT, (enter, exit) -> new SimpleEvent<>(BattlePreJoinEvent::convert, BattlePreJoinEvent.invokerFactory(), BattlePreJoinEvent.Mut.class, enter, exit));
            initializer.add(BATTLE_POST_JOIN_EVENT, (enter, exit) -> new SimpleEvent<>(BattlePostJoinEvent::convert, BattlePostJoinEvent.invokerFactory(), BattlePostJoinEvent.Mut.class, enter, enter));
            initializer.add(BATTLE_PRE_LEAVE_EVENT, (enter, exit) -> new SimpleEvent<>(BattlePreLeaveEvent::convert, BattlePreLeaveEvent.invokerFactory(), BattlePreLeaveEvent.Mut.class, enter, enter));
            initializer.add(BATTLE_POST_LEAVE_EVENT, (enter, exit) -> new SimpleEvent<>(BattlePostLeaveEvent::convert, BattlePostLeaveEvent.invokerFactory(), BattlePostLeaveEvent.Mut.class, enter, enter));
        });
    }

    private BattleEvents() {
    }
}
