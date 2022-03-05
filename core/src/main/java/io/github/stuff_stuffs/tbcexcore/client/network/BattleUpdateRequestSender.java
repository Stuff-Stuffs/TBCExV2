package io.github.stuff_stuffs.tbcexcore.client.network;

import io.github.stuff_stuffs.tbcexcore.common.TBCExCore;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public final class BattleUpdateRequestSender {
    public static final Identifier IDENTIFIER = TBCExCore.createId("battle_update_request");

    public static void send(final BattleHandle handle, final int currentSize) {
        final PacketByteBuf buf = PacketByteBufs.create();
        buf.writeIdentifier(handle.getWorld().getValue());
        buf.writeLong(handle.getId());
        buf.writeInt(currentSize);
        ClientPlayNetworking.send(IDENTIFIER, buf);
    }

    private BattleUpdateRequestSender() {
    }
}
