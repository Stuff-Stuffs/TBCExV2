package io.github.stuff_stuffs.tbcexequipment.common.material.attribute;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import io.github.stuff_stuffs.tbcexequipment.common.material.Material;
import io.github.stuff_stuffs.tbcexutil.common.CodecUtil;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;
import java.util.stream.Collectors;

public final class MaterialAttributeMap<T> {
    public static final Codec<MaterialAttributeMap<?>> CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<MaterialAttributeMap<?>, T>> decode(final DynamicOps<T> ops, final T input) {
            final MapLike<T> map = CodecUtil.get(ops.getMap(input));
            final MaterialAttributeType<?> type = CodecUtil.get(MaterialAttributeTypes.REGISTRY.getCodec().parse(ops, map.get("type")));
            return DataResult.success(Pair.of(decode(type, map, ops), ops.empty()));
        }

        private <T, K> MaterialAttributeMap<T> decode(final MaterialAttributeType<T> type, final MapLike<K> map, final DynamicOps<K> ops) {
            final T defaultValue = type.decode(ops, map.get("default"));
            final MapLike<K> encodedValues = CodecUtil.get(ops.getMap(map.get("values")));
            final Map<Identifier, T> values = new Object2ObjectOpenHashMap<>();
            final Map<Identifier, T> collect = encodedValues.entries().map(p -> Pair.of(new Identifier(CodecUtil.get(ops.getStringValue(p.getFirst()))), type.decode(ops, p.getSecond()))).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
            values.putAll(collect);
            return new MaterialAttributeMap<>(type, values, defaultValue);
        }

        @Override
        public <T> DataResult<T> encode(final MaterialAttributeMap<?> input, final DynamicOps<T> ops, final T prefix) {
            return ops.mapBuilder().add(
                    "type", MaterialAttributeTypes.REGISTRY.getCodec().encodeStart(ops, input.type)
            ).add(
                    "default", input.encodeDefaultValue(ops)
            ).add(
                    "values", input.encodeValues(ops)
            ).build(prefix);
        }
    };

    private final MaterialAttributeType<T> type;
    private final Map<Identifier, T> values;
    private final T defaultValue;

    private MaterialAttributeMap(final MaterialAttributeType<T> type, final Map<Identifier, T> values, final T defaultValue) {
        this.type = type;
        this.values = values;
        this.defaultValue = defaultValue;
    }

    public boolean checkType(final MaterialAttributeType<?> type) {
        return this.type == type;
    }

    public T get(final Material material, final Registry<Material> materialRegistry) {
        return values.getOrDefault(materialRegistry.getId(material), defaultValue);
    }

    private <K> DataResult<K> encodeDefaultValue(final DynamicOps<K> ops) {
        return type.getCodec().encodeStart(ops, defaultValue);
    }

    private <K> DataResult<K> encodeValues(final DynamicOps<K> ops) {
        final RecordBuilder<K> mapBuilder = ops.mapBuilder();
        for (final Map.Entry<Identifier, T> entry : values.entrySet()) {
            mapBuilder.add(entry.getKey().toString(), type.getCodec().encodeStart(ops, entry.getValue()));
        }
        return mapBuilder.build(ops.empty());
    }

    public MaterialAttributeType<T> getType() {
        return type;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public static <T> Builder<T> builder(final MaterialAttributeType<T> type, final T defaultValue) {
        return new Builder<>(type, defaultValue);
    }

    public static final class Builder<T> {
        private final MaterialAttributeType<T> type;
        private final Map<Identifier, JsonElement> values;
        private final T defaultValue;

        private Builder(final MaterialAttributeType<T> type, final T defaultValue) {
            this.type = type;
            this.defaultValue = defaultValue;
            values = new Object2ObjectOpenHashMap<>();
        }

        public void put(final Identifier material, final JsonElement object) {
            values.put(material, object);
        }

        public MaterialAttributeMap<T> build(final Identifier id) {
            if (defaultValue == null) {
                throw new TBCExException("Error while building material attribute map: no default value for attribute map:" + id);
            }
            final Map<Identifier, T> decoded = new Object2ObjectOpenHashMap<>();
            for (final Map.Entry<Identifier, JsonElement> entry : values.entrySet()) {
                final T material = type.decode(JsonOps.INSTANCE, entry.getValue());
                decoded.put(entry.getKey(), material);
            }
            return new MaterialAttributeMap<>(type, decoded, defaultValue);
        }
    }
}
