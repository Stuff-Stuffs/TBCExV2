package io.github.stuff_stuffs.tbcexequipment.common.material;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexequipment.common.TBCExEquipment;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public final class Material {
    public static final RegistryKey<Registry<Material>> REGISTRY_KEY = RegistryKey.ofRegistry(TBCExEquipment.createId("materials"));
    public static final Codec<Material> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.STRING.fieldOf("translationKey").forGetter(mat -> mat.translationKey)).apply(instance, Material::new));
    private final String translationKey;

    public Material(final String translationKey) {
        this.translationKey = translationKey;
    }

    public Text getName() {
        return new TranslatableText(translationKey);
    }

    @Override
    public String toString() {
        return "EquipmentMaterial{" +
                "translationKey='" + translationKey + '\'' +
                '}';
    }
}
