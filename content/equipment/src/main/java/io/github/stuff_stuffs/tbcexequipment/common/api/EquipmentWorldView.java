package io.github.stuff_stuffs.tbcexequipment.common.api;

import io.github.stuff_stuffs.tbcexequipment.common.material.MaterialManager;
import io.github.stuff_stuffs.tbcexequipment.common.part.EquipmentPartManager;

public interface EquipmentWorldView {
    MaterialManager tbcex$getMaterialManager();

    EquipmentPartManager tbcex$getPartManager();
}
