package io.github.stuff_stuffs.tbcexequipment.client.network;

import io.github.stuff_stuffs.tbcexequipment.common.material.attribute.MaterialAttributeMap;
import io.github.stuff_stuffs.tbcexequipment.common.material.attribute.MaterialAttributes;
import io.github.stuff_stuffs.tbcexequipment.common.network.MaterialAttributeSyncSender;
import io.github.stuff_stuffs.tbcexequipment.mixin.api.ClientMaterialManagerHolder;
import io.github.stuff_stuffs.tbcexutil.common.CodecUtil;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.Map;

public final class MaterialAttributeSyncReceiver {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(MaterialAttributeSyncSender.IDENTIFIER, MaterialAttributeSyncReceiver::receive);
    }

    private static void receive(final MinecraftClient client, final ClientPlayNetworkHandler handler, final PacketByteBuf buf, final PacketSender sender) {
        final int count = buf.readVarInt();
        final Map<Identifier, MaterialAttributeMap<?>> maps = new Object2ReferenceOpenHashMap<>();
        for (int i = 0; i < count; i++) {
            maps.put(buf.readIdentifier(), CodecUtil.get(MaterialAttributeMap.CODEC.parse(NbtOps.INSTANCE, buf.readNbt().get("data"))));
        }
        final Map<MaterialAttributes.Key<?>, MaterialAttributeMap<?>> keyed = new Object2ReferenceOpenHashMap<>();
        for (final Map.Entry<Identifier, MaterialAttributeMap<?>> entry : maps.entrySet()) {
            keyed.put(getKey(entry.getKey(), entry.getValue()), entry.getValue());
        }
        ((ClientMaterialManagerHolder) client).tbcex$setMaterialManagerMaps(keyed);
    }

    private static <T> MaterialAttributes.Key<T> getKey(final Identifier id, final MaterialAttributeMap<T> map) {
        return MaterialAttributes.registerOrGet(id, map.getType(), map.getDefaultValue());
    }

    private MaterialAttributeSyncReceiver() {
    }
}
