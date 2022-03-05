package io.github.stuff_stuffs.tbcexcore.mixin.impl;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleWorld;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.world.ClientBattleWorldImpl;
import io.github.stuff_stuffs.tbcexcore.mixin.api.BattleWorldHolder;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public class MixinClientWorld implements BattleWorldHolder {
    private ClientBattleWorldImpl battleWorld;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(final ClientPlayNetworkHandler netHandler, final ClientWorld.Properties properties, final RegistryKey<World> registryRef, final RegistryEntry<DimensionType> registryEntry, final int loadDistance, final int simulationDistance, final Supplier<Profiler> profiler, final WorldRenderer worldRenderer, final boolean debugWorld, final long seed, final CallbackInfo ci) {
        battleWorld = new ClientBattleWorldImpl(registryRef);
    }

    @Inject(at = @At("RETURN"), method = "tick")
    private void tick(final BooleanSupplier shouldKeepTicking, final CallbackInfo ci) {
        battleWorld.tick();
    }

    @Override
    public BattleWorld tbcex$getBattleWorld() {
        return battleWorld;
    }
}
