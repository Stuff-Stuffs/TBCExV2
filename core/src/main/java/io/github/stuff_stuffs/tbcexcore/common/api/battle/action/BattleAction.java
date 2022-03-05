package io.github.stuff_stuffs.tbcexcore.common.api.battle.action;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleState;

public interface BattleAction {
    BattleActionType<?> getType();

    void apply(BattleState state);
}
