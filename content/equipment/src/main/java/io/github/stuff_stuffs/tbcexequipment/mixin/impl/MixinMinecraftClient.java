package io.github.stuff_stuffs.tbcexequipment.mixin.impl;

import io.github.stuff_stuffs.tbcexequipment.common.material.attribute.MaterialAttributeMap;
import io.github.stuff_stuffs.tbcexequipment.common.material.attribute.MaterialAttributes;
import io.github.stuff_stuffs.tbcexequipment.mixin.api.ClientMaterialManagerHolder;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Map;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient implements ClientMaterialManagerHolder {
    private Map<MaterialAttributes.Key<?>, MaterialAttributeMap<?>> tbcex$materialMaps = Map.of();

    @Override
    public Map<MaterialAttributes.Key<?>, MaterialAttributeMap<?>> tbcex$getMaterialManagerMaps() {
        return tbcex$materialMaps;
    }

    @Override
    public void tbcex$setMaterialManagerMaps(final Map<MaterialAttributes.Key<?>, MaterialAttributeMap<?>> maps) {
        tbcex$materialMaps = maps;
    }
}
