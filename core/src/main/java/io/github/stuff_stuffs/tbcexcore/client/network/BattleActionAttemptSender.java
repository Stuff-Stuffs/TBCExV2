package io.github.stuff_stuffs.tbcexcore.client.network;

import io.github.stuff_stuffs.tbcexcore.common.TBCExCore;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.BattleAction;
import io.github.stuff_stuffs.tbcexutil.common.CodecUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public final class BattleActionAttemptSender {
    public static final Identifier IDENTIFIER = TBCExCore.createId("battle_action_attempt");

    public static void send(final BattleHandle handle, final BattleAction action) {
        final PacketByteBuf buf = PacketByteBufs.create();
        final NbtCompound wrapper = new NbtCompound();
        wrapper.put("handle", CodecUtil.get(BattleHandle.CODEC.encodeStart(NbtOps.INSTANCE, handle)));
        wrapper.put("action", CodecUtil.get(BattleAction.CODEC.encodeStart(NbtOps.INSTANCE, action)));
        buf.writeNbt(wrapper);
        ClientPlayNetworking.send(IDENTIFIER, buf);
    }

    private BattleActionAttemptSender() {
    }
}
