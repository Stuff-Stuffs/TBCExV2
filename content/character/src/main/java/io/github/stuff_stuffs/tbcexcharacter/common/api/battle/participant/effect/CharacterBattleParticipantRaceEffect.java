package io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.effect;

import com.mojang.serialization.Codec;
import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.BattleCharacter;
import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.BattleParticipantRace;
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
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CharacterBattleParticipantRaceEffect extends AbstractBattleParticipantStatModifierEffect {
    public static final Codec<CharacterBattleParticipantRaceEffect> CODEC = Codec.unboundedMap(BattleParticipantStats.REGISTRY.getCodec(), Codec.list(Entry.CODEC)).xmap(CharacterBattleParticipantRaceEffect::new, effect -> effect.entryMap);

    private CharacterBattleParticipantRaceEffect(final Map<BattleParticipantStat, List<Entry>> entryMap) {
        super(entryMap);
    }

    public static CharacterBattleParticipantRaceEffect combine(final CharacterBattleParticipantRaceEffect first, final CharacterBattleParticipantRaceEffect second) {
        return TBCExUtil.unimplemented();
    }

    public static <E extends Entity & BattleParticipant> CharacterBattleParticipantRaceEffect extract(final E entity) {
        if (entity instanceof BattleCharacter character) {
            final Collection<BattleParticipantRace.StatModifier> statModifiers = character.getRace().getStatModifiers(entity);
            final Builder builder = new Builder();
            for (final BattleParticipantRace.StatModifier modifier : statModifiers) {
                builder.addEntry(modifier.getStat(), modifier.getAdd(), BattleParticipantStatModifier.Phase.ADDERS, Entry.Op.ADD);
                builder.addEntry(modifier.getStat(), modifier.getMultiplier(), BattleParticipantStatModifier.Phase.MULTIPLIERS, Entry.Op.MULTIPLY);
            }
            return builder.build();
        } else {
            throw new TBCExException("Tried to get race of non BattleCharacter!");
        }
    }

    @Override
    public BattleParticipantEffectType<?, ?> getType() {
        return CharacterBattleParticipantEffects.BATTLE_PARTICIPANT_RACE_EFFECT;
    }

    public static final class Builder {
        private final Map<BattleParticipantStat, List<Entry>> entryMap = new Reference2ObjectOpenHashMap<>();

        private Builder() {
        }

        public Builder addEntry(final BattleParticipantStat stat, final double val, final BattleParticipantStatModifier.Phase phase, final Entry.Op op) {
            entryMap.computeIfAbsent(stat, s -> new ArrayList<>()).add(new Entry(val, op, phase));
            return this;
        }

        public CharacterBattleParticipantRaceEffect build() {
            return new CharacterBattleParticipantRaceEffect(entryMap);
        }
    }
}
