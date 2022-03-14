package io.github.stuff_stuffs.tbcexutil.common.event;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public final class SimpleEvent<V, M> implements AbstractEvent<V, M> {
    private final Map<EventListenerKey, M> eventsByKey = new Reference2ReferenceOpenHashMap<>();
    private final Function<V, M> viewConverter;
    private final InvokerFactory<M> invokerFactory;
    private final Runnable enter;
    private final Runnable exit;
    private M invoker;
    private M[] muts;

    public SimpleEvent(final Function<V, M> viewConverter, final InvokerFactory<M> invokerFactory, final Class<M> clazz, final Runnable enter, final Runnable exit) {
        this.viewConverter = viewConverter;
        this.invokerFactory = invokerFactory;
        muts = (M[]) Array.newInstance(clazz, 0);
        this.enter = enter;
        this.exit = exit;
        invoker = invokerFactory.createInvoker(muts, enter, exit);
    }

    @Override
    public EventListenerKey registerListener(final V v) {
        final EventListenerKey key = new EventListenerKey(this);
        final M converted = viewConverter.apply(v);
        eventsByKey.put(key, converted);
        muts = Arrays.copyOf(muts, muts.length + 1);
        muts[muts.length - 1] = converted;
        invoker = invokerFactory.createInvoker(muts, enter, exit);
        return key;
    }

    @Override
    public EventListenerKey registerMutListener(final M m) {
        final EventListenerKey key = new EventListenerKey(this);
        eventsByKey.put(key, m);
        muts = Arrays.copyOf(muts, muts.length + 1);
        muts[muts.length - 1] = m;
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
        muts = eventsByKey.values().toArray(l -> (M[]) Array.newInstance(muts.getClass().getComponentType(), l));
        invoker = invokerFactory.createInvoker(muts, enter, exit);
    }
}
