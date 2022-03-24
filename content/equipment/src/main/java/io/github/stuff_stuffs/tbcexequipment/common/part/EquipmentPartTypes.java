package io.github.stuff_stuffs.tbcexequipment.common.part;

import com.mojang.serialization.Lifecycle;
import io.github.stuff_stuffs.tbcexequipment.common.TBCExEquipment;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.text.LiteralText;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public final class EquipmentPartTypes {
    public static final RegistryKey<Registry<EquipmentPartType>> REGISTRY_KEY = RegistryKey.ofRegistry(TBCExEquipment.createId("part"));
    public static final Registry<EquipmentPartType> REGISTRY = FabricRegistryBuilder.from(new SimpleRegistry<>(REGISTRY_KEY, Lifecycle.stable(), EquipmentPartType::getReference)).buildAndRegister();
    public static final EquipmentPartType TEST_PART = new EquipmentPartType(new LiteralText("test"));

    public static void init() {
        Registry.register(REGISTRY, TBCExEquipment.createId("test"), TEST_PART);
    }

    private EquipmentPartTypes() {
    }
}
