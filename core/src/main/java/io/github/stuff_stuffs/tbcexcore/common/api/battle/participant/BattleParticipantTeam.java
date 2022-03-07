package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant;

import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;

public final class BattleParticipantTeam {
    public static final Codec<BattleParticipantTeam> CODEC = Identifier.CODEC.xmap(BattleParticipantTeam::new, BattleParticipantTeam::getId);
    private final Identifier id;

    public BattleParticipantTeam(final Identifier id) {
        this.id = id;
    }

    public Identifier getId() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BattleParticipantTeam that)) {
            return false;
        }

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
