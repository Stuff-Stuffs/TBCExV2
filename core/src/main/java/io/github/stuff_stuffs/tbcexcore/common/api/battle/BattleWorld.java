package io.github.stuff_stuffs.tbcexcore.common.api.battle;

import com.mojang.serialization.Codec;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleStateView;
import io.github.stuff_stuffs.tbcexutil.common.CodecUtil;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;

public interface BattleWorld {
    BattleStateView getBattle(BattleHandle handle);

    default NbtElement serializeHandle(final BattleHandle handle) {
        return CodecUtil.get(getBattleHandleCodec().encodeStart(NbtOps.INSTANCE, handle));
    }

    default BattleHandle deserializeHandle(final NbtElement element) {
        return CodecUtil.get(getBattleHandleCodec().parse(NbtOps.INSTANCE, element));
    }

    Codec<BattleHandle> getBattleHandleCodec();

    default NbtElement serializeParticipantHandle(final BattleParticipantHandle handle) {
        return CodecUtil.get(getBattleParticipantHandleCodec().encodeStart(NbtOps.INSTANCE, handle));
    }

    default BattleParticipantHandle deserializeParticipantHandle(final NbtElement element) {
        return CodecUtil.get(getBattleParticipantHandleCodec().parse(NbtOps.INSTANCE, element));
    }

    Codec<BattleParticipantHandle> getBattleParticipantHandleCodec();
}
