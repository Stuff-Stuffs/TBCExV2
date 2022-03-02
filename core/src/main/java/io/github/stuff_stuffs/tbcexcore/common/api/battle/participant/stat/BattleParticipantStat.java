package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryEntry;

public final class BattleParticipantStat {
    private final RegistryEntry.Reference<BattleParticipantStat> entry;

    public BattleParticipantStat() {
        entry = BattleParticipantStats.REGISTRY.createEntry(this);
    }

    public RegistryEntry.Reference<BattleParticipantStat> getEntry() {
        return entry;
    }

    @Override
    public String toString() {
        final Identifier id = BattleParticipantStats.REGISTRY.getId(this);
        if (id == null) {
            return "Unregistered BattleParticipantStat";
        }
        return "BattleParticipantStat{" + id + "}";
    }
}
