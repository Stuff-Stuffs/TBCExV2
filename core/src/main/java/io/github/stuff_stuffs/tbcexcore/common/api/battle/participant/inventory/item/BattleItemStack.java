package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;

public final class BattleItemStack {
    public static final Codec<BattleItemStack> CODEC = RecordCodecBuilder.create(instance -> instance.group(BattleItem.CODEC.fieldOf("item").forGetter(BattleItemStack::getItem), Codec.INT.fieldOf("count").forGetter(BattleItemStack::getCount)).apply(instance, BattleItemStack::new));
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
