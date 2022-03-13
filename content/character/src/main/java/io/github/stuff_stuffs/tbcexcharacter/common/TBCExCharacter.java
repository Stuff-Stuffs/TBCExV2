package io.github.stuff_stuffs.tbcexcharacter.common;

import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.BattleParticipantItemCategories;
import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.BattleParticipantStats;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class TBCExCharacter implements ModInitializer {
    public static final String MOD_ID = "tbcex_character";

    @Override
    public void onInitialize() {
        BattleParticipantStats.init();
        BattleParticipantItemCategories.init();
    }

    public static Identifier createId(final String path) {
        return new Identifier(MOD_ID, path);
    }
}
