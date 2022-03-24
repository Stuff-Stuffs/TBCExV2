package io.github.stuff_stuffs.tbcexequipment.mixin.impl;

import io.github.stuff_stuffs.tbcexequipment.common.api.EquipmentWorldView;
import io.github.stuff_stuffs.tbcexequipment.common.material.MaterialManager;
import io.github.stuff_stuffs.tbcexequipment.common.part.EquipmentPartManager;
import io.github.stuff_stuffs.tbcexequipment.mixin.api.EquipmentWorldViewHolder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerWorld.class)
public class MixinServerWorld implements EquipmentWorldView {
    @Shadow
    @Final
    private MinecraftServer server;

    @Override
    public MaterialManager tbcex$getMaterialManager() {
        return ((EquipmentWorldViewHolder) server).tbcex$getMaterialManager();
    }

    @Override
    public EquipmentPartManager tbcex$getPartManager() {
        return ((EquipmentWorldViewHolder) server).tbcex$getPartManager();
    }
}
