package io.github.stuff_stuffs.tbcexcore.common;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStats;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class TBCExCore implements ModInitializer {
    public static final String MOD_ID = "tbcexcore";

    @Override
    public void onInitialize() {
        BattleParticipantStats.init();
    }

    public static Identifier createId(final String path) {
        return new Identifier(MOD_ID, path);
    }
}
