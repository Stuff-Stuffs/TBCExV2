package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.github.stuff_stuffs.tbcexutil.common.CodecUtil;

public interface BattleItem {
    Codec<BattleItem> CODEC = CodecUtil.createDependentPairCodecFirst(BattleItemTypes.REGISTRY.getCodec(), new CodecUtil.DependentEncoder<>() {
        @Override
        public <T> DataResult<T> encode(final BattleItemType<?> coValue, final BattleItem value, final DynamicOps<T> ops) {
            return coValue.encode(ops, value);
        }
    }, new CodecUtil.DependentDecoder<>() {
        @Override
        public <T> DataResult<BattleItem> decode(final BattleItemType<?> coValue, final T value, final DynamicOps<T> ops) {
            return coValue.decode(ops, value);
        }
    }, BattleItem::getType);

    BattleItemType<?> getType();

    //MUST OVERRIDE
    @Override
    boolean equals(Object o);

    //MUST OVERRIDE
    @Override
    int hashCode();
}
