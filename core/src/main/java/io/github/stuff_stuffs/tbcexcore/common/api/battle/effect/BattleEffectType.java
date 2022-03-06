package io.github.stuff_stuffs.tbcexcore.common.api.battle.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantEquipmentSlots;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryEntry;

import java.util.function.BinaryOperator;
import java.util.function.Function;

public final class BattleEffectType<View extends BattleEffect, Mut extends View> {
    private final Codec<Mut> codec;
    private final BinaryOperator<Mut> combiner;
    private final RegistryEntry.Reference<BattleEffectType<?, ?>> reference;

    public BattleEffectType(final Codec<Mut> codec, final BinaryOperator<Mut> combiner) {
        this.codec = codec;
        this.combiner = combiner;
        reference = BattleEffectTypes.REGISTRY.createEntry(this);
    }

    public <K> DataResult<K> encode(final DynamicOps<K> ops, final BattleEffect effect) {
        return codec.encodeStart(ops, (Mut) effect);
    }

    public <K> DataResult<BattleEffect> decode(final DynamicOps<K> ops, final K val) {
        return codec.parse(ops, val).map(Function.identity());
    }

    public RegistryEntry.Reference<BattleEffectType<?, ?>> getReference() {
        return reference;
    }

    public Mut combine(final BattleEffect first, final BattleEffect second) {
        if (first.getType() != this || second.getType() != this) {
            throw new TBCExException("Type mismatch");
        }
        return combiner.apply((Mut) first, (Mut) second);
    }

    @Override
    public String toString() {
        final Identifier id = BattleEffectTypes.REGISTRY.getId(this);
        if (id == null) {
            return "Unregistered BattleEffectType";
        }
        return "BattleEffectType{" + id + "}";
    }
}
