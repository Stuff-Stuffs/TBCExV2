package io.github.stuff_stuffs.tbcexcore.mixin.impl;

import io.github.stuff_stuffs.tbcexcore.client.render.screen.BattleMenuScreen;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.mixin.api.BattlePlayerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {
    @Shadow
    public abstract void setScreen(@Nullable Screen screen);

    @Redirect(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private void redirect(final MinecraftClient client, final Screen screen) {
        if (screen instanceof InventoryScreen) {
            final PlayerEntity player = client.player;
            final BattleHandle battleHandle = ((BattlePlayerEntity) player).tbcex$getCurrentBattle();
            if (battleHandle != null) {
                setScreen(new BattleMenuScreen(player));
                return;
            }
        }
        setScreen(screen);
    }
}
