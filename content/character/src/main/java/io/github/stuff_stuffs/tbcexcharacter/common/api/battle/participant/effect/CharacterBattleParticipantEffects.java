package io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.effect;

import io.github.stuff_stuffs.tbcexcharacter.common.TBCExCharacter;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffectType;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffectTypes;
import net.minecraft.text.LiteralText;
import net.minecraft.util.registry.Registry;

public final class CharacterBattleParticipantEffects {
    public static final BattleParticipantEffectType<CharacterBattleParticipantRaceEffect, CharacterBattleParticipantRaceEffect> BATTLE_PARTICIPANT_RACE_EFFECT = new BattleParticipantEffectType<>(new LiteralText("Racial Effect"), CharacterBattleParticipantRaceEffect.CODEC, CharacterBattleParticipantRaceEffect::combine, CharacterBattleParticipantRaceEffect::extract);
    public static final BattleParticipantEffectType<CharacterBattleParticipantLevelEffect, CharacterBattleParticipantLevelEffect> BATTLE_PARTICIPANT_LEVEL_EFFECT = new BattleParticipantEffectType<>(new LiteralText("Level Effect"), CharacterBattleParticipantLevelEffect.CODEC, CharacterBattleParticipantLevelEffect::combine, CharacterBattleParticipantLevelEffect::extract);
    public static final BattleParticipantEffectType<CharacterDefaultEffect, CharacterDefaultEffect> BATTLE_PARTICIPANT_DEFAULT_EFFECT = new BattleParticipantEffectType<>(new LiteralText("You shouldn't be seeing this"), CharacterDefaultEffect.CODEC, CharacterDefaultEffect::combine, CharacterDefaultEffect::extract);

    public static void init() {
        Registry.register(BattleParticipantEffectTypes.REGISTRY, TBCExCharacter.createId("race"), BATTLE_PARTICIPANT_RACE_EFFECT);
        Registry.register(BattleParticipantEffectTypes.REGISTRY, TBCExCharacter.createId("level"), BATTLE_PARTICIPANT_LEVEL_EFFECT);
        Registry.register(BattleParticipantEffectTypes.REGISTRY, TBCExCharacter.createId("default"), BATTLE_PARTICIPANT_DEFAULT_EFFECT);
    }

    private CharacterBattleParticipantEffects() {
    }
}
