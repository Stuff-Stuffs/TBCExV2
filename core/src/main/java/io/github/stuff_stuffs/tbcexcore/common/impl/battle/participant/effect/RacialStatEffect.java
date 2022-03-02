package io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffect;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffectType;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffectTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStat;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStats;
import it.unimi.dsi.fastutil.objects.Reference2DoubleMap;
import it.unimi.dsi.fastutil.objects.Reference2DoubleOpenHashMap;

import java.util.Map;

public class RacialStatEffect implements BattleParticipantEffect {
    public static final Codec<RacialStatEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.unboundedMap(BattleParticipantStats.REGISTRY.getCodec(), Codec.DOUBLE).fieldOf("basics").forGetter(effect -> effect.basics)).apply(instance, RacialStatEffect::new));
    private final Reference2DoubleMap<BattleParticipantStat> basics;

    private RacialStatEffect(final Map<BattleParticipantStat, Double> basics) {
        this.basics = new Reference2DoubleOpenHashMap<>(basics);
    }

    @Override
    public BattleParticipantEffectType<?> getType() {
        return BattleParticipantEffectTypes.RACIAL_STAT_BATTLE_PARTICIPANT_EFFECT;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final Reference2DoubleMap<BattleParticipantStat> basics = new Reference2DoubleOpenHashMap<>();

        public Builder set(final BattleParticipantStat stat, final double base) {
            basics.put(stat, base);
            return this;
        }

        public RacialStatEffect build() {
            return new RacialStatEffect(basics);
        }
    }
}
