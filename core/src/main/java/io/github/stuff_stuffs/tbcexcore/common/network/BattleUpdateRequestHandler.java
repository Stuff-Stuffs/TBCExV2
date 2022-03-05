package io.github.stuff_stuffs.tbcexcore.common.network;

import io.github.stuff_stuffs.tbcexcore.client.network.BattleUpdateRequestSender;
import io.github.stuff_stuffs.tbcexcore.common.TBCExCore;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.world.ServerBattleWorldImpl;
import io.github.stuff_stuffs.tbcexcore.mixin.api.BattleWorldHolder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public final class BattleUpdateRequestHandler {
    public static final Identifier UPDATE_SEND_CHANNEL = TBCExCore.createId("battle_update_s2c");

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(BattleUpdateRequestSender.IDENTIFIER, BattleUpdateRequestHandler::receive);
    }

    private static void receive(final MinecraftServer server, final ServerPlayerEntity player, final ServerPlayNetworkHandler serverPlayNetworkHandler, final PacketByteBuf buf, final PacketSender sender) {
        final Identifier worldId = buf.readIdentifier();
        final long id = buf.readLong();
        final RegistryKey<World> worldKey = RegistryKey.of(Registry.WORLD_KEY, worldId);
        final BattleHandle handle = new BattleHandle(worldKey, id);
        server.execute(() -> {
            final ServerWorld world = server.getWorld(worldKey);
            final ServerBattleWorldImpl battleWorld = (ServerBattleWorldImpl) ((BattleWorldHolder) world).tbcex$getBattleWorld();
            final PacketByteBuf send = PacketByteBufs.create();
            battleWorld.writeBattle(handle, send);
            sender.sendPacket(UPDATE_SEND_CHANNEL, send);
        });
    }

    private BattleUpdateRequestHandler() {
    }
}
