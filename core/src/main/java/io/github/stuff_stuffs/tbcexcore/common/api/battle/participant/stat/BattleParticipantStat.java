package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryEntry;

public final class BattleParticipantStat {
    private final Text displayName;
    private final boolean hidden;
    private final boolean invertPositiveColour;
    private final RegistryEntry.Reference<BattleParticipantStat> entry;

    public BattleParticipantStat(Text displayName, boolean hidden, boolean invertPositiveColour) {
        this.displayName = displayName;
        this.hidden = hidden;
        this.invertPositiveColour = invertPositiveColour;
        entry = BattleParticipantStats.REGISTRY.createEntry(this);
    }

    public Text getDisplayName() {
        return displayName;
    }

    public boolean shouldInvertPositiveColour() {
        return invertPositiveColour;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isVisible() {
        return !isHidden();
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
