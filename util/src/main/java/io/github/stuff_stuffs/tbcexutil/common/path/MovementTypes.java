package io.github.stuff_stuffs.tbcexutil.common.path;

import com.mojang.serialization.Lifecycle;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public final class MovementTypes {
    public static final Registry<RegisteredMovementType> REGISTRY = FabricRegistryBuilder.from(new SimpleRegistry<>(RegistryKey.ofRegistry(new Identifier("tbcexutil", "movement_types")), Lifecycle.stable(), RegisteredMovementType::getReference)).buildAndRegister();
    public static final RegisteredMovementType FALL = new RegisteredMovementType(BasicMovements.FALL);
    public static final RegisteredMovementType WALK_NORTH = new RegisteredMovementType(BasicMovements.NORTH);
    public static final RegisteredMovementType WALK_EAST = new RegisteredMovementType(BasicMovements.EAST);
    public static final RegisteredMovementType WALK_SOUTH = new RegisteredMovementType(BasicMovements.SOUTH);
    public static final RegisteredMovementType WALK_WEST = new RegisteredMovementType(BasicMovements.WEST);

    public static final RegisteredMovementType WALK_NORTH_EAST = new RegisteredMovementType(DiagonalMovements.NORTH_EAST);
    public static final RegisteredMovementType WALK_SOUTH_EAST = new RegisteredMovementType(DiagonalMovements.SOUTH_EAST);
    public static final RegisteredMovementType WALK_SOUTH_WEST = new RegisteredMovementType(DiagonalMovements.SOUTH_WEST);
    public static final RegisteredMovementType WALK_NORTH_WEST = new RegisteredMovementType(DiagonalMovements.NORTH_WEST);

    public static final RegisteredMovementType JUMP_NORTH = new RegisteredMovementType(SimpleJumpMovements.NORTH_JUMP);
    public static final RegisteredMovementType JUMP_EAST = new RegisteredMovementType(SimpleJumpMovements.EAST_JUMP);
    public static final RegisteredMovementType JUMP_SOUTH = new RegisteredMovementType(SimpleJumpMovements.SOUTH_JUMP);
    public static final RegisteredMovementType JUMP_WEST = new RegisteredMovementType(SimpleJumpMovements.WEST_JUMP);

    public static final RegisteredMovementType FALL_NORTH = new RegisteredMovementType(SimpleJumpMovements.NORTH_FALL);
    public static final RegisteredMovementType FALL_EAST = new RegisteredMovementType(SimpleJumpMovements.EAST_FALL);
    public static final RegisteredMovementType FALL_SOUTH = new RegisteredMovementType(SimpleJumpMovements.SOUTH_FALL);
    public static final RegisteredMovementType FALL_WEST = new RegisteredMovementType(SimpleJumpMovements.WEST_FALL);

    public static final TagKey<RegisteredMovementType> WALK_TAG = TagKey.of(MovementTypes.REGISTRY.getKey(), new Identifier("tbcexutil", "walk"));
    public static final TagKey<RegisteredMovementType> JUMP_TAG = TagKey.of(MovementTypes.REGISTRY.getKey(), new Identifier("tbcexutil", "jump"));
    public static final TagKey<RegisteredMovementType> FALL_TAG = TagKey.of(MovementTypes.REGISTRY.getKey(), new Identifier("tbcexutil", "fall"));

    public static void init() {
        Registry.register(REGISTRY, new Identifier("tbcexutil", "fall"), FALL);
        Registry.register(REGISTRY, new Identifier("tbcexutil", "walk_north"), WALK_NORTH);
        Registry.register(REGISTRY, new Identifier("tbcexutil", "walk_east"), WALK_EAST);
        Registry.register(REGISTRY, new Identifier("tbcexutil", "walk_south"), WALK_SOUTH);
        Registry.register(REGISTRY, new Identifier("tbcexutil", "walk_west"), WALK_WEST);

        Registry.register(REGISTRY, new Identifier("tbcexutil", "walk_north_east"), WALK_NORTH_EAST);
        Registry.register(REGISTRY, new Identifier("tbcexutil", "walk_south_east"), WALK_SOUTH_EAST);
        Registry.register(REGISTRY, new Identifier("tbcexutil", "walk_south_west"), WALK_SOUTH_WEST);
        Registry.register(REGISTRY, new Identifier("tbcexutil", "walk_north_west"), WALK_NORTH_WEST);

        Registry.register(REGISTRY, new Identifier("tbcexutil", "jump_north"), JUMP_NORTH);
        Registry.register(REGISTRY, new Identifier("tbcexutil", "jump_east"), JUMP_EAST);
        Registry.register(REGISTRY, new Identifier("tbcexutil", "jump_south"), JUMP_SOUTH);
        Registry.register(REGISTRY, new Identifier("tbcexutil", "jump_west"), JUMP_WEST);

        Registry.register(REGISTRY, new Identifier("tbcexutil", "fall_north"), FALL_NORTH);
        Registry.register(REGISTRY, new Identifier("tbcexutil", "fall_east"), FALL_EAST);
        Registry.register(REGISTRY, new Identifier("tbcexutil", "fall_south"), FALL_SOUTH);
        Registry.register(REGISTRY, new Identifier("tbcexutil", "fall_west"), FALL_WEST);
    }

    public static final class RegisteredMovementType {
        private final MovementType delegate;
        private final RegistryEntry.Reference<RegisteredMovementType> reference;

        public RegisteredMovementType(final MovementType delegate) {
            this.delegate = delegate;
            reference = REGISTRY.createEntry(this);
        }

        public MovementType getDelegate() {
            return delegate;
        }

        public RegistryEntry.Reference<RegisteredMovementType> getReference() {
            return reference;
        }
    }

    private MovementTypes() {
    }
}
