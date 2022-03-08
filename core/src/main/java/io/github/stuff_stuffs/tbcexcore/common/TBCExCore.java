package io.github.stuff_stuffs.tbcexcore.common;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.BattleActionTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.effect.BattleEffectTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.event.BattleEvents;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipantTeam;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.damage.BattleParticipantDamageTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffectTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.event.BattleParticipantEvents;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantEquipmentSlots;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.equipment.BattleParticipantEquipmentTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItemTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.restore.RestoreDataTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStats;
import io.github.stuff_stuffs.tbcexcore.common.network.BattlePlayerUpdateSender;
import io.github.stuff_stuffs.tbcexcore.common.network.BattleUpdateRequestHandler;
import io.github.stuff_stuffs.tbcexcore.mixin.api.BattlePlayerEntity;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;

public class TBCExCore implements ModInitializer {
    public static final String MOD_ID = "tbcexcore";
    //TODO custom player teams
    public static final BattleParticipantTeam PLAYER_TEAM = new BattleParticipantTeam(createId("player"));

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
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof ServerPlayerEntity player) {
                BattlePlayerUpdateSender.send(player.server, player);
            }
        });
        ServerTickEvents.END_SERVER_TICK.register(server -> BattlePlayerUpdateSender.send(server, null));
    }

    public static Identifier createId(final String path) {
        return new Identifier(MOD_ID, path);
    }

    public static void setupPlayerToJoinBattle(final ServerPlayerEntity entity, final BattleHandle handle) {
        if (((BattlePlayerEntity) entity).tbcex$getCurrentBattle() != null) {
            throw new TBCExException("Player already in battle, cannot join another");
        }
        ((BattlePlayerEntity) entity).tbcex$setCurrentBattle(handle);
        entity.changeGameMode(GameMode.SPECTATOR);
    }
}
