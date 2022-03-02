package io.github.stuff_stuffs.tbcexutil.common.event.map;

import io.github.stuff_stuffs.tbcexutil.common.event.*;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;

import java.util.function.Function;

public final class EventMapImpl implements MutEventMap {
    private final Reference2ReferenceMap<EventKey<?, ?>, AbstractEvent<?, ?>> map = new Reference2ReferenceOpenHashMap<>();

    public <V, M> void registerSimple(final EventKey<V, M> key, final Function<V, M> viewConverter, final Function<M[], M> invokerFactory, final Class<M> clazz) {
        register(key, new SimpleEvent<>(viewConverter, invokerFactory, clazz));
    }

    public <V, M, K extends Comparable<K>> void registerSorted(final EventKey<V, M> key, final Function<V, M> viewConverter, final Function<M[], M> invokerFactory, final Function<M, K> comparableExtractor, final Class<M> clazz) {
        register(key, new SortedEvent<>(viewConverter, invokerFactory, comparableExtractor, clazz));
    }

    public <V, M> void register(final EventKey<V, M> key, final AbstractEvent<V, M> event) {
        if (map.put(key, event) != null) {
            throw new RuntimeException("Duplicate event entry with key: " + key);
        }
    }

    @Override
    public <V> Event<V, ?> getEvent(final EventKey<V, ?> key) {
        final AbstractEvent<?, ?> abstractEvent = map.get(key);
        if (abstractEvent == null) {
            throw new RuntimeException("Missing event: " + key);
        }
        return (Event<V, ?>) abstractEvent;
    }

    @Override
    public <V, M> MutEvent<V, M> getEventMut(final EventKey<V, M> key) {
        final AbstractEvent<?, ?> abstractEvent = map.get(key);
        if (abstractEvent == null) {
            throw new RuntimeException("Missing event: " + key);
        }
        return (MutEvent<V, M>) abstractEvent;
    }
}
