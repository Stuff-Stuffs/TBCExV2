package io.github.stuff_stuffs.tbcexcore.common.api.battle.state;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.effect.BattleEffectContainerView;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexutil.common.event.AbstractEvent;
import io.github.stuff_stuffs.tbcexutil.common.event.EventKey;
import io.github.stuff_stuffs.tbcexutil.common.event.map.EventMap;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public interface BattleStateView {
    Event<EventRegisterer> BATTLE_EVENT_INIT = EventFactory.createArrayBacked(EventRegisterer.class, eventRegisterers -> initializer -> {
        for (final EventRegisterer registerer : eventRegisterers) {
            registerer.register(initializer);
        }
    });

    EventMap getEventMap();

    BattleParticipantStateView getParticipant(BattleParticipantHandle handle);

    BattleHandle getHandle();

    BattleEffectContainerView getEffects();

    interface EventRegisterer {
        void register(EventInitializer initializer);
    }

    interface EventInitializer {
        <V, M> void add(EventKey<V, M> key, BiFunction<Runnable, Runnable, ? extends AbstractEvent<V, M>> initializer);
    }
}
