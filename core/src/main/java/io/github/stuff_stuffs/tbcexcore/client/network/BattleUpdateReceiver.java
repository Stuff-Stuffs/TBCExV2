package io.github.stuff_stuffs.tbcexcore.client.network;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.BattleImpl;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.world.ClientBattleWorld;
import io.github.stuff_stuffs.tbcexcore.common.network.BattleUpdateRequestHandler;
import io.github.stuff_stuffs.tbcexcore.mixin.api.BattleWorldHolder;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import io.netty.buffer.ByteBufInputStream;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;

import java.io.IOException;
import java.util.Optional;

public final class BattleUpdateReceiver {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(BattleUpdateRequestHandler.UPDATE_SEND_CHANNEL, BattleUpdateReceiver::receive);
    }

    private static void receive(final MinecraftClient client, final ClientPlayNetworkHandler handler, final PacketByteBuf buf, final PacketSender sender) {
        try (final ByteBufInputStream input = new ByteBufInputStream(buf)) {
            final NbtCompound compound = NbtIo.readCompressed(input);
            if (compound.contains("handle")) {
                final BattleHandle handle = BattleHandle.CODEC.parse(NbtOps.INSTANCE, compound.get("handle")).result().orElseThrow(() -> new TBCExException("Error while deserializing BattleHandle"));
                final Optional<BattleImpl> result = BattleImpl.CODEC.parse(NbtOps.INSTANCE, compound.get("data")).result();
                if (result.isPresent()) {
                    final BattleImpl battle = result.get();
                    final ClientBattleWorld clientBattleWorld = (ClientBattleWorld) ((BattleWorldHolder) client.world).tbcex$getBattleWorld();
                    client.execute(() -> clientBattleWorld.pushUpdate(handle, battle));
                } else {
                    throw new TBCExException("Error while deserializing battle update!");
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private BattleUpdateReceiver() {
    }
}
