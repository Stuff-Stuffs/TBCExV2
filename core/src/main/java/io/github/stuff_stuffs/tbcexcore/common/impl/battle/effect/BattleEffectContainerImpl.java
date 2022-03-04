package io.github.stuff_stuffs.tbcexcore.common.impl.battle.effect;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.effect.BattleEffect;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.effect.BattleEffectContainer;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.effect.BattleEffectType;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.effect.BattleEffectTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleState;
import io.github.stuff_stuffs.tbcexutil.common.CodecUtil;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;

import java.util.List;

public class BattleEffectContainerImpl implements BattleEffectContainer {
    public static final Codec<BattleEffectContainerImpl> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.list(CodecUtil.createDependentPairCodec(BattleEffectTypes.REGISTRY.getCodec(), new CodecUtil.DependentEncoder<BattleEffectType<?, ?>, BattleEffect>() {
        @Override
        public <T> DataResult<T> encode(final BattleEffectType<?, ?> coValue, final BattleEffect value, final DynamicOps<T> ops) {
            return coValue.encode(ops, value);
        }
    }, new CodecUtil.DependentDecoder<>() {
        @Override
        public <T> DataResult<BattleEffect> decode(final BattleEffectType<?, ?> coValue, final T value, final DynamicOps<T> ops) {
            return coValue.decode(ops, value);
        }
    })).fieldOf("effects").forGetter(BattleEffectContainerImpl::getPairsForCodec)).apply(instance, BattleEffectContainerImpl::new));
    private final Reference2ObjectLinkedOpenHashMap<BattleEffectType<?, ?>, BattleEffect> effects;
    private boolean init = false;
    private BattleState state;

    public BattleEffectContainerImpl() {
        effects = new Reference2ObjectLinkedOpenHashMap<>();
        init = true;
    }

    private BattleEffectContainerImpl(final List<Pair<BattleEffectType<?, ?>, BattleEffect>> pairs) {
        effects = new Reference2ObjectLinkedOpenHashMap<>();
        for (final Pair<BattleEffectType<?, ?>, BattleEffect> pair : pairs) {
            effects.putAndMoveToLast(pair.getFirst(), pair.getSecond());
        }
    }

    private List<Pair<BattleEffectType<?, ?>, BattleEffect>> getPairsForCodec() {
        return effects.entrySet().stream().map(entry -> Pair.<BattleEffectType<?, ?>, BattleEffect>of(entry.getKey(), entry.getValue())).toList();
    }

    public void init(final BattleState state) {
        if (init) {
            return;
        }
        this.state = state;
        init = true;
        for (final BattleEffect effect : effects.values()) {
            effect.init(state);
        }
    }

    @Override
    public boolean addEffect(final BattleEffect effect) {
        if (!init) {
            throw new TBCExException("Not initialized effect container before tried to use");
        }
        final BattleEffect prev = effects.putAndMoveToLast(effect.getType(), effect);
        if (prev != null) {
            effects.remove(effect.getType());
            final BattleEffect combined = effect.getType().combine(effect, prev);
            effects.put(effect.getType(), combined);
            prev.deinit();
            combined.init(state);
        } else {
            effect.init(state);
        }
        return true;
    }

    @Override
    public boolean removeEffect(final BattleEffectType<?, ?> type) {
        if (!init) {
            throw new TBCExException("Not initialized effect container before tried to use");
        }
        final BattleEffect removed = effects.remove(type);
        if (removed != null) {
            removed.deinit();
        }
        return true;
    }

    @Override
    public boolean hasEffect(final BattleEffectType<?, ?> type) {
        if (!init) {
            throw new TBCExException("Not initialized effect container before tried to use");
        }
        return effects.containsKey(type);
    }

    @Override
    public <View extends BattleEffect> View getEffect(final BattleEffectType<View, ?> type) {
        if (!init) {
            throw new TBCExException("Not initialized effect container before tried to use");
        }
        return (View) effects.get(type);
    }

    @Override
    public <View extends BattleEffect, Mut extends View> Mut getEffectMut(final BattleEffectType<View, Mut> type) {
        if (!init) {
            throw new TBCExException("Not initialized effect container before tried to use");
        }
        return (Mut) effects.get(type);
    }
}
