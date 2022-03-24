package io.github.stuff_stuffs.tbcexequipment.mixin.impl;

import io.github.stuff_stuffs.tbcexequipment.common.api.EquipmentWorldView;
import io.github.stuff_stuffs.tbcexequipment.common.material.MaterialManager;
import io.github.stuff_stuffs.tbcexequipment.mixin.api.MaterialManagerHolder;
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
        return ((MaterialManagerHolder) getServer()).tbcex$getMaterialManager();
    }
}
