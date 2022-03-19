package io.github.stuff_stuffs.tbcexcharacter.common;

import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.CharacterBattleParticipantItemCategories;
import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.CharacterBattleParticipantStats;
import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.effect.CharacterBattleParticipantEffects;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class TBCExCharacter implements ModInitializer {
    public static final String MOD_ID = "tbcex_character";

    @Override
    public void onInitialize() {
        CharacterBattleParticipantStats.init();
        CharacterBattleParticipantItemCategories.init();
        CharacterBattleParticipantEffects.init();
    }

    public static Identifier createId(final String path) {
        return new Identifier(MOD_ID, path);
    }
}
