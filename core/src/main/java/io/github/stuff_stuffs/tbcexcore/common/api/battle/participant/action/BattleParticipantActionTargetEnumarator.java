package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.action;

import com.mojang.datafixers.util.Pair;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public interface BattleParticipantActionTargetEnumarator<T> {
    BattleParticipantActionTargetType<T> getType();

    boolean hasNext();

    void advance();

    BattleParticipantActionTarget<T> value();

    Text name();

    @Nullable Pair<BattleParticipantActionTarget<T>, Double> raycast(Vec3d pos, Vec3d direction);
}
