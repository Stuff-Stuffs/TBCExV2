package io.github.stuff_stuffs.tbcexutil.common.event;

public interface InvokerFactory<T> {
    T createInvoker(T[] listeners, Runnable enter, Runnable exit);
}
