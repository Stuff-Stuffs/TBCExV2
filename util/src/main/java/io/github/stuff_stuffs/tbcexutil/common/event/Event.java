package io.github.stuff_stuffs.tbcexutil.common.event;

public sealed interface Event<View, Mut> permits MutEvent {
    EventListenerKey registerListener(View view);
}
