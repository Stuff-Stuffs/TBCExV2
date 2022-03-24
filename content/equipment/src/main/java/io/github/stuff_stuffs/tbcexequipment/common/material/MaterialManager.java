package io.github.stuff_stuffs.tbcexequipment.common.material;

import io.github.stuff_stuffs.tbcexequipment.common.material.attribute.MaterialAttributeMap;
import io.github.stuff_stuffs.tbcexequipment.common.material.attribute.MaterialAttributes;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public final class MaterialManager {
    private final Registry<Material> materialRegistry;
    private final Map<MaterialAttributes.Key<?>, MaterialAttributeMap<?>> attributeMapById;

    public MaterialManager(final Registry<Material> materialRegistry, final Map<MaterialAttributes.Key<?>, MaterialAttributeMap<?>> attributeMapById) {
        this.materialRegistry = materialRegistry;
        this.attributeMapById = attributeMapById;
    }

    public <T> Optional<T> getAttribute(final Material material, final MaterialAttributes.Key<T> key) {
        final MaterialAttributeMap<?> map = attributeMapById.get(key);
        if (map == null) {
            return Optional.empty();
        }
        final MaterialAttributeMap<T> casted = (MaterialAttributeMap<T>) map;
        return Optional.of(casted.get(material, materialRegistry));
    }

    public <T> Optional<MaterialAttributeMap<T>> getAttributeMap(final MaterialAttributes.Key<T> key) {
        return Optional.ofNullable((MaterialAttributeMap<T>) attributeMapById.getOrDefault(key, null));
    }

    public Optional<Material> getMaterial(final Identifier id) {
        return Optional.ofNullable(materialRegistry.get(id));
    }

    public Optional<Collection<Material>> getTaggedMaterials(final TagKey<Material> tag) {
        return materialRegistry.getEntryList(tag).map(i -> i.stream().map(RegistryEntry::value).toList());
    }

    public PacketByteBuf toPacket() {
        final PacketByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(attributeMapById.size());
        for (final Map.Entry<MaterialAttributes.Key<?>, MaterialAttributeMap<?>> entry : attributeMapById.entrySet()) {
            buf.writeIdentifier(entry.getKey().getId());
            final NbtCompound wrapper = new NbtCompound();
            final Optional<NbtElement> result = MaterialAttributeMap.CODEC.encodeStart(NbtOps.INSTANCE, entry.getValue()).result();
            if (result.isEmpty()) {
                throw new TBCExException("Error while trying to sync MaterialAttributeMap");
            }
            wrapper.put("data", result.get());
            buf.writeNbt(wrapper);
        }
        return buf;
    }
}
