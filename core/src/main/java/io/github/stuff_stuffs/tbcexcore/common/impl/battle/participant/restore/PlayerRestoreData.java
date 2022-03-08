package io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.restore;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.restore.RestoreData;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.restore.RestoreDataType;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.restore.RestoreDataTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexcore.mixin.api.BattlePlayerEntity;
import io.github.stuff_stuffs.tbcexutil.common.CodecUtil;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.UUID;

public class PlayerRestoreData implements RestoreData {
    public static final Codec<PlayerRestoreData> CODEC = RecordCodecBuilder.create(instance -> instance.group(CodecUtil.UUID_CODEC.fieldOf("uuid").forGetter(data -> data.playerUuid)).apply(instance, PlayerRestoreData::new));
    public static final RestoreDataType.RestoreFunction<PlayerRestoreData> RESTORE_FUNCTION = new RestoreDataType.RestoreFunction<PlayerRestoreData>() {
        @Override
        public void restore(PlayerRestoreData data, BattleParticipantState state, World world) {
            if(world instanceof ServerWorld serverWorld) {
                final Entity entity = serverWorld.getEntity(data.playerUuid);
                if(entity instanceof BattlePlayerEntity player) {
                    player.tbcex$setCurrentBattle(null);
                }
            }
        }
    };
    private final UUID playerUuid;

    public PlayerRestoreData(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }

    @Override
    public RestoreDataType<?> getType() {
        return RestoreDataTypes.PLAYER_RESTORE_DATA_TYPE;
    }
}
