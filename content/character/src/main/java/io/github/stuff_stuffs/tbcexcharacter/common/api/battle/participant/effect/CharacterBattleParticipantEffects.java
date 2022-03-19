package io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.effect;

import io.github.stuff_stuffs.tbcexcharacter.common.TBCExCharacter;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffectType;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffectTypes;
import net.minecraft.text.LiteralText;
import net.minecraft.util.registry.Registry;

public final class CharacterBattleParticipantEffects {
    public static final BattleParticipantEffectType<CharacterBattleParticipantRaceEffect, CharacterBattleParticipantRaceEffect> BATTLE_PARTICIPANT_RACE_EFFECT = new BattleParticipantEffectType<>(new LiteralText("Racial Effect"), CharacterBattleParticipantRaceEffect.CODEC, CharacterBattleParticipantRaceEffect::combine, CharacterBattleParticipantRaceEffect::extract);

    public static void init() {
        Registry.register(BattleParticipantEffectTypes.REGISTRY, TBCExCharacter.createId("race_effect"), BATTLE_PARTICIPANT_RACE_EFFECT);
    }

    private CharacterBattleParticipantEffects() {
    }
}
