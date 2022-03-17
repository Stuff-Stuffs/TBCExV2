package io.github.stuff_stuffs.tbcexgui.mixin;

import io.github.stuff_stuffs.tbcexgui.client.api.MouseLockableScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mouse.class)
public abstract class MixinMouse {
    @Shadow
    public abstract void lockCursor();

    @Shadow
    public abstract void unlockCursor();

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "updateMouse", at = @At("HEAD"))
    private void lockMouseIfNeeded(final CallbackInfo ci) {
        if (MinecraftClient.getInstance().currentScreen instanceof MouseLockableScreen lockableScreen) {
            if (lockableScreen.shouldLockMouse()) {
                lockCursor();
            } else {
                unlockCursor();
            }
        }
    }

    @Inject(method = "getX", at = @At("HEAD"), cancellable = true)
    private void centerX(final CallbackInfoReturnable<Double> cir) {
        if (client.currentScreen instanceof MouseLockableScreen lockable && lockable.shouldLockMouse()) {
            cir.setReturnValue(0.5 * client.getWindow().getWidth());
        }
    }

    @Inject(method = "getY", at = @At("HEAD"), cancellable = true)
    private void centerY(final CallbackInfoReturnable<Double> cir) {
        if (client.currentScreen instanceof MouseLockableScreen lockable && lockable.shouldLockMouse()) {
            cir.setReturnValue(0.5 * client.getWindow().getHeight());
        }
    }

    @Redirect(method = "lockCursor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private void hook(final MinecraftClient minecraftClient, final Screen screen) {
        if (!(minecraftClient.currentScreen instanceof MouseLockableScreen)) {
            minecraftClient.setScreen(screen);
        }
    }
}
