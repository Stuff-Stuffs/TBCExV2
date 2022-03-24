package io.github.stuff_stuffs.tbcexequipment.mixin.impl;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;
import io.github.stuff_stuffs.tbcexequipment.common.TBCExEquipment;
import io.github.stuff_stuffs.tbcexequipment.common.material.Material;
import io.github.stuff_stuffs.tbcexequipment.common.material.MaterialManager;
import io.github.stuff_stuffs.tbcexequipment.common.part.EquipmentPartManager;
import io.github.stuff_stuffs.tbcexequipment.mixin.api.EquipmentWorldViewHolder;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.SaveLoader;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.util.UserCache;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.Proxy;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer implements EquipmentWorldViewHolder {
    @Unique
    private MaterialManager tbcex$materialManager;
    @Unique
    private EquipmentPartManager tbcex$partManager;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void inject(final Thread serverThread, final LevelStorage.Session session, final ResourcePackManager dataPackManager, final SaveLoader saveLoader, final Proxy proxy, final DataFixer dataFixer, final MinecraftSessionService sessionService, final GameProfileRepository gameProfileRepo, final UserCache userCache, final WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, final CallbackInfo ci) {
        tbcex$materialManager = TBCExEquipment.load(saveLoader.resourceManager(), saveLoader.dynamicRegistryManager());
        tbcex$partManager = EquipmentPartManager.load(saveLoader.dynamicRegistryManager().get(Material.REGISTRY_KEY), saveLoader.resourceManager());
    }

    @Override
    public MaterialManager tbcex$getMaterialManager() {
        return tbcex$materialManager;
    }

    @Override
    public EquipmentPartManager tbcex$getPartManager() {
        return tbcex$partManager;
    }
}
