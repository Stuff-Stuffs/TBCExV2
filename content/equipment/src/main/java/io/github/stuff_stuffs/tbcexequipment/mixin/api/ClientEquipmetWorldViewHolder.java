package io.github.stuff_stuffs.tbcexequipment.mixin.api;

import io.github.stuff_stuffs.tbcexequipment.common.material.Material;
import io.github.stuff_stuffs.tbcexequipment.common.material.attribute.MaterialAttributeMap;
import io.github.stuff_stuffs.tbcexequipment.common.material.attribute.MaterialAttributes;
import io.github.stuff_stuffs.tbcexequipment.common.part.EquipmentPartType;
import net.minecraft.tag.TagKey;

import java.util.Map;
import java.util.Set;

public interface ClientEquipmetWorldViewHolder {
    Map<MaterialAttributes.Key<?>, MaterialAttributeMap<?>> tbcex$getMaterialManagerMaps();

    void tbcex$setMaterialManagerMaps(Map<MaterialAttributes.Key<?>, MaterialAttributeMap<?>> maps);

    void tbcex$setAcceptableTags(Map<EquipmentPartType, Set<TagKey<Material>>> map);

    Map<EquipmentPartType, Set<TagKey<Material>>> tbcex$getAcceptableTags();
}
