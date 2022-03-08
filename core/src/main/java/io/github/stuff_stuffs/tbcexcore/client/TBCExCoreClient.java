package io.github.stuff_stuffs.tbcexcore.client;

import io.github.stuff_stuffs.tbcexcore.client.network.BattlePlayerUpdateReceiver;
import io.github.stuff_stuffs.tbcexcore.client.network.BattleUpdateReceiver;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.api.ClientModInitializer;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public class TBCExCoreClient implements ClientModInitializer {
    private static final Map<UUID, BattleHandle> BATTLE_BY_PLAYER = new Object2ObjectOpenHashMap<>();

    @Override
    public void onInitializeClient() {
        BattleUpdateReceiver.init();
        BattlePlayerUpdateReceiver.init();
    }

    public static void setBattle(final UUID uuid, @Nullable final BattleHandle handle) {
        if (handle == null) {
            BATTLE_BY_PLAYER.remove(uuid);
        } else {
            BATTLE_BY_PLAYER.put(uuid, handle);
        }
    }

    public static @Nullable BattleHandle getBattle(final UUID uuid) {
        return BATTLE_BY_PLAYER.getOrDefault(uuid, null);
    }
}
