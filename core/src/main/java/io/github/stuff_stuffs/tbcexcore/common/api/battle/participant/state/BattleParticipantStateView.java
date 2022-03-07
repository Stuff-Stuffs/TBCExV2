package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipantTeam;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffectContainerView;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.health.BattleParticipantHealthContainerView;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantInventoryView;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStat;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleStateView;
import io.github.stuff_stuffs.tbcexutil.common.event.AbstractEvent;
import io.github.stuff_stuffs.tbcexutil.common.event.EventKey;
import io.github.stuff_stuffs.tbcexutil.common.event.map.EventMap;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.function.BiFunction;

public interface BattleParticipantStateView {
    Event<EventRegisterer> BATTLE_PARTICIPANT_EVENT_INIT = EventFactory.createArrayBacked(EventRegisterer.class, eventRegisterers -> initializer -> {
        for (final EventRegisterer registerer : eventRegisterers) {
            registerer.register(initializer);
        }
    });

    BattleParticipantHandle getHandle();

    EventMap getEventMap();

    BattleStateView getBattleState();

    BattleParticipantInventoryView getInventory();

    BattleParticipantEffectContainerView getEffectContainer();

    double getStat(BattleParticipantStat stat);

    BattleParticipantHealthContainerView getHealthContainer();

    BattleParticipantTeam getTeam();

    interface EventRegisterer {
        void register(BattleStateView.EventInitializer initializer);
    }

    interface EventInitializer {
        <V, M> void add(EventKey<V, M> key, BiFunction<Runnable, Runnable, ? extends AbstractEvent<V, M>> initializer);
    }
}
