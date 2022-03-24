package io.github.stuff_stuffs.tbcexequipment.common.material.attribute;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryEntry;

import java.util.Optional;

public final class MaterialAttributeType<T> {
    private final Codec<T> codec;
    private final RegistryEntry.Reference<MaterialAttributeType<?>> reference;

    public MaterialAttributeType(final Codec<T> codec) {
        this.codec = codec;
        reference = MaterialAttributeTypes.REGISTRY.createEntry(this);
    }

    public RegistryEntry.Reference<MaterialAttributeType<?>> getReference() {
        return reference;
    }

    public Codec<T> getCodec() {
        return codec;
    }

    public <K> T decode(final DynamicOps<K> ops, final K value) {
        final Optional<T> parse = codec.parse(ops, value).result();
        if (parse.isEmpty()) {
            throw new TBCExException("Error while decoding material attribute with type: " + MaterialAttributeTypes.REGISTRY.getId(this));
        }
        return parse.get();
    }

    @Override
    public String toString() {
        final Identifier id = MaterialAttributeTypes.REGISTRY.getId(this);
        if (id == null) {
            return "Unregistered MaterialAttributeType";
        }
        return "MaterialAttributeType{" + id + "}";
    }
}
