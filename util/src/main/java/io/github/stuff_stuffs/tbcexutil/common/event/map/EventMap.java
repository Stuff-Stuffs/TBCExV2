package io.github.stuff_stuffs.tbcexutil.common.event.map;

import io.github.stuff_stuffs.tbcexutil.common.event.Event;
import io.github.stuff_stuffs.tbcexutil.common.event.EventKey;

public sealed interface EventMap permits MutEventMap {
    <V> Event<V, ?> getEvent(EventKey<V, ?> key);
}
