package io.github.stuff_stuffs.tbcexcore.common.impl.battle.world;

import io.github.stuff_stuffs.tbcexcore.client.network.BattleUpdateRequestSender;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.Battle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleWorld;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.BattleAction;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.BattleImpl;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.BattleTimelineImpl;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import it.unimi.dsi.fastutil.longs.Long2ReferenceMap;
import it.unimi.dsi.fastutil.longs.Long2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;

public class ClientBattleWorldImpl implements BattleWorld {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Long2ReferenceMap<BattleImpl> battles;
    private final RegistryKey<World> worldKey;
    private final Set<BattleHandle> toRequest;

    public ClientBattleWorldImpl(final RegistryKey<World> worldKey) {
        this.worldKey = worldKey;
        battles = new Long2ReferenceOpenHashMap<>();
        toRequest = new ObjectOpenHashSet<>();
    }

    @Override
    public Battle getBattle(final BattleHandle handle) {
        if (!handle.getWorld().equals(worldKey)) {
            LOGGER.error("Tried to get battle from wrong world!");
            return null;
        }
        toRequest.add(handle);
        return battles.get(handle.getId());
    }

    public void pushNew(final BattleHandle handle, final BattleImpl impl) {
        if (!handle.getWorld().equals(worldKey)) {
            LOGGER.error("Tried to get battle from wrong world!");
        } else {
            battles.put(handle.getId(), impl);
        }
    }

    public void pushUpdate(final BattleHandle handle, final int size, final List<BattleAction> actions) {
        if (!handle.getWorld().equals(worldKey)) {
            LOGGER.error("Tried to get battle from wrong world!");
        } else {
            final BattleImpl battle = battles.get(handle.getId());
            if (battle == null) {
                throw new TBCExException("Tried to update non existent battle!");
            }
            final BattleTimelineImpl timeline = (BattleTimelineImpl) battle.getTimeline();
            timeline.trim(size);
            for (final BattleAction action : actions) {
                timeline.push(action);
            }
        }
    }

    public void tick() {
        for (final BattleHandle handle : toRequest) {
            final BattleImpl battle = battles.get(handle.getId());
            final int size;
            if (battle == null) {
                size = 0;
            } else {
                size = battle.getTimeline().getSize();
            }
            BattleUpdateRequestSender.send(handle, size);
        }
    }
}
