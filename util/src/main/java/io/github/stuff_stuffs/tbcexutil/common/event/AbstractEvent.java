package io.github.stuff_stuffs.tbcexutil.common.event;

public non-sealed interface AbstractEvent<View, Mut> extends MutEvent<View, Mut> {
    void remove(EventListenerKey key);
}
