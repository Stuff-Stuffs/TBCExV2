package io.github.stuff_stuffs.tbcexcore.mixin.impl;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.mixin.api.BattlePlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public class MixinServerPlayerInteractionManager {
    @Shadow
    @Final
    protected ServerPlayerEntity player;

    @Inject(at = @At("HEAD"), method = "changeGameMode")
    private void inject(final GameMode gameMode, final CallbackInfoReturnable<Boolean> cir) {
        final BattleHandle handle = ((BattlePlayerEntity) player).tbcex$getCurrentBattle();
        if (handle != null && gameMode != GameMode.SPECTATOR) {
            cir.setReturnValue(false);
        }
    }
}
