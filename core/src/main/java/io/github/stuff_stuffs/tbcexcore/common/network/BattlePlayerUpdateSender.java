package io.github.stuff_stuffs.tbcexcore.common.network;

import io.github.stuff_stuffs.tbcexcore.common.TBCExCore;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.mixin.api.BattlePlayerEntity;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class BattlePlayerUpdateSender {
    public static final Identifier IDENTIFIER = TBCExCore.createId("battle_player_update");

    public static void send(final MinecraftServer server, @Nullable final ServerPlayerEntity sendTo) {
        final List<ServerPlayerEntity> dirty = new ArrayList<>();
        for (final ServerPlayerEntity entity : server.getPlayerManager().getPlayerList()) {
            if (((BattlePlayerEntity) entity).tbcex$isDirty()) {
                dirty.add(entity);
                if (sendTo == null) {
                    ((BattlePlayerEntity) entity).tbcex$setDirty(false);
                }
            }
        }
        if (dirty.isEmpty()) {
            return;
        }
        final PacketByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(dirty.size());
        for (final ServerPlayerEntity entity : dirty) {
            buf.writeUuid(entity.getUuid());
            final BattleHandle handle = ((BattlePlayerEntity) entity).tbcex$getCurrentBattle();
            final boolean present = handle != null;
            buf.writeBoolean(present);
            if (present) {
                final NbtCompound wrapper = new NbtCompound();
                final Optional<NbtElement> result = BattleHandle.CODEC.encodeStart(NbtOps.INSTANCE, handle).result();
                if (result.isEmpty()) {
                    throw new TBCExException("Error while serializing BattleHandle");
                }
                wrapper.put("handle", result.get());
                buf.writeNbt(wrapper);
            }
        }
        buf.retain();
        if (sendTo != null) {
            ServerPlayNetworking.send(sendTo, IDENTIFIER, buf);
        } else {
            for (final ServerPlayerEntity entity : server.getPlayerManager().getPlayerList()) {
                ServerPlayNetworking.send(entity, IDENTIFIER, buf);
            }
        }
        buf.release();
    }
}
