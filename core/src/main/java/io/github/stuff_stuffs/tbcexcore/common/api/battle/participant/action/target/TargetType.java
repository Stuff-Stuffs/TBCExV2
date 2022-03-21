package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.action.target;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.Battle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.action.BattleParticipantTargetInstance;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleStateView;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface TargetType<T extends BattleParticipantTargetInstance> {
    @Nullable T find(Vec3d pos, Vec3d direction, BattleParticipantHandle user, Battle battle);

    void render(@Nullable BattleParticipantTargetInstance hovered, List<BattleParticipantTargetInstance> targeted, BattleParticipantHandle user, BattleStateView battle, float tickDelta);

    boolean isAnyValid(BattleParticipantHandle user, Battle battle);
}
