package io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.effect;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffect;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffectContainer;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffectType;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffectTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexutil.common.CodecUtil;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;
import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;

import java.util.List;
import java.util.function.Function;

public class BattleParticipantEffectContainerImpl implements BattleParticipantEffectContainer {
    public static final Codec<BattleParticipantEffectContainerImpl> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.list(CodecUtil.createDependentPairCodec(BattleParticipantEffectTypes.REGISTRY.getCodec(), new CodecUtil.DependentEncoder<BattleParticipantEffectType<?, ?>, BattleParticipantEffect>() {
        @Override
        public <T> DataResult<T> encode(final BattleParticipantEffectType<?, ?> coValue, final BattleParticipantEffect value, final DynamicOps<T> ops) {
            return coValue.encode(ops, value);
        }
    }, new CodecUtil.DependentDecoder<>() {
        @Override
        public <T> DataResult<BattleParticipantEffect> decode(final BattleParticipantEffectType<?, ?> coValue, final T value, final DynamicOps<T> ops) {
            return coValue.decode(ops, value).map(Function.identity());
        }
    })).fieldOf("effects").forGetter(BattleParticipantEffectContainerImpl::getPairsForCodec)).apply(instance, BattleParticipantEffectContainerImpl::new));
    private final Reference2ObjectLinkedOpenHashMap<BattleParticipantEffectType<?, ?>, BattleParticipantEffect> effects;
    private boolean init = false;
    private BattleParticipantState state;

    public BattleParticipantEffectContainerImpl() {
        effects = new Reference2ObjectLinkedOpenHashMap<>();
        init = true;
    }

    private BattleParticipantEffectContainerImpl(final List<Pair<BattleParticipantEffectType<?, ?>, BattleParticipantEffect>> pairs) {
        effects = new Reference2ObjectLinkedOpenHashMap<>();
        for (final Pair<BattleParticipantEffectType<?, ?>, BattleParticipantEffect> pair : pairs) {
            effects.putAndMoveToLast(pair.getFirst(), pair.getSecond());
        }
    }

    private List<Pair<BattleParticipantEffectType<?, ?>, BattleParticipantEffect>> getPairsForCodec() {
        return effects.entrySet().stream().map(entry -> Pair.<BattleParticipantEffectType<?, ?>, BattleParticipantEffect>of(entry.getKey(), entry.getValue())).toList();
    }

    public void init(final BattleParticipantState state, Tracer<ActionTrace> tracer) {
        if (init) {
            return;
        }
        this.state = state;
        init = true;
        for (final BattleParticipantEffect effect : effects.values()) {
            effect.init(state, tracer);
        }
    }

    @Override
    public void addEffect(final BattleParticipantEffect effect, Tracer<ActionTrace> tracer) {
        if (!init) {
            throw new TBCExException("Not initialized effect container before tried to use");
        }
        final BattleParticipantEffect prev = effects.putAndMoveToLast(effect.getType(), effect);
        if (prev != null) {
            effects.remove(effect.getType());
            final BattleParticipantEffect combined = effect.getType().combine(effect, prev);
            effects.put(effect.getType(), combined);
            prev.deinit(tracer);
            combined.init(state, tracer);
        } else {
            effect.init(state, tracer);
        }
    }

    @Override
    public void removeEffect(final BattleParticipantEffectType<?, ?> type, Tracer<ActionTrace> tracer) {
        if (!init) {
            throw new TBCExException("Not initialized effect container before tried to use");
        }
        final BattleParticipantEffect removed = effects.remove(type);
        if (removed != null) {
            removed.deinit(tracer);
        }
    }

    @Override
    public boolean hasEffect(final BattleParticipantEffectType<?, ?> type) {
        if (!init) {
            throw new TBCExException("Not initialized effect container before tried to use");
        }
        return effects.containsKey(type);
    }

    @Override
    public <View extends BattleParticipantEffect> View getEffect(final BattleParticipantEffectType<View, ?> type) {
        if (!init) {
            throw new TBCExException("Not initialized effect container before tried to use");
        }
        return (View) effects.get(type);
    }

    @Override
    public <View extends BattleParticipantEffect, Mut extends View> Mut getEffectMut(final BattleParticipantEffectType<View, Mut> type) {
        if (!init) {
            throw new TBCExException("Not initialized effect container before tried to use");
        }
        return (Mut) effects.get(type);
    }
}
