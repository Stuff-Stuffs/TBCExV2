package io.github.stuff_stuffs.tbcexutil.common.event;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class EventKey<View, Mut> {
    private static final Map<Class<?>, Map<Class<?>, EventKey<?, ?>>> CACHE = new Reference2ReferenceOpenHashMap<>();
    private final Class<View> viewClass;
    private final Class<Mut> mutClass;

    private EventKey(final Class<View> viewClass, final Class<Mut> mutClass) {
        this.viewClass = viewClass;
        this.mutClass = mutClass;
    }

    public static <V, M> EventKey<V, M> get(final Class<V> viewClass, final Class<M> mutClass) {
        final EventKey<?, ?> eventKey = CACHE.computeIfAbsent(viewClass, v -> new Reference2ReferenceOpenHashMap<>()).computeIfAbsent(mutClass, m -> new EventKey<>(viewClass, m));
        final EventKey<V, M> casted = eventKey.checkAndCast(viewClass, mutClass);
        if (casted == null) {
            throw new InternalError();
        }
        return casted;
    }

    public <V, M> @Nullable EventKey<V, M> checkAndCast(final Class<V> vClass, final Class<M> mClass) {
        if (vClass == viewClass && mClass == mutClass) {
            return (EventKey<V, M>) this;
        }
        return null;
    }

    @Override
    public String toString() {
        return "EventKey{" +
                "viewClass=" + viewClass +
                ", mutClass=" + mutClass +
                '}';
    }
}
