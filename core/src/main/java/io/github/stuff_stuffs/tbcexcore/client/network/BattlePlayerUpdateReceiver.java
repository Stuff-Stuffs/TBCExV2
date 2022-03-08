package io.github.stuff_stuffs.tbcexcore.client.network;

import io.github.stuff_stuffs.tbcexcore.client.TBCExCoreClient;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.common.network.BattlePlayerUpdateSender;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;

import java.util.Optional;
import java.util.UUID;

public final class BattlePlayerUpdateReceiver {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(BattlePlayerUpdateSender.IDENTIFIER, BattlePlayerUpdateReceiver::receive);
    }

    private static void receive(final MinecraftClient client, final ClientPlayNetworkHandler handler, final PacketByteBuf buf, final PacketSender sender) {
        final int updateCount = buf.readVarInt();
        for (int i = 0; i < updateCount; i++) {
            final UUID uuid = buf.readUuid();
            final boolean present = buf.readBoolean();
            if (present) {
                final NbtCompound compound = buf.readNbt();
                final Optional<BattleHandle> result = BattleHandle.CODEC.parse(NbtOps.INSTANCE, compound.get("handle")).result();
                if (result.isEmpty()) {
                    throw new TBCExException("Error while deserializing BattleHandle");
                }
                TBCExCoreClient.setBattle(uuid, result.get());
            } else {
                TBCExCoreClient.setBattle(uuid, null);
            }
        }
    }

    private BattlePlayerUpdateReceiver() {
    }
}
