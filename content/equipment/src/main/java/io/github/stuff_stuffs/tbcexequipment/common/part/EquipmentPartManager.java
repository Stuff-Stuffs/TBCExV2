package io.github.stuff_stuffs.tbcexequipment.common.part;

import com.google.gson.*;
import io.github.stuff_stuffs.tbcexequipment.common.material.Material;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntryList;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;

public final class EquipmentPartManager {
    private static final Gson GSON = new GsonBuilder().registerTypeHierarchyAdapter(DecodedList.class, new Deserializer()).create();
    private final Registry<Material> materialRegistry;
    private final Map<EquipmentPartType, Set<TagKey<Material>>> acceptableTags;

    public EquipmentPartManager(final Registry<Material> materialRegistry, final Map<EquipmentPartType, Set<TagKey<Material>>> acceptableTags) {
        this.materialRegistry = materialRegistry;
        this.acceptableTags = acceptableTags;
    }

    public boolean isAcceptable(final Material material, final EquipmentPartType type) {
        return acceptableTags.get(type).stream().flatMap(materialTagKey -> materialRegistry.getEntryList(materialTagKey).stream()).flatMap(RegistryEntryList.ListBacked::stream).anyMatch(entry -> entry.value() == material);
    }

    public static EquipmentPartManager load(final Registry<Material> materialRegistry, final ResourceManager manager) {
        final String startingPath = "tbcex_part/acceptable/";
        final Collection<Identifier> resources = manager.findResources(startingPath, s -> s.endsWith(".json"));
        final Map<EquipmentPartType, Set<TagKey<Material>>> acceptableTags = new Reference2ObjectOpenHashMap<>();
        for (final Identifier resourceId : resources) {
            final String path = resourceId.getPath();
            final Identifier identifier = new Identifier(resourceId.getNamespace(), path.substring(startingPath.length(), path.length() - ".json".length()));
            final EquipmentPartType partType = EquipmentPartTypes.REGISTRY.get(identifier);
            if (partType == null) {
                continue;
            }
            try {
                final List<Resource> allResources = manager.getAllResources(resourceId);
                final List<DecodedList> decodedLists = new ArrayList<>();
                for (final Resource resource : allResources) {
                    try (final Reader reader = new InputStreamReader(new BufferedInputStream(resource.getInputStream()))) {
                        final DecodedList decodedList = GSON.fromJson(reader, DecodedList.class);
                        if (decodedList.replace) {
                            decodedLists.clear();
                        }
                        decodedLists.add(decodedList);
                    }
                }
                final Set<TagKey<Material>> materialTags = new ObjectOpenHashSet<>();
                for (final DecodedList decodedList : decodedLists) {
                    for (final Identifier id : decodedList.acceptableTagIds) {
                        materialTags.add(TagKey.of(Material.REGISTRY_KEY, id));
                    }
                }
                acceptableTags.put(partType, materialTags);
            } catch (final FileNotFoundException ignored) {
            } catch (final Exception e) {
                throw new TBCExException("Error while loading equipment part manager");
            }
        }
        return new EquipmentPartManager(materialRegistry, acceptableTags);
    }

    public void toPacket(PacketByteBuf buf) {
        buf.writeVarInt(acceptableTags.size());
        for (Map.Entry<EquipmentPartType, Set<TagKey<Material>>> entry : acceptableTags.entrySet()) {
            buf.writeIdentifier(EquipmentPartTypes.REGISTRY.getId(entry.getKey()));
            buf.writeVarInt(entry.getValue().size());
            for (TagKey<Material> key : entry.getValue()) {
                buf.writeIdentifier(key.id());
            }
        }
    }

    private static final class DecodedList {
        private final Set<Identifier> acceptableTagIds;
        private final boolean replace;

        private DecodedList(final Set<Identifier> acceptableTagIds, final boolean replace) {
            this.acceptableTagIds = acceptableTagIds;
            this.replace = replace;
        }
    }

    private static final class Deserializer implements JsonDeserializer<DecodedList> {
        @Override
        public DecodedList deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
            final JsonObject object = json.getAsJsonObject();
            boolean replace = false;
            if (object.has("replace")) {
                replace = object.get("replace").getAsBoolean();
            }
            final Set<Identifier> tagIds = new ObjectOpenHashSet<>();
            final JsonArray tags = object.getAsJsonArray("tags");
            for (final JsonElement tag : tags) {
                tagIds.add(new Identifier(tag.getAsString()));
            }
            return new DecodedList(tagIds, replace);
        }
    }
}
