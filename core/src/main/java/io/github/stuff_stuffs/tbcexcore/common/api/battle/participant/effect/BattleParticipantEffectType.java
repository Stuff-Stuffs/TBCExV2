package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipant;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.damage.BattleParticipantDamageTypes;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryEntry;
import org.jetbrains.annotations.Nullable;

import java.util.function.BinaryOperator;

public final class BattleParticipantEffectType<View extends BattleParticipantEffect, Mut extends View> {
    private final Text name;
    private final Codec<Mut> codec;
    private final RegistryEntry.Reference<BattleParticipantEffectType<?, ?>> reference;
    private final BinaryOperator<Mut> combiner;
    private final Extractor<Mut> extractor;

    public BattleParticipantEffectType(final Text name, final Codec<Mut> codec, final BinaryOperator<Mut> combiner, final Extractor<Mut> extractor) {
        this.name = name;
        this.codec = codec;
        this.combiner = combiner;
        this.extractor = extractor;
        reference = BattleParticipantEffectTypes.REGISTRY.createEntry(this);
    }

    public <K> DataResult<Mut> decode(final DynamicOps<K> ops, final K val) {
        return codec.parse(ops, val);
    }

    public <K> DataResult<K> encode(final DynamicOps<K> ops, final BattleParticipantEffect effect) {
        if (effect.getType() != this) {
            throw new TBCExException("Type mismatch");
        }
        return codec.encodeStart(ops, (Mut) effect);
    }

    public <E extends Entity & BattleParticipant> @Nullable Mut extract(final E entity) {
        return extractor.extract(entity);
    }

    public View combine(final BattleParticipantEffect first, final BattleParticipantEffect second) {
        if (first.getType() != this || second.getType() != this) {
            throw new TBCExException("Type mismatch");
        }
        return combiner.apply((Mut) first, (Mut) second);
    }

    public Codec<Mut> getCodec() {
        return codec;
    }

    public Text getName() {
        return name;
    }

    public RegistryEntry.Reference<BattleParticipantEffectType<?, ?>> getReference() {
        return reference;
    }

    @Override
    public String toString() {
        final Identifier id = BattleParticipantEffectTypes.REGISTRY.getId(this);
        if (id == null) {
            return "Unregistered BattleParticipantEffectType";
        }
        return "BattleParticipantEffectType{" + id + "}";
    }

    public interface Extractor<T extends BattleParticipantEffect> {
        <E extends Entity & BattleParticipant> @Nullable T extract(E entity);
    }
}
