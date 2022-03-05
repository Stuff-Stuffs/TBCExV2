package io.github.stuff_stuffs.tbcexcore.client.network;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.BattleAction;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.BattleImpl;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.world.ClientBattleWorldImpl;
import io.github.stuff_stuffs.tbcexcore.common.network.BattleUpdateRequestHandler;
import io.github.stuff_stuffs.tbcexcore.mixin.api.BattleWorldHolder;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import io.netty.buffer.ByteBufInputStream;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.*;
import net.minecraft.network.PacketByteBuf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class BattleUpdateReceiver {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(BattleUpdateRequestHandler.UPDATE_SEND_CHANNEL, BattleUpdateReceiver::receive);
    }

    private static void receive(final MinecraftClient client, final ClientPlayNetworkHandler handler, final PacketByteBuf buf, final PacketSender sender) {
        final int size = buf.readInt();
        final ClientBattleWorldImpl clientBattleWorld = (ClientBattleWorldImpl) ((BattleWorldHolder) client.world).tbcex$getBattleWorld();
        try (final ByteBufInputStream input = new ByteBufInputStream(buf)) {
            final NbtCompound compound = NbtIo.readCompressed(input);
            final BattleHandle handle = BattleHandle.CODEC.parse(NbtOps.INSTANCE, compound.get("handle")).result().orElseThrow(() -> new TBCExException("Error while deserializing BattleHandle"));
            if (size == 0) {
                final Optional<BattleImpl> result = BattleImpl.CODEC.parse(NbtOps.INSTANCE, compound.get("data")).result();
                if (result.isPresent()) {
                    final BattleImpl battle = result.get();
                    client.execute(() -> clientBattleWorld.pushNew(handle, battle));
                } else {
                    throw new TBCExException("Error while deserializing battle update!");
                }
            } else {
                final NbtList list = compound.getList("data", NbtElement.COMPOUND_TYPE);
                final List<BattleAction> actions = new ArrayList<>(list.size());
                for (final NbtElement element : list) {
                    final Optional<BattleAction> result = BattleAction.CODEC.parse(NbtOps.INSTANCE, element).result();
                    if (result.isPresent()) {
                        actions.add(result.get());
                    } else {
                        throw new TBCExException("Error while deserializing battle action!");
                    }
                }
                client.execute(() -> clientBattleWorld.pushUpdate(handle, size, actions));
            }
        } catch (final IOException e) {
            throw new TBCExException("Exception while reading battle update buffer!", e);
        }
    }

    private BattleUpdateReceiver() {
    }
}
