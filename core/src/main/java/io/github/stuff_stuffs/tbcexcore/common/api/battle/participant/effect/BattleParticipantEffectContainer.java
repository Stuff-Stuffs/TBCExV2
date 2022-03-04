package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexutil.common.CodecUtil;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;

import java.util.List;
import java.util.function.Function;

public final class BattleParticipantEffectContainer {
    public static final Codec<BattleParticipantEffectContainer> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.list(CodecUtil.createDependentPairCodec(BattleParticipantEffectTypes.REGISTRY.getCodec(), new CodecUtil.DependentEncoder<BattleParticipantEffectType<?>, BattleParticipantEffect>() {
        @Override
        public <T> DataResult<T> encode(final BattleParticipantEffectType<?> coValue, final BattleParticipantEffect value, final DynamicOps<T> ops) {
            return coValue.encode(ops, value);
        }
    }, new CodecUtil.DependentDecoder<>() {
        @Override
        public <T> DataResult<BattleParticipantEffect> decode(final BattleParticipantEffectType<?> coValue, final T value, final DynamicOps<T> ops) {
            return coValue.decode(ops, value).map(Function.identity());
        }
    })).fieldOf("effects").forGetter(BattleParticipantEffectContainer::getPairsForCodec)).apply(instance, BattleParticipantEffectContainer::new));
    private final Reference2ObjectLinkedOpenHashMap<BattleParticipantEffectType<?>, BattleParticipantEffect> effects;
    private boolean init = false;

    public BattleParticipantEffectContainer() {
        effects = new Reference2ObjectLinkedOpenHashMap<>();
        init = true;
    }

    private BattleParticipantEffectContainer(final List<Pair<BattleParticipantEffectType<?>, BattleParticipantEffect>> pairs) {
        effects = new Reference2ObjectLinkedOpenHashMap<>();
        for (final Pair<BattleParticipantEffectType<?>, BattleParticipantEffect> pair : pairs) {
            effects.putAndMoveToLast(pair.getFirst(), pair.getSecond());
        }
    }

    private List<Pair<BattleParticipantEffectType<?>, BattleParticipantEffect>> getPairsForCodec() {
        return effects.entrySet().stream().map(entry -> Pair.<BattleParticipantEffectType<?>, BattleParticipantEffect>of(entry.getKey(), entry.getValue())).toList();
    }

    public void init(final BattleParticipantState state) {
        if (init) {
            return;
        }
        for (final BattleParticipantEffect effect : effects.values()) {
            effect.init(state);
        }
        init = true;
    }

    public void addEffect(final BattleParticipantEffect effect, final BattleParticipantState state) {
        if (!init) {
            throw new TBCExException("Not initialized effect container before tried to use");
        }
        final BattleParticipantEffect prev = effects.putAndMoveToLast(effect.getType(), effect);
        if (prev != null) {
            effects.remove(effect.getType());
            final BattleParticipantEffect combined = effect.getType().combine(effect, prev);
            effects.put(effect.getType(), combined);
            prev.deinit();
            combined.init(state);
        } else {
            effect.init(state);
        }
    }

    public void removeEffect(final BattleParticipantEffectType<?> type) {
        if (!init) {
            throw new TBCExException("Not initialized effect container before tried to use");
        }
        final BattleParticipantEffect removed = effects.remove(type);
        if (removed != null) {
            removed.deinit();
        }
    }

    public boolean hasEffect(final BattleParticipantEffectType<?> type) {
        if (!init) {
            throw new TBCExException("Not initialized effect container before tried to use");
        }
        return effects.containsKey(type);
    }

    public <T extends BattleParticipantEffect> T getEffect(final BattleParticipantEffectType<T> type) {
        if (!init) {
            throw new TBCExException("Not initialized effect container before tried to use");
        }
        return (T) effects.get(type);
    }
}
