package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.github.stuff_stuffs.tbcexutil.common.CodecUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Set;

public interface BattleParticipantItem {
    Codec<BattleParticipantItem> CODEC = CodecUtil.createDependentPairCodecFirst(BattleParticipantItemTypes.REGISTRY.getCodec(), new CodecUtil.DependentEncoder<>() {
        @Override
        public <T> DataResult<T> encode(final BattleParticipantItemType<?> coValue, final BattleParticipantItem value, final DynamicOps<T> ops) {
            return coValue.encode(ops, value);
        }
    }, new CodecUtil.DependentDecoder<>() {
        @Override
        public <T> DataResult<BattleParticipantItem> decode(final BattleParticipantItemType<?> coValue, final T value, final DynamicOps<T> ops) {
            return coValue.decode(ops, value);
        }
    }, BattleParticipantItem::getType);

    Text getName();

    BattleParticipantItemType<?> getType();

    Set<BattleParticipantItemCategory> getCategory();

    BattleParticipantItemRarity getRarity();

    List<ItemStack> convert(int count);

    //MUST OVERRIDE
    @Override
    boolean equals(Object o);

    //MUST OVERRIDE
    @Override
    int hashCode();
}
