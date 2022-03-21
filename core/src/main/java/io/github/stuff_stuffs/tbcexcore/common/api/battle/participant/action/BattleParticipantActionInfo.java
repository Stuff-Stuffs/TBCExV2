package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.action;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.action.target.TargetType;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleStateView;
import net.minecraft.text.OrderedText;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface BattleParticipantActionInfo {
    @Nullable TargetType<?> getNextTargetType(List<BattleParticipantTargetInstance> list);

    boolean canActivate(BattleStateView battleState, BattleParticipantHandle user, List<BattleParticipantTargetInstance> list);

    void activate(BattleStateView battleState, BattleParticipantHandle user, List<BattleParticipantTargetInstance> list);

    @Nullable List<OrderedText> getDescription(BattleStateView battleState, BattleParticipantHandle user, List<BattleParticipantTargetInstance> list);
}
