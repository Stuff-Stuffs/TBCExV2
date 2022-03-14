package io.github.stuff_stuffs.tbcexutil.common;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3i;

import java.util.stream.IntStream;

public final class BattleBounds {
    public static final Codec<BattleBounds> CODEC = Codec.INT_STREAM.xmap(BattleBounds::new, BattleBounds::boundStream);
    public final int minX;
    public final int minY;
    public final int minZ;
    public final int maxX;
    public final int maxY;
    public final int maxZ;
    private final Box box;

    private BattleBounds(final IntStream stream) {
        final int[] arr = stream.toArray();
        if (arr.length != 6) {
            throw new TBCExException("Too many points to form BattleBounds!");
        }
        minX = arr[0];
        minY = arr[1];
        minZ = arr[2];
        maxX = arr[3];
        maxY = arr[4];
        maxZ = arr[5];
        box = new Box(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public BattleBounds(final int minX, final int minY, final int minZ, final int maxX, final int maxY, final int maxZ) {
        if (minX > maxX || minY > maxY || minZ > maxZ) {
            throw new TBCExException("BattleBounds with reversed min/max!");
        }
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        box = new Box(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public BattleBounds setBase(final int x, final int y, final int z) {
        return new BattleBounds(x, y, z, getXLength() + x, getYLength() + y, getZLength() + z);
    }

    public boolean isIn(final Vec3i vec) {
        return isIn(vec.getX(), vec.getY(), vec.getZ());
    }

    public boolean isIn(final int x, final int y, final int z) {
        return (minX <= x && x < maxX) && (minY <= y && y < maxY) && (minZ <= z && z < maxZ);
    }

    public BattleBounds offset(final Vec3i vec) {
        return offset(vec.getX(), vec.getY(), vec.getZ());
    }

    public BattleBounds offset(final int x, final int y, final int z) {
        return new BattleBounds(minX + x, minY + y, minZ + z, maxX + x, maxY + y, maxZ + z);
    }

    public int getXLength() {
        return maxX - minX;
    }

    public int getYLength() {
        return maxY - minY;
    }

    public int getZLength() {
        return maxZ - minZ;
    }

    private IntStream boundStream() {
        return IntStream.of(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public boolean isValid(final BattleParticipantBounds bounds) {
        return bounds.isFullyInside(box);
    }
}
