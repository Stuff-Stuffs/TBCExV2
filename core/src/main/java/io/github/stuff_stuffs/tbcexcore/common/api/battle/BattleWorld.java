package io.github.stuff_stuffs.tbcexcore.common.api.battle;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleStateView;

public interface BattleWorld {
    BattleStateView getBattle(BattleHandle handle);
}
