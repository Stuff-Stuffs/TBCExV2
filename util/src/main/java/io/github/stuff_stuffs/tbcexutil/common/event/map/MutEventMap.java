package io.github.stuff_stuffs.tbcexutil.common.event.map;

import io.github.stuff_stuffs.tbcexutil.common.event.EventKey;
import io.github.stuff_stuffs.tbcexutil.common.event.MutEvent;

public sealed interface MutEventMap extends EventMap permits EventMapImpl {
    <V, M> MutEvent<V, M> getEventMut(EventKey<V, M> key);
}
