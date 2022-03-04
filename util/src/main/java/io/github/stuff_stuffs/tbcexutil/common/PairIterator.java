package io.github.stuff_stuffs.tbcexutil.common;

import java.util.Iterator;
import java.util.Map;

public interface PairIterator<L, R> {
    boolean next();

    L getLeft();

    R getRight();

    default PairIterator<R, L> swap() {
        return new PairIterator<>() {
            @Override
            public boolean next() {
                return PairIterator.this.next();
            }

            @Override
            public R getLeft() {
                return PairIterator.this.getRight();
            }

            @Override
            public L getRight() {
                return PairIterator.this.getLeft();
            }
        };
    }

    static <L, R> PairIterator<L, R> fromMap(final Map<L, R> map) {
        return new PairIterator<>() {
            private final Iterator<Map.Entry<L, R>> iterator = map.entrySet().iterator();
            private boolean init = false;
            private Map.Entry<L, R> cursor = null;

            @Override
            public boolean next() {
                if (iterator.hasNext()) {
                    init = true;
                    cursor = iterator.next();
                    return true;
                }
                return false;
            }

            @Override
            public L getLeft() {
                if (!init) {
                    throw new TBCExException("Used pair iterator without calling next!");
                }
                return cursor.getKey();
            }

            @Override
            public R getRight() {
                if (!init) {
                    throw new TBCExException("Used pair iterator without calling next!");
                }
                return cursor.getValue();
            }
        };
    }
}
