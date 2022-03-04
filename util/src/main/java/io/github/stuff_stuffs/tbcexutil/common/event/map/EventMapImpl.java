package io.github.stuff_stuffs.tbcexutil.common.event.map;

import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import io.github.stuff_stuffs.tbcexutil.common.event.AbstractEvent;
import io.github.stuff_stuffs.tbcexutil.common.event.Event;
import io.github.stuff_stuffs.tbcexutil.common.event.EventKey;
import io.github.stuff_stuffs.tbcexutil.common.event.MutEvent;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;

import java.util.function.BiFunction;

public final class EventMapImpl implements MutEventMap {
    private static final int MAX_DEPTH = 512;
    private final Reference2ReferenceMap<EventKey<?, ?>, AbstractEvent<?, ?>> map = new Reference2ReferenceOpenHashMap<>();
    private int depth = 0;

    public <V, M> void register(final EventKey<V, M> key, final BiFunction<Runnable, Runnable, ? extends AbstractEvent<V, M>> event) {
        if (map.put(key, event.apply(this::enter, this::exit)) != null) {
            throw new RuntimeException("Duplicate event entry with key: " + key);
        }
    }

    private void enter() {
        depth++;
        if (depth > MAX_DEPTH) {
            throw new TBCExException("Probably entered infinite loop in event in battle");
        }
    }

    private void exit() {
        depth--;
        if (depth < 0) {
            throw new TBCExException("Unbalanced enter and exit in event");
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
