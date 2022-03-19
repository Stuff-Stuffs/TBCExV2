package io.github.stuff_stuffs.tbcexcharacter.common.api.battle;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipant;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffect;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStat;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.function.Function;

public class BattleParticipantRace {
    private final Text name;
    private final Function<BattleParticipant, Collection<StatModifier>> statModifierGetter;
    private final Function<BattleParticipant, Collection<BattleParticipantEffect>> effectGetter;

    public BattleParticipantRace(final Text name, final Function<BattleParticipant, Collection<StatModifier>> statModifierGetter, final Function<BattleParticipant, Collection<BattleParticipantEffect>> effectGetter) {
        this.name = name;
        this.statModifierGetter = statModifierGetter;
        this.effectGetter = effectGetter;
    }

    public Text getName() {
        return name;
    }

    public Collection<StatModifier> getStatModifiers(final BattleParticipant participant) {
        return statModifierGetter.apply(participant);
    }

    public Collection<BattleParticipantEffect> getEffects(final BattleParticipant participant) {
        return effectGetter.apply(participant);
    }

    public static final class StatModifier {
        private final BattleParticipantStat stat;
        private final double add;
        private final double multiplier;

        public StatModifier(final BattleParticipantStat stat, final double add, final double multiplier) {
            this.stat = stat;
            this.add = add;
            this.multiplier = multiplier;
        }

        public BattleParticipantStat getStat() {
            return stat;
        }

        public double getAdd() {
            return add;
        }

        public double getMultiplier() {
            return multiplier;
        }
    }
}
