package io.github.stuff_stuffs.tbcexcore.common.network;

import io.github.stuff_stuffs.tbcexcore.client.network.BattleActionAttemptSender;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.Battle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleTimeline;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.BattleAction;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;
import io.github.stuff_stuffs.tbcexcore.mixin.api.BattleWorldHolder;
import io.github.stuff_stuffs.tbcexutil.common.CodecUtil;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public final class BattleActionAttemptReceiver {
    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(BattleActionAttemptSender.IDENTIFIER, BattleActionAttemptReceiver::receive);
    }

    private static void receive(final MinecraftServer server, final ServerPlayerEntity entity, final ServerPlayNetworkHandler handler, final PacketByteBuf buf, final PacketSender sender) {
        final NbtCompound wrapper = buf.readNbt();
        final BattleHandle handle = CodecUtil.get(BattleHandle.CODEC.parse(NbtOps.INSTANCE, wrapper.get("handle")));
        final BattleAction action = CodecUtil.get(BattleAction.CODEC.parse(NbtOps.INSTANCE, wrapper.get("action")));
        final Battle battle = ((BattleWorldHolder) entity.world).tbcex$getBattleWorld().getBattle(handle);
        if (battle == null || !new BattleParticipantHandle(handle, entity.getUuid()).equals(battle.getState().getCurrentTurnParticipant())) {
            entity.sendMessage(new LiteralText("Could not perform action, it is not your turn!"), true);
        } else {
            ((BattleTimeline)battle.getTimeline()).push(action, new Tracer<>(ActionTrace.TRACE, i -> false));
        }
    }

    private BattleActionAttemptReceiver() {
    }
}
