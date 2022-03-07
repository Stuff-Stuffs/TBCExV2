package io.github.stuff_stuffs.tbcexutil.common;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;
import java.util.function.Predicate;

public final class Tracer<T> {
    private final ObjectArrayList<T> stack;
    private final Predicate<T> nonDebugPredicate;

    public Tracer(final Predicate<T> debugPredicate) {
        stack = new ObjectArrayList<>();
        nonDebugPredicate = debugPredicate.negate();
    }

    public void push(final T val) {
        stack.push(val);
    }

    public void pop() {
        stack.pop();
    }

    public List<T> getStack() {
        return stack.stream().filter(nonDebugPredicate).toList();
    }

    public List<T> getDebugStack() {
        return List.copyOf(stack);
    }
}
