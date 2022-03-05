package io.github.stuff_stuffs.tbcexcore.common.impl.battle;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleTimeline;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.BattleAction;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.state.BattleStateImpl;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;

import java.util.ArrayList;
import java.util.List;

public class BattleTimelineImpl implements BattleTimeline {
    private final List<BattleAction> actions = new ArrayList<>();
    private BattleStateImpl state = new BattleStateImpl();

    @Override
    public void trim(final int size) {
        if (size < 0) {
            throw new TBCExException("Cannot trim into a negative size!");
        }
        if (size != actions.size()) {
            while (actions.size() > size) {
                actions.remove(actions.size() - 1);
            }
            state = new BattleStateImpl();
            for (final BattleAction action : actions) {
                action.apply(state);
            }
        }
    }

    public BattleStateImpl getState() {
        return state;
    }

    @Override
    public void push(final BattleAction action) {
        actions.add(action);
        action.apply(state);
    }

    @Override
    public int getSize() {
        return actions.size();
    }

    @Override
    public BattleAction getAction(final int index) {
        return actions.get(index);
    }
}
