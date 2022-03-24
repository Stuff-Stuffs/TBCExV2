package io.github.stuff_stuffs.tbcexequipment.mixin.impl;

import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.MutableRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BuiltinRegistries.class)
public interface MixinBuiltinRegistries {
    @Accessor(value = "ROOT")
    static MutableRegistry<MutableRegistry<?>> getRoot() {
        throw new AssertionError("Mixin did not apply");
    }
}
