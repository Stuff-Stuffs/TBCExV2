package io.github.stuff_stuffs.tbcexcore.common.api.battle;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.BattleAction;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;

public interface BattleTimeline extends BattleTimelineView {
    void trim(int size);

    void push(BattleAction action, Tracer<ActionTrace> tracer);
}
