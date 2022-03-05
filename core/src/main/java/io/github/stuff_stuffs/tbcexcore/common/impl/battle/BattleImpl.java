package io.github.stuff_stuffs.tbcexcore.common.impl.battle;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.Battle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleTimelineView;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleStateView;

public class BattleImpl implements Battle {
    private final BattleTimelineImpl timeline;

    public BattleImpl() {
        timeline = new BattleTimelineImpl();
    }

    @Override
    public BattleStateView getState() {
        return timeline.getState();
    }

    @Override
    public BattleTimelineView getTimeline() {
        return timeline;
    }
}
