package io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.race;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipant;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffect;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStat;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.registry.RegistryEntry;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public final class BattleParticipantRace {
    private final Text name;
    private final Function<BattleParticipant, Map<BattleParticipantStat, StatModifier>> statModifierGetter;
    private final EffectExtractor effectGetter;
    private final RegistryEntry.Reference<BattleParticipantRace> reference;

    public BattleParticipantRace(final Text name, final Function<BattleParticipant, Map<BattleParticipantStat, StatModifier>> statModifierGetter, final EffectExtractor effectGetter) {
        this.name = name;
        this.statModifierGetter = statModifierGetter;
        this.effectGetter = effectGetter;
        reference = BattleParticipantRaces.REGISTRY.createEntry(this);
    }

    public Text getName() {
        return name;
    }

    public Map<BattleParticipantStat, StatModifier> getStatModifiers(final BattleParticipant participant) {
        return statModifierGetter.apply(participant);
    }

    public <E extends Entity & BattleParticipant> Collection<BattleParticipantEffect> getEffects(final E entity) {
        return effectGetter.extract(entity);
    }

    public RegistryEntry.Reference<BattleParticipantRace> getReference() {
        return reference;
    }

    public static final class StatModifier {
        private final double add;
        private final double multiplier;

        public StatModifier(final double add, final double multiplier) {
            this.add = add;
            this.multiplier = multiplier;
        }

        public double getAdd() {
            return add;
        }

        public double getMultiplier() {
            return multiplier;
        }
    }

    public static ModifierBuilder modifierBuilder() {
        return new ModifierBuilder();
    }

    public static final class ModifierBuilder {
        private final Map<BattleParticipantStat, StatModifier> mods = new Reference2ObjectOpenHashMap<>();

        private ModifierBuilder() {
        }

        public ModifierBuilder set(final BattleParticipantStat stat, final double add, final double multiplier) {
            mods.put(stat, new StatModifier(add, multiplier));
            return this;
        }

        public Map<BattleParticipantStat, StatModifier> build() {
            return new Reference2ObjectOpenHashMap<>(mods);
        }
    }

    public interface EffectExtractor {
        <E extends Entity & BattleParticipant> Collection<BattleParticipantEffect> extract(E entity);
    }
}
