package io.github.stuff_stuffs.tbcexutil.common.path;

import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import io.github.stuff_stuffs.tbcexutil.common.BattleParticipantBounds;
import io.github.stuff_stuffs.tbcexutil.common.WorldShapeCache;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public enum DiagonalMovements implements MovementType {
    NORTH_EAST(MovementTypes.WALK_NORTH_EAST) {
        @Override
        public @Nullable Movement modify(final BattleParticipantBounds bounds, final BlockPos pos, final Box pathBounds, final World world, final WorldShapeCache cache) {
            if (MovementType.doesCollideWith(bounds.offset(0, -1, 0), cache) && MovementType.doesCollideWith(bounds.offset(1, -1, -1), cache) && !MovementType.doesCollideWith(bounds.offset(1, 0, 0), cache) && !MovementType.doesCollideWith(bounds.offset(0, 0, -1), cache) && !MovementType.doesCollideWith(bounds.offset(1, 0, -1), cache)) {
                return create(pos, pos.add(1, 0, -1));
            }
            return null;
        }
    },
    NORTH_WEST(MovementTypes.WALK_NORTH_WEST) {
        @Override
        public @Nullable Movement modify(final BattleParticipantBounds bounds, final BlockPos pos, final Box pathBounds, final World world, final WorldShapeCache cache) {
            if (MovementType.doesCollideWith(bounds.offset(0, -1, 0), cache) && MovementType.doesCollideWith(bounds.offset(-1, -1, -1), cache) && !MovementType.doesCollideWith(bounds.offset(-1, 0, 0), cache) && !MovementType.doesCollideWith(bounds.offset(0, 0, -1), cache) && !MovementType.doesCollideWith(bounds.offset(-1, 0, -1), cache)) {
                return create(pos, pos.add(-1, 0, -1));
            }
            return null;
        }
    },
    SOUTH_EAST(MovementTypes.WALK_SOUTH_EAST) {
        @Override
        public @Nullable Movement modify(final BattleParticipantBounds bounds, final BlockPos pos, final Box pathBounds, final World world, final WorldShapeCache cache) {
            if (MovementType.doesCollideWith(bounds.offset(0, -1, 0), cache) && MovementType.doesCollideWith(bounds.offset(1, -1, 1), cache) && !MovementType.doesCollideWith(bounds.offset(1, 0, 0), cache) && !MovementType.doesCollideWith(bounds.offset(0, 0, 1), cache) && !MovementType.doesCollideWith(bounds.offset(1, 0, 1), cache)) {
                return create(pos, pos.add(1, 0, 1));
            }
            return null;
        }
    },
    SOUTH_WEST(MovementTypes.WALK_SOUTH_WEST) {
        @Override
        public @Nullable Movement modify(final BattleParticipantBounds bounds, final BlockPos pos, final Box pathBounds, final World world, final WorldShapeCache cache) {
            if (MovementType.doesCollideWith(bounds.offset(0, -1, 0), cache) && MovementType.doesCollideWith(bounds.offset(-1, -1, 1), cache) && !MovementType.doesCollideWith(bounds.offset(-1, 0, 0), cache) && !MovementType.doesCollideWith(bounds.offset(0, 0, 1), cache) && !MovementType.doesCollideWith(bounds.offset(-1, 0, 1), cache)) {
                return create(pos, pos.add(-1, 0, 1));
            }
            return null;
        }
    };

    private final MovementTypes.RegisteredMovementType movementType;

    DiagonalMovements(MovementTypes.RegisteredMovementType movementType) {
        this.movementType = movementType;
    }

    @Override
    public <T> T serialize(final DynamicOps<T> ops, final Movement movement) {
        final Simple simple = (Simple) movement;
        final RecordBuilder<T> builder = ops.mapBuilder();
        builder.add("start_pos", BlockPos.CODEC.encodeStart(ops, simple.start));
        builder.add("end_pos", BlockPos.CODEC.encodeStart(ops, simple.end));
        return builder.build(ops.empty()).getOrThrow(false, s -> {
            throw new RuntimeException(s);
        });
    }

    @Override
    public <T> Movement deserialize(final DynamicOps<T> ops, final T serialized) {
        final MapLike<T> mapLike = ops.getMap(serialized).getOrThrow(false, s -> {
            throw new RuntimeException(s);
        });
        final BlockPos startPos = BlockPos.CODEC.parse(ops, mapLike.get("start_pos")).getOrThrow(false, s -> {
            throw new RuntimeException(s);
        });
        final BlockPos endPos = BlockPos.CODEC.parse(ops, mapLike.get("end_pos")).getOrThrow(false, s -> {
            throw new RuntimeException(s);
        });
        return new Simple(startPos, endPos, movementType);
    }

    @Nullable Movement create(final BlockPos start, final BlockPos end) {
        return new Simple(start, end, movementType);
    }

    private static final class Simple implements Movement {
        private static final double LENGTH = Math.sqrt(2);
        private final BlockPos start;
        private final BlockPos end;
        private final BlockPos delta;
        private final MovementTypes.RegisteredMovementType type;

        private Simple(final BlockPos start, final BlockPos end, final MovementTypes.RegisteredMovementType type) {
            this.start = start;
            this.end = end;
            delta = end.subtract(start);
            this.type = type;
        }


        @Override
        public double getCost() {
            return LENGTH;
        }

        @Override
        public BlockPos getStartPos() {
            return start;
        }

        @Override
        public BlockPos getEndPos() {
            return end;
        }

        @Override
        public double getLength() {
            return LENGTH;
        }

        @Override
        public Vec3d interpolate(final Vec3d start, double t) {
            t = t / LENGTH;
            return start.add(delta.getX() * t, delta.getY() * t, delta.getZ() * t);
        }

        @Override
        public MovementTypes.RegisteredMovementType getType() {
            return type;
        }
    }
}
