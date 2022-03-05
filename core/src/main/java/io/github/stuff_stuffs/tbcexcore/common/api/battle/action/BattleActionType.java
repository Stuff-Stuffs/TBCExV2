package io.github.stuff_stuffs.tbcexcore.common.api.battle.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import net.minecraft.util.registry.RegistryEntry;

public final class BattleActionType<T> {
    private final Codec<T> codec;
    private final RegistryEntry.Reference<BattleActionType<?>> reference;

    public BattleActionType(final Codec<T> codec) {
        this.codec = codec;
        reference = BattleActionTypes.REGISTRY.createEntry(this);
    }

    public <K> DataResult<T> decode(final DynamicOps<K> ops, final K val) {
        return codec.parse(ops, val);
    }

    public <K> DataResult<K> encode(final DynamicOps<K> ops, final BattleAction action) {
        if (action.getType() != this) {
            throw new TBCExException("Type mismatch");
        }
        return codec.encodeStart(ops, (T) action);
    }

    public RegistryEntry.Reference<BattleActionType<?>> getReference() {
        return reference;
    }
}
