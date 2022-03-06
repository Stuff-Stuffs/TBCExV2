package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.damage;

import com.mojang.serialization.Codec;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.objects.Reference2DoubleMap;
import it.unimi.dsi.fastutil.objects.Reference2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2LongMap;
import it.unimi.dsi.fastutil.objects.Reference2LongOpenHashMap;

import java.util.Map;

public final class BattleParticipantDamagePacket {
    public static final Codec<BattleParticipantDamagePacket> CODEC = Codec.unboundedMap(BattleParticipantDamageTypes.REGISTRY.getCodec(), Codec.LONG).xmap(BattleParticipantDamagePacket::new, packet -> packet.damageByType);
    private final Reference2LongMap<BattleParticipantDamageType> damageByType;
    private final long sum;

    private BattleParticipantDamagePacket(final Reference2LongMap<BattleParticipantDamageType> damageByType, final long sum) {
        this.damageByType = damageByType;
        this.sum = sum;
    }

    private BattleParticipantDamagePacket(final Map<BattleParticipantDamageType, Long> damageByType) {
        this.damageByType = new Reference2LongOpenHashMap<>(damageByType);
        long s = 0;
        final LongIterator iterator = this.damageByType.values().iterator();
        while (iterator.hasNext()) {
            s += iterator.nextLong();
        }
        sum = s;
    }

    public long getDamageOfType(final BattleParticipantDamageType type) {
        return damageByType.getLong(type);
    }

    public long getSum() {
        return sum;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final Reference2LongMap<BattleParticipantDamageType> damageByType = new Reference2LongOpenHashMap<>();
        private long sum;

        private Builder() {
        }

        public Builder set(final BattleParticipantDamageType type, final long amount) {
            if (amount < 0) {
                throw new TBCExException("Negative damage amount with type: " + type);
            }
            sum += amount;
            sum -= damageByType.put(type, amount);
            return this;
        }

        public BattleParticipantDamagePacket build() {
            return new BattleParticipantDamagePacket(new Reference2LongOpenHashMap<>(damageByType), sum);
        }
    }

    public static PercentBuilder percentBuilder() {
        return new PercentBuilder();
    }

    public static final class PercentBuilder {
        private final Reference2DoubleMap<BattleParticipantDamageType> damagePercentByType = new Reference2DoubleOpenHashMap<>();
        private double sum;

        public PercentBuilder set(final BattleParticipantDamageType type, final double amount) {
            if (amount < 0) {
                throw new TBCExException("Negative damage amount with type: " + type);
            }
            sum += amount;
            sum -= damagePercentByType.put(type, amount);
            return this;
        }

        public BattleParticipantDamagePacket build(final long totalDamage) {
            long damageSum = 0;
            final Reference2LongMap<BattleParticipantDamageType> damageByType = new Reference2LongOpenHashMap<>();
            for (final Reference2DoubleMap.Entry<BattleParticipantDamageType> entry : damagePercentByType.reference2DoubleEntrySet()) {
                final long damage = (long) (entry.getDoubleValue() / sum * totalDamage);
                damageSum += damage;
                damageByType.put(entry.getKey(), damage);
            }
            if (damageSum == 0) {
                throw new TBCExException("Zero entry BattleParticipantDamagePacket percent builder");
            }
            return new BattleParticipantDamagePacket(damageByType, damageSum);
        }
    }
}
