package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import io.github.stuff_stuffs.tbcexutil.common.CodecUtil;
import net.minecraft.text.Text;
import net.minecraft.util.registry.RegistryEntry;

public final class BattleParticipantEffectType<T extends BattleParticipantEffect> {
    private final Text name;
    private final Codec<T> codec;
    private final RegistryEntry.Reference<BattleParticipantEffectType<?>> reference;

    public BattleParticipantEffectType(final Text name, final Codec<T> codec) {
        this.name = name;
        this.codec = codec;
        reference = BattleParticipantEffectTypes.REGISTRY.createEntry(this);
    }

    public <K> T decode(final DynamicOps<K> ops, final K val) {
        return CodecUtil.get(codec.parse(ops, val));
    }

    public <K> K encode(final DynamicOps<K> ops, final BattleParticipantEffect effect) {
        return CodecUtil.get(codec.encodeStart(ops, (T) effect));
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
}
