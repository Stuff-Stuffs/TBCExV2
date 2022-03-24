package io.github.stuff_stuffs.tbcexequipment.common.network;

import io.github.stuff_stuffs.tbcexequipment.common.TBCExEquipment;
import io.github.stuff_stuffs.tbcexequipment.common.material.MaterialManager;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public final class MaterialAttributeSyncSender {
    public static final Identifier IDENTIFIER = TBCExEquipment.createId("material_attribute_sync");

    public static void sync(final MaterialManager materialManager, final PacketSender sender) {
        final PacketByteBuf buf = materialManager.toPacket();
        sender.sendPacket(IDENTIFIER, buf);
    }

    private MaterialAttributeSyncSender() {
    }
}
