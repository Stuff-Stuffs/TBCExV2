package io.github.stuff_stuffs.tbcexutil.common.event;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;

public final class SortedEvent<V, M, K extends Comparable<K>> implements AbstractEvent<V, M> {
    private final Map<EventListenerKey, M> eventsByKey = new Reference2ReferenceOpenHashMap<>();
    private final Function<V, M> viewConverter;
    private final InvokerFactory<M> invokerFactory;
    private final Comparator<M> comparator;
    private final Class<M> clazz;
    private final Runnable enter;
    private final Runnable exit;
    private M invoker;
    private M[] muts;

    public SortedEvent(final Function<V, M> viewConverter, final InvokerFactory<M> invokerFactory, final Function<M, K> comparableExtractor, final Class<M> clazz, final Runnable enter, final Runnable exit) {
        this.viewConverter = viewConverter;
        this.invokerFactory = invokerFactory;
        this.clazz = clazz;
        muts = (M[]) Array.newInstance(clazz, 0);
        this.enter = enter;
        this.exit = exit;
        invoker = invokerFactory.createInvoker(muts, enter, exit);
        comparator = Comparator.comparing(comparableExtractor);
    }

    @Override
    public EventListenerKey registerListener(final V v) {
        final EventListenerKey key = new EventListenerKey(this);
        final M converted = viewConverter.apply(v);
        eventsByKey.put(key, converted);
        muts = Arrays.copyOf(muts, muts.length + 1);
        muts[muts.length - 1] = converted;
        Arrays.sort(muts, comparator);
        invoker = invokerFactory.createInvoker(muts, enter, exit);
        return key;
    }

    @Override
    public EventListenerKey registerMutListener(final M m) {
        final EventListenerKey key = new EventListenerKey(this);
        eventsByKey.put(key, m);
        muts = Arrays.copyOf(muts, muts.length + 1);
        muts[muts.length - 1] = m;
        Arrays.sort(muts, comparator);
        invoker = invokerFactory.createInvoker(muts, enter, exit);
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
        Arrays.sort(muts, comparator);
        invoker = invokerFactory.createInvoker(muts, enter, exit);
    }
}
