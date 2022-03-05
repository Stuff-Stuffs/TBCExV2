package io.github.stuff_stuffs.tbcexcore.common;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.BattleActionTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.effect.BattleEffectTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffectTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantEquipmentSlots;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.equipment.BattleParticipantEquipmentTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleItemTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStats;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class TBCExCore implements ModInitializer {
    public static final String MOD_ID = "tbcexcore";

    @Override
    public void onInitialize() {
        BattleParticipantStats.init();
        BattleParticipantEffectTypes.init();
        BattleItemTypes.init();
        BattleParticipantEquipmentSlots.init();
        BattleParticipantEquipmentTypes.init();
        BattleEffectTypes.init();
        BattleActionTypes.init();
    }

    public static Identifier createId(final String path) {
        return new Identifier(MOD_ID, path);
    }
}
