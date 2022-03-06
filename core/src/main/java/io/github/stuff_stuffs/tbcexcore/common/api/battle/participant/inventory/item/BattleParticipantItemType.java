package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryEntry;

import java.util.function.Function;

public final class BattleParticipantItemType<T extends BattleParticipantItem> {
    private final Codec<T> codec;
    private final RegistryEntry.Reference<BattleParticipantItemType<?>> reference;

    public BattleParticipantItemType(final Codec<T> codec) {
        this.codec = codec;
        reference = BattleParticipantItemTypes.REGISTRY.createEntry(this);
    }

    public <K> DataResult<BattleParticipantItem> decode(final DynamicOps<K> ops, final K val) {
        return codec.parse(ops, val).map(Function.identity());
    }

    public <K> DataResult<K> encode(final DynamicOps<K> ops, final BattleParticipantItem item) {
        return codec.encodeStart(ops, (T) item);
    }

    public RegistryEntry.Reference<BattleParticipantItemType<?>> getReference() {
        return reference;
    }

    public Codec<T> getCodec() {
        return codec;
    }

    @Override
    public String toString() {
        final Identifier id = BattleParticipantItemTypes.REGISTRY.getId(this);
        if (id == null) {
            return "Unregistered BattleItemType";
        }
        return "BattleItemType{" + id + "}";
    }
}
