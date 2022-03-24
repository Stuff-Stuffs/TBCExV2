package io.github.stuff_stuffs.tbcexequipment.client;

import io.github.stuff_stuffs.tbcexequipment.client.network.EquipmentWorldSyncReceiver;
import net.fabricmc.api.ClientModInitializer;

public class TBCExEquipmentClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EquipmentWorldSyncReceiver.init();
    }
}
