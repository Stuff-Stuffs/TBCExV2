package io.github.stuff_stuffs.tbcexcore.common.api.battle.state;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
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
    Event<EventInitializer> BATTLE_EVENT_INIT = EventFactory.createArrayBacked(EventInitializer.class, eventInitializers -> new EventInitializer() {
        @Override
        public <V, M> void initializeEvents(final BiConsumer<EventKey<V, M>, BiFunction<Runnable, Runnable, ? extends AbstractEvent<V, M>>> consumer) {
            for (final EventInitializer initializer : eventInitializers) {
                initializer.initializeEvents(consumer);
            }
        }
    });

    EventMap getEventMap();

    BattleParticipantStateView getParticipant(BattleParticipantHandle handle);

    BattleHandle getHandle();

    interface EventInitializer {
        <V, M> void initializeEvents(BiConsumer<EventKey<V, M>, BiFunction<Runnable, Runnable, ? extends AbstractEvent<V, M>>> consumer);
    }
}
