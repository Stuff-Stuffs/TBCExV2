package io.github.stuff_stuffs.tbcexcore.common.api.battle;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.BattleAction;

public interface BattleTimeline extends BattleTimelineView {
    void trim(int size);

    void push(BattleAction action);
}
