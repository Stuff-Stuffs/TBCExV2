package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryEntry;

public final class BattleParticipantEquipmentSlot {
    private final RegistryEntry.Reference<BattleParticipantEquipmentSlot> reference;

    public BattleParticipantEquipmentSlot() {
        reference = BattleParticipantEquipmentSlots.REGISTRY.createEntry(this);
    }

    public RegistryEntry.Reference<BattleParticipantEquipmentSlot> getReference() {
        return reference;
    }

    @Override
    public String toString() {
        final Identifier id = BattleParticipantEquipmentSlots.REGISTRY.getId(this);
        if (id == null) {
            return "Unregistered BattleParticipantEquipmentSlot";
        }
        return "BattleParticipantEquipmentSlot{" + id + "}";
    }
}
