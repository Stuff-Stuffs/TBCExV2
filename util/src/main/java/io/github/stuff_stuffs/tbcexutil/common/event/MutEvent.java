package io.github.stuff_stuffs.tbcexutil.common.event;

public sealed interface MutEvent<View, Mut> extends Event<View, Mut> permits AbstractEvent {
    EventListenerKey registerMutListener(Mut mut);

    Mut getInvoker();
}
