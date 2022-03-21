package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.action;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.BattleAction;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleStateView;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Consumer;

public interface BattleParticipantAction {
    Text getName();

    List<OrderedText> getTooltip();

    BattleParticipantActionInstance createInstance(BattleStateView battleState, BattleParticipantHandle handle, Consumer<BattleAction> sender);
}
