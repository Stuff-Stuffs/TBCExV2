package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item;

import io.github.stuff_stuffs.tbcexutil.common.TBCExException;

public final class BattleItemStack {
    private final BattleItem item;
    private final int count;

    public BattleItemStack(final BattleItem item, final int count) {
        if (count < 1) {
            throw new TBCExException("BattleItemStack with count<1!");
        }
        this.item = item;
        this.count = count;
    }

    public BattleItem getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }

    public boolean canCombine(final BattleItemStack other) {
        return other.item.equals(item);
    }
}
