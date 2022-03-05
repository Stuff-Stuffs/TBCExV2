package io.github.stuff_stuffs.tbcexcore.common.api.battle;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleStateView;

public interface Battle {
    BattleStateView getState();

    BattleTimelineView getTimeline();
}
