package io.github.stuff_stuffs.tbcextest.common;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipant;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.world.ServerBattleWorldImpl;
import io.github.stuff_stuffs.tbcexcore.mixin.api.BattleWorldHolder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

public class TBCExTest implements ModInitializer {
    public static final String MOD_ID = "tbcextest";

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> dispatcher.register(CommandManager.literal("createBattle").requires(source -> source.hasPermissionLevel(2)).executes(context -> {
            final ServerWorld world = context.getSource().getWorld();
            final ServerBattleWorldImpl battleWorld = (ServerBattleWorldImpl) ((BattleWorldHolder) world).tbcex$getBattleWorld();
            final BattleHandle handle = battleWorld.create();
            final Entity entity = context.getSource().getEntity();
            if (entity instanceof BattleParticipant participant) {
                battleWorld.tryJoin(participant, handle);
            }
            return 0;
        })));
    }

    public static Identifier createId(final String path) {
        return new Identifier(MOD_ID, path);
    }
}
