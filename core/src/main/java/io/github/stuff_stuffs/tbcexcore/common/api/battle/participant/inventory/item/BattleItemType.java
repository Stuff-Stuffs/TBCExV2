package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.util.registry.RegistryEntry;

import java.util.function.Function;

public final class BattleItemType<T extends BattleItem> {
    private final Codec<T> codec;
    private final RegistryEntry.Reference<BattleItemType<?>> reference;

    public BattleItemType(final Codec<T> codec) {
        this.codec = codec;
        reference = BattleItemTypes.REGISTRY.createEntry(this);
    }

    public <K> DataResult<BattleItem> decode(final DynamicOps<K> ops, final K val) {
        return codec.parse(ops, val).map(Function.identity());
    }

    public <K> DataResult<K> encode(final DynamicOps<K> ops, final BattleItem item) {
        return codec.encodeStart(ops, (T) item);
    }

    public RegistryEntry.Reference<BattleItemType<?>> getReference() {
        return reference;
    }

    public Codec<T> getCodec() {
        return codec;
    }
}
