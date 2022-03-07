package io.github.stuff_stuffs.tbcexcore.common;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.BattleActionTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.effect.BattleEffectTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.event.BattleEvents;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.damage.BattleParticipantDamageTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffectTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.event.BattleParticipantEvents;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantEquipmentSlots;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.equipment.BattleParticipantEquipmentTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItemTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.restore.RestoreDataTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStats;
import io.github.stuff_stuffs.tbcexcore.common.network.BattleUpdateRequestHandler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class TBCExCore implements ModInitializer {
    public static final String MOD_ID = "tbcexcore";

    @Override
    public void onInitialize() {
        BattleParticipantStats.init();
        BattleParticipantEffectTypes.init();
        BattleParticipantItemTypes.init();
        BattleParticipantEquipmentSlots.init();
        BattleParticipantEquipmentTypes.init();
        BattleEffectTypes.init();
        BattleActionTypes.init();
        BattleUpdateRequestHandler.init();
        RestoreDataTypes.init();
        BattleEvents.init();
        BattleParticipantEvents.init();
        BattleParticipantDamageTypes.init();
    }

    public static Identifier createId(final String path) {
        return new Identifier(MOD_ID, path);
    }
}
