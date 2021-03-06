package io.github.stuff_stuffs.tbcexequipment.mixin.impl;

import io.github.stuff_stuffs.tbcexequipment.common.api.EquipmentWorldView;
import io.github.stuff_stuffs.tbcexequipment.common.material.MaterialManager;
import io.github.stuff_stuffs.tbcexequipment.common.part.EquipmentPartManager;
import io.github.stuff_stuffs.tbcexequipment.mixin.api.EquipmentWorldViewHolder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.ChunkRegion;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChunkRegion.class)
public abstract class MixinChunkRegion implements EquipmentWorldView {
    @Shadow
    @Nullable
    public abstract MinecraftServer getServer();

    @Override
    public MaterialManager tbcex$getMaterialManager() {
        return ((EquipmentWorldViewHolder) getServer()).tbcex$getMaterialManager();
    }

    @Override
    public EquipmentPartManager tbcex$getPartManager() {
        return ((EquipmentWorldViewHolder) getServer()).tbcex$getPartManager();
    }
}
