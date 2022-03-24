package io.github.stuff_stuffs.tbcexequipment.client;

import io.github.stuff_stuffs.tbcexequipment.client.network.MaterialAttributeSyncReceiver;
import net.fabricmc.api.ClientModInitializer;

public class TBCExEquipmentClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MaterialAttributeSyncReceiver.init();
    }
}
