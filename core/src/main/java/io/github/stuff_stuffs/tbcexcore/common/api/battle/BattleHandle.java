package io.github.stuff_stuffs.tbcexcore.common.api.battle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public final class BattleHandle {
    private static final Codec<RegistryKey<World>> WORLD_CODEC = RegistryKey.createCodec(Registry.WORLD_KEY);
    public static final Codec<BattleHandle> CODEC = RecordCodecBuilder.create(instance -> instance.group(WORLD_CODEC.fieldOf("world").forGetter(BattleHandle::getWorld), Codec.LONG.fieldOf("id").forGetter(BattleHandle::getId)).apply(instance, BattleHandle::new));
    private final RegistryKey<World> world;
    private final long id;

    public BattleHandle(final RegistryKey<World> world, final long id) {
        this.world = world;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public RegistryKey<World> getWorld() {
        return world;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final BattleHandle that)) {
            return false;
        }

        if (id != that.id) {
            return false;
        }
        return world.equals(that.world);
    }

    @Override
    public int hashCode() {
        int result = world.hashCode();
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }
}
