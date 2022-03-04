package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffect;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffectType;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStat;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleStateView;
import io.github.stuff_stuffs.tbcexutil.common.event.AbstractEvent;
import io.github.stuff_stuffs.tbcexutil.common.event.EventKey;
import io.github.stuff_stuffs.tbcexutil.common.event.map.EventMap;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public interface BattleParticipantStateView {
    Event<EventInitializer> PARTICIPANT_EVENT_INIT = EventFactory.createArrayBacked(EventInitializer.class, eventInitializers -> new EventInitializer() {
        @Override
        public <V, M> void initializeEvents(final BiConsumer<EventKey<V, M>, BiFunction<Runnable, Runnable, ? extends AbstractEvent<V, M>>> consumer) {
            for (final EventInitializer initializer : eventInitializers) {
                initializer.initializeEvents(consumer);
            }
        }
    });

    BattleParticipantHandle getHandle();

    EventMap getEventMap();

    BattleStateView getBattleState();

    double getStat(BattleParticipantStat stat);

    <T extends BattleParticipantEffect> @Nullable T getEffect(BattleParticipantEffectType<T> type);

    interface EventInitializer {
        <V, M> void initializeEvents(BiConsumer<EventKey<V, M>, BiFunction<Runnable, Runnable, ? extends AbstractEvent<V, M>>> consumer);
    }
}
