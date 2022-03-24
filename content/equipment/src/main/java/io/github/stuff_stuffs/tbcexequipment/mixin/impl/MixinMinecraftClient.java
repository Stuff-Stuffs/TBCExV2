package io.github.stuff_stuffs.tbcexequipment.mixin.impl;

import io.github.stuff_stuffs.tbcexequipment.common.material.Material;
import io.github.stuff_stuffs.tbcexequipment.common.material.attribute.MaterialAttributeMap;
import io.github.stuff_stuffs.tbcexequipment.common.material.attribute.MaterialAttributes;
import io.github.stuff_stuffs.tbcexequipment.common.part.EquipmentPartType;
import io.github.stuff_stuffs.tbcexequipment.mixin.api.ClientEquipmetWorldViewHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Map;
import java.util.Set;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient implements ClientEquipmetWorldViewHolder {
    private Map<MaterialAttributes.Key<?>, MaterialAttributeMap<?>> tbcex$materialMaps = Map.of();
    private Map<EquipmentPartType, Set<TagKey<Material>>> tbcex$acceptableTags = Map.of();

    @Override
    public Map<MaterialAttributes.Key<?>, MaterialAttributeMap<?>> tbcex$getMaterialManagerMaps() {
        return tbcex$materialMaps;
    }

    @Override
    public void tbcex$setMaterialManagerMaps(final Map<MaterialAttributes.Key<?>, MaterialAttributeMap<?>> maps) {
        tbcex$materialMaps = maps;
    }

    @Override
    public void tbcex$setAcceptableTags(final Map<EquipmentPartType, Set<TagKey<Material>>> map) {
        tbcex$acceptableTags = map;
    }

    @Override
    public Map<EquipmentPartType, Set<TagKey<Material>>> tbcex$getAcceptableTags() {
        return tbcex$acceptableTags;
    }
}
