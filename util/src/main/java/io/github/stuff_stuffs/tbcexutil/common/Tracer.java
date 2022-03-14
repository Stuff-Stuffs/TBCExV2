package io.github.stuff_stuffs.tbcexutil.common;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class Tracer<T> {
    private final Node<T> root;
    private final Predicate<T> debugPredicate;
    private Node<T> cursor;

    public Tracer(final T rootVal, Predicate<T> debugPredicate) {
        root = new Node<>(rootVal, -1, null);
        this.debugPredicate = debugPredicate;
        cursor = root;
    }

    public void push(final T val) {
        final Node<T> node = new Node<>(val, cursor.getChildCount(), cursor);
        cursor.children.add(node);
        cursor = node;
    }

    public void pop() {
        cursor = cursor.parent;
    }

    public Stream<T> getCurrentPath(boolean debug) {
        final Stream.Builder<T> builder = Stream.builder();
        prependParent(cursor, builder, debug);
        return builder.build();
    }

    private void prependParent(Node<T> node,  Stream.Builder<T> builder, boolean debug) {
        if(node.parent!=null) {
            prependParent(node.parent, builder, debug);
        }
        if(debug || debugPredicate.test(node.val)) {
            builder.accept(node.val);
        }
    }

    public static final class Node<T> {
        private final T val;
        private final List<Node<T>> children;
        private final int parentChildIndex;
        private final @Nullable Node<T> parent;

        private Node(final T val, final int parentChildIndex, @Nullable final Node<T> parent) {
            this.val = val;
            children = new ArrayList<>(1);
            this.parentChildIndex = parentChildIndex;
            this.parent = parent;
        }

        public T getVal() {
            return val;
        }

        public int getChildCount() {
            return children.size();
        }

        public Node<T> getChild(final int index) {
            return children.get(index);
        }

        public int getParentChildIndex() {
            return parentChildIndex;
        }

        public @Nullable Node<T> getParent() {
            return parent;
        }

        public Stream<Node<T>> stream() {
            return Stream.concat(Stream.of(this), children.stream().flatMap(node -> node.stream()));
        }
    }
}
