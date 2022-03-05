package io.github.stuff_stuffs.tbcexcore.client;

import io.github.stuff_stuffs.tbcexcore.client.network.BattleUpdateReceiver;
import net.fabricmc.api.ClientModInitializer;

public class TBCExCoreClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BattleUpdateReceiver.init();
    }
}
