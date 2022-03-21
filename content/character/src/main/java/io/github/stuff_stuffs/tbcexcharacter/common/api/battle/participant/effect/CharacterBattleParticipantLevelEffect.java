package io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.BattleCharacter;
import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.CharacterLevelInfo;
import io.github.stuff_stuffs.tbcexcharacter.mixin.api.BattleCharacterHolder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipant;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffectType;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStat;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStatModifier;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStats;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import io.github.stuff_stuffs.tbcexutil.common.TBCExUtil;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CharacterBattleParticipantLevelEffect extends AbstractBattleParticipantStatModifierEffect {
    public static final Codec<CharacterBattleParticipantLevelEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(BattleParticipantStats.REGISTRY.getCodec(), Codec.list(Entry.CODEC)).fieldOf("stats").forGetter(effect -> effect.entryMap),
            Codec.INT.fieldOf("level").forGetter(effect -> effect.level)
    ).apply(instance, CharacterBattleParticipantLevelEffect::new));
    private static final int STATS_PER_POINT_SPENT = 5;
    private final int level;

    private CharacterBattleParticipantLevelEffect(final Map<BattleParticipantStat, List<Entry>> entryMap, final int level) {
        super(entryMap);
        this.level = level;
    }

    @Override
    public BattleParticipantEffectType<?, ?> getType() {
        return CharacterBattleParticipantEffects.BATTLE_PARTICIPANT_LEVEL_EFFECT;
    }

    public static <E extends Entity & BattleParticipant> CharacterBattleParticipantLevelEffect extract(final E entity) {
        BattleCharacter character;
        if (entity instanceof BattleCharacter c) {
            character = c;
        } else if(entity instanceof BattleCharacterHolder holder) {
            character = holder.getCharacter();
        } else {
            throw new TBCExException("Tried to get LevelInfo of a non BattleCharacter!");
        }
        final CharacterLevelInfo info = character.getLevelInfo();
        final Builder builder = builder();
        for (final BattleParticipantStat stat : BattleParticipantStats.REGISTRY) {
            builder.addEntry(stat, info.getStatInvestment(stat) * STATS_PER_POINT_SPENT);
        }
        return builder.build(info.getLevel());
    }

    public static CharacterBattleParticipantLevelEffect combine(final CharacterBattleParticipantLevelEffect first, final CharacterBattleParticipantLevelEffect second) {
        return TBCExUtil.unimplemented();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final Map<BattleParticipantStat, List<Entry>> entryMap = new Reference2ObjectOpenHashMap<>();

        private Builder() {
        }

        public Builder addEntry(final BattleParticipantStat stat, final double val) {
            entryMap.computeIfAbsent(stat, s -> new ArrayList<>()).add(new Entry(val, Entry.Op.ADD, BattleParticipantStatModifier.Phase.ADDERS));
            return this;
        }

        public CharacterBattleParticipantLevelEffect build(final int level) {
            return new CharacterBattleParticipantLevelEffect(entryMap, level);
        }
    }
}
