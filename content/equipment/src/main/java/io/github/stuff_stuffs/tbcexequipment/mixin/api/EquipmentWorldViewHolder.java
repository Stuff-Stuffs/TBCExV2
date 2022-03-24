package io.github.stuff_stuffs.tbcexequipment.mixin.api;

import io.github.stuff_stuffs.tbcexequipment.common.material.MaterialManager;
import io.github.stuff_stuffs.tbcexequipment.common.part.EquipmentPartManager;

public interface EquipmentWorldViewHolder {
    MaterialManager tbcex$getMaterialManager();

    EquipmentPartManager tbcex$getPartManager();
}
