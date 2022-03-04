package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipant;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.registry.RegistryEntry;
import org.jetbrains.annotations.Nullable;

import java.util.function.BinaryOperator;

public final class BattleParticipantEffectType<T extends BattleParticipantEffect> {
    private final Text name;
    private final Codec<T> codec;
    private final RegistryEntry.Reference<BattleParticipantEffectType<?>> reference;
    private final BinaryOperator<T> combiner;
    private final Extractor<T> extractor;

    public BattleParticipantEffectType(final Text name, final Codec<T> codec, final BinaryOperator<T> combiner, final Extractor<T> extractor) {
        this.name = name;
        this.codec = codec;
        this.combiner = combiner;
        this.extractor = extractor;
        reference = BattleParticipantEffectTypes.REGISTRY.createEntry(this);
    }

    public <K> DataResult<T> decode(final DynamicOps<K> ops, final K val) {
        return codec.parse(ops, val);
    }

    public <K> DataResult<K> encode(final DynamicOps<K> ops, final BattleParticipantEffect effect) {
        if (effect.getType() != this) {
            throw new TBCExException("Type mismatch");
        }
        return codec.encodeStart(ops, (T) effect);
    }

    public <E extends Entity & BattleParticipant> @Nullable T extract(final E entity) {
        return extractor.extract(entity);
    }

    public T combine(final BattleParticipantEffect first, final BattleParticipantEffect second) {
        if (first.getType() != this || second.getType() != this) {
            throw new TBCExException("Type mismatch");
        }
        return combiner.apply((T) first, (T) second);
    }

    public Codec<T> getCodec() {
        return codec;
    }

    public Text getName() {
        return name;
    }

    public RegistryEntry.Reference<BattleParticipantEffectType<?>> getReference() {
        return reference;
    }

    @Override
    public String toString() {
        return "BattleParticipantEffectType{" +
                "name=" + name +
                '}';
    }

    public interface Extractor<T extends BattleParticipantEffect> {
        <E extends Entity & BattleParticipant> @Nullable T extract(E entity);
    }
}
