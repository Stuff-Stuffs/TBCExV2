package io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStat;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStats;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;

import java.util.Map;

//TODO perks/skills
public final class CharacterLevelInfo {
    public static final Codec<CharacterLevelInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.simpleMap(BattleParticipantStats.REGISTRY.getCodec(), Codec.INT, BattleParticipantStats.REGISTRY).fieldOf("statInvestments").forGetter(info -> info.statInvestments),
            Codec.INT.fieldOf("level").forGetter(info -> info.level),
            Codec.INT.fieldOf("unspentPoints").forGetter(info -> info.unspentPoints)
    ).apply(instance, CharacterLevelInfo::new));
    private final int level;
    private final int unspentPoints;
    private final Reference2IntMap<BattleParticipantStat> statInvestments;

    private CharacterLevelInfo(final Map<BattleParticipantStat, Integer> statInvestments, final int level, final int unspentPoints) {
        this.level = level;
        this.unspentPoints = unspentPoints;
        this.statInvestments = new Reference2IntOpenHashMap<>();
        for (final Map.Entry<BattleParticipantStat, Integer> entry : statInvestments.entrySet()) {
            final int value = entry.getValue();
            if (value > 0) {
                this.statInvestments.put(entry.getKey(), value);
            }
        }
    }

    public int getStatInvestment(final BattleParticipantStat stat) {
        return statInvestments.getInt(stat);
    }

    public int getLevel() {
        return level;
    }

    public int getUnspentPoints() {
        return unspentPoints;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final Reference2IntMap<BattleParticipantStat> statInvestments = new Reference2IntOpenHashMap<>();

        private Builder() {
        }

        public Builder set(final BattleParticipantStat stat, final int investments) {
            if (investments < 0) {
                throw new TBCExException("Can not negatively invest in a stat!");
            }
            statInvestments.put(stat, investments);
            return this;
        }

        public CharacterLevelInfo build(final int level, final int unspentPoints) {
            if (level < 0) {
                throw new TBCExException("Can not have a negative level!");
            }
            if (unspentPoints < 0) {
                throw new TBCExException("Can not have negative unspent points!");
            }
            return new CharacterLevelInfo(statInvestments, level, unspentPoints);
        }
    }
}
