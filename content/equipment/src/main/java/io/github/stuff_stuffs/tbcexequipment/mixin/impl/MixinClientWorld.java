package io.github.stuff_stuffs.tbcexequipment.mixin.impl;

import io.github.stuff_stuffs.tbcexequipment.common.api.EquipmentWorldView;
import io.github.stuff_stuffs.tbcexequipment.common.material.Material;
import io.github.stuff_stuffs.tbcexequipment.common.material.MaterialManager;
import io.github.stuff_stuffs.tbcexequipment.mixin.api.ClientMaterialManagerHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientWorld.class)
public abstract class MixinClientWorld implements EquipmentWorldView {
    @Shadow
    public abstract DynamicRegistryManager getRegistryManager();

    @Shadow
    @Final
    private MinecraftClient client;
    private MaterialManager tbcex$materialManager = null;

    @Override
    public MaterialManager tbcex$getMaterialManager() {
        if (tbcex$materialManager == null) {
            tbcex$materialManager = new MaterialManager(getRegistryManager().get(Material.REGISTRY_KEY), ((ClientMaterialManagerHolder) client).tbcex$getMaterialManagerMaps());
        }
        return tbcex$materialManager;
    }
}
