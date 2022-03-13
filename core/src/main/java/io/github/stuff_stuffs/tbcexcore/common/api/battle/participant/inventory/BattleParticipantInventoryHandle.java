package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;

public final class BattleParticipantInventoryHandle {
    public static final Codec<BattleParticipantInventoryHandle> CODEC = RecordCodecBuilder.create(instance -> instance.group(BattleParticipantHandle.CODEC.fieldOf("parent").forGetter(handle -> handle.parent), Codec.LONG.fieldOf("id").forGetter(handle -> handle.id)).apply(instance, BattleParticipantInventoryHandle::new));
    private final BattleParticipantHandle parent;
    private final long id;

    public BattleParticipantInventoryHandle(final BattleParticipantHandle parent, final long id) {
        this.parent = parent;
        this.id = id;
    }

    public BattleParticipantHandle getParent() {
        return parent;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BattleParticipantInventoryHandle that)) {
            return false;
        }

        if (id != that.id) {
            return false;
        }
        return parent.equals(that.parent);
    }

    @Override
    public int hashCode() {
        int result = parent.hashCode();
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }
}
