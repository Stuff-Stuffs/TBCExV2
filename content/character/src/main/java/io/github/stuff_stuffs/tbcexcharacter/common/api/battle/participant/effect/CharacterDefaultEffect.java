package io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.effect;

import com.mojang.serialization.Codec;
import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.CharacterBattleParticipantStats;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipant;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffect;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffectType;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStatModifier;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStatModifierHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStats;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;
import net.minecraft.entity.Entity;

public class CharacterDefaultEffect implements BattleParticipantEffect {
    public static final Codec<CharacterDefaultEffect> CODEC = Codec.unit(CharacterDefaultEffect::new);
    private BattleParticipantStatModifierHandle healthHandle;
    private BattleParticipantStatModifierHandle energyHandle;

    @Override
    public BattleParticipantEffectType<?, ?> getType() {
        return CharacterBattleParticipantEffects.BATTLE_PARTICIPANT_DEFAULT_EFFECT;
    }

    public static CharacterDefaultEffect combine(final CharacterDefaultEffect first, final CharacterDefaultEffect second) {
        throw new TBCExException("Can not combine two default effect instances");
    }

    public static <T extends Entity & BattleParticipant> CharacterDefaultEffect extract(final T entity) {
        return new CharacterDefaultEffect();
    }

    @Override
    public void init(final BattleParticipantState state, final Tracer<ActionTrace> tracer) {
        healthHandle = state.getStatContainer().addModifier(BattleParticipantStats.MAX_HEALTH, (current, stat) -> current + state.getStat(CharacterBattleParticipantStats.VITALITY), BattleParticipantStatModifier.Phase.ADDERS, tracer);
        energyHandle = state.getStatContainer().addModifier(BattleParticipantStats.MAX_ENERGY, (current, stat) -> current + state.getStat(CharacterBattleParticipantStats.DEXTERITY), BattleParticipantStatModifier.Phase.ADDERS, tracer);
    }

    @Override
    public void deinit(final Tracer<ActionTrace> tracer) {
        healthHandle.destroy(tracer);
        energyHandle.destroy(tracer);
    }
}
