package io.github.stuff_stuffs.tbcexutil.common.event;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public final class SimpleEvent<V, M> implements AbstractEvent<V, M> {
    private final Map<EventListenerKey, M> eventsByKey = new Reference2ReferenceOpenHashMap<>();
    private final Function<V, M> viewConverter;
    private final Function<M[], M> invokerFactory;
    private final Class<M> clazz;
    private M invoker;
    private M[] muts;

    public SimpleEvent(final Function<V, M> viewConverter, final Function<M[], M> invokerFactory, final Class<M> clazz) {
        this.viewConverter = viewConverter;
        this.invokerFactory = invokerFactory;
        this.clazz = clazz;
        muts = (M[]) Array.newInstance(clazz, 0);
        invoker = invokerFactory.apply(muts);
    }

    @Override
    public EventListenerKey registerListener(final V v) {
        final EventListenerKey key = new EventListenerKey(this);
        final M converted = viewConverter.apply(v);
        eventsByKey.put(key, converted);
        muts = Arrays.copyOf(muts, muts.length + 1);
        muts[muts.length - 1] = converted;
        invoker = invokerFactory.apply(muts);
        return key;
    }

    @Override
    public EventListenerKey registerMutListener(final M m) {
        final EventListenerKey key = new EventListenerKey(this);
        eventsByKey.put(key, m);
        muts = Arrays.copyOf(muts, muts.length + 1);
        muts[muts.length - 1] = m;
        invoker = invokerFactory.apply(muts);
        return key;
    }

    @Override
    public M getInvoker() {
        return invoker;
    }

    @Override
    public void remove(final EventListenerKey key) {
        eventsByKey.remove(key);
        muts = eventsByKey.values().toArray(l -> (M[]) Array.newInstance(clazz, l));
        invoker = invokerFactory.apply(muts);
    }
}
