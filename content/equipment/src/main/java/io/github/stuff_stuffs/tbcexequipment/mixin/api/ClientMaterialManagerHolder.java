package io.github.stuff_stuffs.tbcexequipment.mixin.api;

import io.github.stuff_stuffs.tbcexequipment.common.material.attribute.MaterialAttributeMap;
import io.github.stuff_stuffs.tbcexequipment.common.material.attribute.MaterialAttributes;

import java.util.Map;

public interface ClientMaterialManagerHolder {
    Map<MaterialAttributes.Key<?>, MaterialAttributeMap<?>> tbcex$getMaterialManagerMaps();

    void tbcex$setMaterialManagerMaps(Map<MaterialAttributes.Key<?>, MaterialAttributeMap<?>> maps);
}
