package io.github.stuff_stuffs.tbcexcore.common.api.battle;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.BattleAction;

public interface BattleTimelineView {
    int getSize();

    BattleAction getAction(int index);
}
