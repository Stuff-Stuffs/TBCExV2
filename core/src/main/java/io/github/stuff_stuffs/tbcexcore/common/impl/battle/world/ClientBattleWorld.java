package io.github.stuff_stuffs.tbcexcore.common.impl.battle.world;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.Battle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleWorld;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.BattleImpl;
import it.unimi.dsi.fastutil.longs.Long2ReferenceMap;
import it.unimi.dsi.fastutil.longs.Long2ReferenceOpenHashMap;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientBattleWorld implements BattleWorld {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Long2ReferenceMap<BattleImpl> battles;
    private final RegistryKey<World> worldKey;

    public ClientBattleWorld(final RegistryKey<World> worldKey) {
        this.worldKey = worldKey;
        battles = new Long2ReferenceOpenHashMap<>();
    }

    @Override
    public Battle getBattle(final BattleHandle handle) {
        if (!handle.getWorld().equals(worldKey)) {
            LOGGER.error("Tried to get battle from wrong world!");
            return null;
        }
        return battles.get(handle.getId());
    }

    public void pushUpdate(final BattleHandle handle, final BattleImpl impl) {
        if (!handle.getWorld().equals(worldKey)) {
            LOGGER.error("Tried to get battle from wrong world!");
        } else {
            battles.put(handle.getId(), impl);
        }
    }
}
