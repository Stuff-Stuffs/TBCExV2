package io.github.stuff_stuffs.tbcexequipment.common;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import io.github.stuff_stuffs.tbcexequipment.common.material.Material;
import io.github.stuff_stuffs.tbcexequipment.common.material.MaterialManager;
import io.github.stuff_stuffs.tbcexequipment.common.material.attribute.MaterialAttributeMap;
import io.github.stuff_stuffs.tbcexequipment.common.material.attribute.MaterialAttributeTypes;
import io.github.stuff_stuffs.tbcexequipment.common.material.attribute.MaterialAttributes;
import io.github.stuff_stuffs.tbcexequipment.common.network.EquipmentWorldSyncSender;
import io.github.stuff_stuffs.tbcexequipment.common.part.EquipmentPartTypes;
import io.github.stuff_stuffs.tbcexequipment.mixin.api.EquipmentWorldViewHolder;
import io.github.stuff_stuffs.tbcexequipment.mixin.impl.MixinBuiltinRegistries;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class TBCExEquipment implements ModInitializer {
    private static final Gson GSON = new GsonBuilder().registerTypeHierarchyAdapter(AttributeValueList.class, new AttributeListAdapter()).create();
    public static final String MOD_ID = "tbcexequipment";

    @Override
    public void onInitialize() {
        MaterialAttributes.init();
        MaterialAttributeTypes.init();
        EquipmentPartTypes.init();
        final SimpleRegistry<Material> materials = new SimpleRegistry<>(Material.REGISTRY_KEY, Lifecycle.stable(), null);
        Registry.register(MixinBuiltinRegistries.getRoot(), Material.REGISTRY_KEY.getValue(), materials);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> EquipmentWorldSyncSender.sync(((EquipmentWorldViewHolder) server).tbcex$getMaterialManager(), ((EquipmentWorldViewHolder) server).tbcex$getPartManager(), sender));
    }

    public static MaterialManager load(final ResourceManager manager, final DynamicRegistryManager registryManager) {
        final Map<MaterialAttributes.Key<?>, MaterialAttributeMap<?>> built = new Object2ReferenceOpenHashMap<>();
        for (final MaterialAttributes.Key<?> key : MaterialAttributes.getKeys()) {
            built.put(key, build(key.getId(), createBuilder(key), manager));
        }
        return new MaterialManager(registryManager.get(Material.REGISTRY_KEY), built);
    }

    private static <T> MaterialAttributeMap.Builder<T> createBuilder(final MaterialAttributes.Key<T> key) {
        return MaterialAttributeMap.builder(key.getType(), key.getDefaultValue());
    }

    private static <T> MaterialAttributeMap<T> build(final Identifier id, final MaterialAttributeMap.Builder<T> builder, final ResourceManager manager) {
        final String startingPath = "tbcex_material/attribute/";
        try {
            final List<Resource> allResources = manager.getAllResources(new Identifier(id.getNamespace(), startingPath + id.getPath() + ".json"));
            for (final Resource resource : allResources) {
                try (final InputStreamReader reader = new InputStreamReader(new BufferedInputStream(resource.getInputStream()))) {
                    final AttributeValueList list = GSON.fromJson(reader, AttributeValueList.class);
                    for (final Map.Entry<Identifier, JsonElement> entry : list.values.entrySet()) {
                        builder.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        } catch (final FileNotFoundException e) {
            return builder.build(id);
        } catch (final Exception e) {
            throw new TBCExException("Error while loading material attributes", e);
        }
        return builder.build(id);
    }

    public static void registerRegistries(final ImmutableMap.Builder<RegistryKey<? extends Registry<?>>, DynamicRegistryManager.Info<?>> builder) {
        builder.put(Material.REGISTRY_KEY, createInfo(Material.REGISTRY_KEY, Material.CODEC, Material.CODEC));
    }

    private static <T> DynamicRegistryManager.Info<T> createInfo(final RegistryKey<? extends Registry<T>> registry, final Codec<T> entryCodec, @Nullable final Codec<T> networkEntryCodec) {
        return new DynamicRegistryManager.Info<>(registry, entryCodec, networkEntryCodec);
    }

    public static Identifier createId(final String path) {
        return new Identifier(MOD_ID, path);
    }

    private static final class AttributeValueList {
        private final Map<Identifier, JsonElement> values;

        private AttributeValueList(final Map<Identifier, JsonElement> values) {
            this.values = values;
        }
    }

    private static final class AttributeListAdapter implements JsonDeserializer<AttributeValueList> {

        @Override
        public AttributeValueList deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
            final Map<Identifier, JsonElement> values = new Object2ReferenceOpenHashMap<>();
            final JsonObject object = json.getAsJsonObject();
            for (final String s : object.keySet()) {
                values.put(new Identifier(s), object.get(s));
            }
            return new AttributeValueList(values);
        }
    }
}
