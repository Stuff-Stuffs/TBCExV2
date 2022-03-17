package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import io.github.stuff_stuffs.tbcexutil.common.path.Path;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.doubles.DoubleListIterator;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public final class BattlePath {
    public static final Codec<BattlePath> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.list(BlockPos.CODEC).fieldOf("positions").forGetter(path -> path.positions), Codec.list(Codec.DOUBLE).fieldOf("costs").forGetter(path -> path.energyCosts), Path.CODEC.fieldOf("path").forGetter(BattlePath::getPath)).apply(instance, BattlePath::new));
    private final List<BlockPos> positions;
    private final DoubleList energyCosts;
    private final Path path;
    private final double totalCost;

    private BattlePath(final List<BlockPos> positions, final List<Double> energyCosts, final Path path) {
        this.positions = positions;
        this.energyCosts = new DoubleArrayList(energyCosts);
        this.path = path;
        final DoubleListIterator iterator = this.energyCosts.iterator();
        double s = 0;
        while (iterator.hasNext()) {
            s += iterator.nextDouble();
        }
        totalCost = s;
        if (positions.size() != energyCosts.size()) {
            throw new TBCExException("Mismatch position and energy cost list size!");
        }
    }

    public BattlePath(final List<BlockPos> positions, final DoubleList energyCosts, final Path path) {
        this.positions = positions;
        this.energyCosts = new DoubleArrayList(energyCosts);
        this.path = path;
        final DoubleListIterator iterator = this.energyCosts.iterator();
        double s = 0;
        while (iterator.hasNext()) {
            s += iterator.nextDouble();
        }
        totalCost = s;
        if (positions.size() != energyCosts.size()) {
            throw new TBCExException("Mismatch position and energy cost list size!");
        }
    }

    public int getSize() {
        return positions.size();
    }

    public BlockPos getPos(final int index) {
        return positions.get(index);
    }

    public double getCost(final int index) {
        return energyCosts.getDouble(index);
    }

    public double getTotalCost() {
        return totalCost;
    }

    public Path getPath() {
        return path;
    }
}
