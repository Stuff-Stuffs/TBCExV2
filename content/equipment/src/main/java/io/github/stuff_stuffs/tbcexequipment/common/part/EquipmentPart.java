package io.github.stuff_stuffs.tbcexequipment.common.part;

import io.github.stuff_stuffs.tbcexequipment.common.material.Material;

public final class EquipmentPart {
    private final EquipmentPartType type;
    private final Material material;

    public EquipmentPart(EquipmentPartType type, Material material) {
        this.type = type;
        this.material = material;
    }
}
