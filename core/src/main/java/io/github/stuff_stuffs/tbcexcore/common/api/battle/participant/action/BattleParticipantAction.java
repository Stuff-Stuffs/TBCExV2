package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.action;

import com.mojang.datafixers.util.Pair;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.BattleAction;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class BattleParticipantAction {
    private final Executor executor;
    private final BattleParticipantActionContext context;
    private final List<BattleParticipantActionTarget<?>> targets = new ArrayList<>();
    private final Text displayName;
    private List<BattleParticipantActionTargetEnumarator<?>> enumerators;

    public BattleParticipantAction(final Executor executor, final BattleParticipantActionContext context, final Text displayName) {
        this.executor = executor;
        this.context = context;
        this.displayName = displayName;
        enumerators = executor.getTargetEnumerators(targets, context);
    }

    public void raycast(final Vec3d pos, final Vec3d dir) {
        double best = Double.POSITIVE_INFINITY;
        BattleParticipantActionTarget<?> target = null;
        for (final BattleParticipantActionTargetEnumarator<?> enumerator : enumerators) {
            final @Nullable Pair<? extends BattleParticipantActionTarget<?>, Double> raycast = enumerator.raycast(pos, dir);
            if (raycast != null && raycast.getSecond() < best) {
                target = raycast.getFirst();
                best = raycast.getSecond();
            }
        }
        if (target != null) {
            targets.add(target);
            enumerators = executor.getTargetEnumerators(targets, context);
        }
    }


    public Text getDisplayName() {
        return displayName;
    }

    public boolean canExecute() {
        return executor.execute(targets, context) != null;
    }

    public BattleAction execute() {
        final BattleAction apply = executor.execute(targets, context);
        if (apply == null) {
            throw new TBCExException("Should not execute action, tried anyway!");
        }
        return apply;
    }

    public interface Executor {
        @Nullable BattleAction execute(List<BattleParticipantActionTarget<?>> targets, BattleParticipantActionContext context);

        List<BattleParticipantActionTargetEnumarator<?>> getTargetEnumerators(List<BattleParticipantActionTarget<?>> targets, BattleParticipantActionContext context);
    }
}
