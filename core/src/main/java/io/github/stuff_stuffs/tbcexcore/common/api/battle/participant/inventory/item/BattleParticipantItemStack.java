package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;

public final class BattleParticipantItemStack {
    public static final Codec<BattleParticipantItemStack> CODEC = RecordCodecBuilder.create(instance -> instance.group(BattleParticipantItem.CODEC.fieldOf("item").forGetter(BattleParticipantItemStack::getItem), Codec.INT.fieldOf("count").forGetter(BattleParticipantItemStack::getCount)).apply(instance, BattleParticipantItemStack::new));
    private final BattleParticipantItem item;
    private final int count;

    public BattleParticipantItemStack(final BattleParticipantItem item, final int count) {
        if (count < 1) {
            throw new TBCExException("BattleParticipantItemStack with count<1!");
        }
        this.item = item;
        this.count = count;
    }

    public BattleParticipantItem getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }

    public boolean canCombine(final BattleParticipantItemStack other) {
        return other.item.equals(item);
    }
}
