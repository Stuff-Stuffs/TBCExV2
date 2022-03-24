package io.github.stuff_stuffs.tbcexequipment.mixin.impl;

import com.google.common.collect.ImmutableMap;
import io.github.stuff_stuffs.tbcexequipment.common.TBCExEquipment;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DynamicRegistryManager.class)
public interface MixinDynamicRegistryManager {
    @Inject(at = @At("RETURN"), method = "method_30531()Lcom/google/common/collect/ImmutableMap;", cancellable = true)
    private static void inject(final CallbackInfoReturnable<ImmutableMap<RegistryKey<? extends Registry<?>>, DynamicRegistryManager.Info<?>>> cir) {
        final ImmutableMap.Builder<RegistryKey<? extends Registry<?>>, DynamicRegistryManager.Info<?>> builder = new ImmutableMap.Builder<>();
        builder.putAll(cir.getReturnValue());
        TBCExEquipment.registerRegistries(builder);
        cir.setReturnValue(builder.build());
    }
}
