package io.github.stuff_stuffs.tbcexequipment.mixin.impl;

import io.github.stuff_stuffs.tbcexequipment.common.api.EquipmentWorldView;
import net.minecraft.world.RegistryWorldView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RegistryWorldView.class)
public interface MixinRegistryWorldView extends EquipmentWorldView {

}
