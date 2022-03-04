package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexutil.common.CodecUtil;

import java.util.UUID;

public final class BattleParticipantHandle {
    public static final Codec<BattleParticipantHandle> CODEC = RecordCodecBuilder.create(instance -> instance.group(BattleHandle.CODEC.fieldOf("parent").forGetter(BattleParticipantHandle::getParent), CodecUtil.UUID_CODEC.fieldOf("uuid").forGetter(BattleParticipantHandle::getUuid)).apply(instance, BattleParticipantHandle::new));
    private final BattleHandle parent;
    private final UUID uuid;

    public BattleParticipantHandle(final BattleHandle parent, final UUID uuid) {
        this.parent = parent;
        this.uuid = uuid;
    }

    public BattleHandle getParent() {
        return parent;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final BattleParticipantHandle that)) {
            return false;
        }

        if (!parent.equals(that.parent)) {
            return false;
        }
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        int result = parent.hashCode();
        result = 31 * result + uuid.hashCode();
        return result;
    }
}
