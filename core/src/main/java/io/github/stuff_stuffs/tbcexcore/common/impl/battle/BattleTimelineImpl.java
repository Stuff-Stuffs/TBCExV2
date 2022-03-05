package io.github.stuff_stuffs.tbcexcore.common.impl.battle;

import com.mojang.serialization.Codec;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleTimeline;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.BattleAction;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.state.BattleStateImpl;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;

import java.util.ArrayList;
import java.util.List;

public class BattleTimelineImpl implements BattleTimeline {
    public static final Codec<BattleTimelineImpl> CODEC = Codec.list(BattleAction.CODEC).xmap(BattleTimelineImpl::new, battleTimeline -> battleTimeline.actions);
    private final List<BattleAction> actions;
    private BattleHandle handle;
    private boolean init = false;
    private BattleStateImpl state = new BattleStateImpl();

    public BattleTimelineImpl() {
        actions = new ArrayList<>();
    }

    private BattleTimelineImpl(final List<BattleAction> actions) {
        this.actions = new ArrayList<>(actions);
    }

    public void init(final BattleHandle handle) {
        if (!init) {
            this.handle = handle;
            state.init(handle);
            for (final BattleAction action : actions) {
                action.apply(state);
            }
            init = true;
        }
    }

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
            state.init(handle);
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