package io.github.stuff_stuffs.tbcexutil.common;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

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

    static PairIterator<ItemStack, Integer> fromInventory(final Inventory inventory) {
        return new PairIterator<>() {
            private int index = -1;

            @Override
            public boolean next() {
                index++;
                while (index < inventory.size() && inventory.getStack(index).isEmpty()) {
                    index++;
                }
                return index < inventory.size();
            }

            @Override
            public ItemStack getLeft() {
                return inventory.getStack(index);
            }

            @Override
            public Integer getRight() {
                return index;
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
