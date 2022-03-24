package io.github.stuff_stuffs.tbcexequipment.common.material.attribute;

import io.github.stuff_stuffs.tbcexequipment.common.TBCExEquipment;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Map;

public final class MaterialAttributes {
    private static final Map<Identifier, Key<?>> CACHE = new Object2ReferenceOpenHashMap<>();
    public static final Key<Double> EDGE_STABILITY = registerOrGet(TBCExEquipment.createId("edge_stability"), MaterialAttributeTypes.DOUBLE_TYPE, 0.0);

    public static <T> Key<T> registerOrGet(final Identifier id, final MaterialAttributeType<T> type, final T defaultValue) {
        final Key<?> key = CACHE.computeIfAbsent(id, identifier -> new Key<>(identifier, type, defaultValue));
        if (key.type != type || !defaultValue.equals(key.defaultValue)) {
            throw new TBCExException("Material attributes with same name but different types or defaultValues!");
        }
        return (Key<T>) key;
    }

    public static Collection<Key<?>> getKeys() {
        return CACHE.values();
    }

    public static void init() {
        registerOrGet(TBCExEquipment.createId("edge_stability"), MaterialAttributeTypes.DOUBLE_TYPE, 0.0);
    }

    public static final class Key<T> {
        private final Identifier id;
        private final MaterialAttributeType<T> type;
        private final T defaultValue;

        private Key(final Identifier id, final MaterialAttributeType<T> type, final T defaultValue) {
            this.id = id;
            this.type = type;
            this.defaultValue = defaultValue;
        }

        public Identifier getId() {
            return id;
        }

        public MaterialAttributeType<T> getType() {
            return type;
        }

        public T getDefaultValue() {
            return defaultValue;
        }
    }

    private MaterialAttributes() {
    }
}
