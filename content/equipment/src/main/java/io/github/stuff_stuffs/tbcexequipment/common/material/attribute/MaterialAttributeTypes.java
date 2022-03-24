package io.github.stuff_stuffs.tbcexequipment.common.material.attribute;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import io.github.stuff_stuffs.tbcexequipment.common.TBCExEquipment;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public final class MaterialAttributeTypes {
    public static final RegistryKey<Registry<MaterialAttributeType<?>>> REGISTRY_KEY = RegistryKey.ofRegistry(TBCExEquipment.createId("material_attribute_types"));
    public static final Registry<MaterialAttributeType<?>> REGISTRY = FabricRegistryBuilder.from(new SimpleRegistry<>(REGISTRY_KEY, Lifecycle.stable(), MaterialAttributeType::getReference)).buildAndRegister();
    public static final MaterialAttributeType<Double> DOUBLE_TYPE = new MaterialAttributeType<>(Codec.DOUBLE);
    public static final MaterialAttributeType<Integer> INTEGER_TYPE = new MaterialAttributeType<>(Codec.INT);

    public static void init() {
        Registry.register(REGISTRY, TBCExEquipment.createId("double"), DOUBLE_TYPE);
        Registry.register(REGISTRY, TBCExEquipment.createId("integer"), INTEGER_TYPE);
    }

    private MaterialAttributeTypes() {
    }
}
