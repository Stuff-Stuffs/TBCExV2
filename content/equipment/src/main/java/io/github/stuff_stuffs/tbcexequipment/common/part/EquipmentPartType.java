package io.github.stuff_stuffs.tbcexequipment.common.part;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryEntry;

public final class EquipmentPartType {
    private final Text name;
    private final RegistryEntry.Reference<EquipmentPartType> reference;

    public EquipmentPartType(final Text name) {
        this.name = name;
        reference = EquipmentPartTypes.REGISTRY.createEntry(this);
    }

    public Text getName() {
        return name;
    }

    public RegistryEntry.Reference<EquipmentPartType> getReference() {
        return reference;
    }

    @Override
    public String toString() {
        final Identifier id = EquipmentPartTypes.REGISTRY.getId(this);
        if (id == null) {
            return "Unregistered EquipmentPartType";
        }
        return "EquipmentPartType{" + id + "}";
    }
}
