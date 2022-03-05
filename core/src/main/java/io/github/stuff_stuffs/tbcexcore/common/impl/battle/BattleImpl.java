package io.github.stuff_stuffs.tbcexcore.common.impl.battle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.Battle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleTimelineView;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleStateView;

public class BattleImpl implements Battle {
    public static final Codec<BattleImpl> CODEC = RecordCodecBuilder.create(instance -> instance.group(BattleTimelineImpl.CODEC.fieldOf("timeline").forGetter(battle -> battle.timeline)).apply(instance, BattleImpl::new));
    private final BattleTimelineImpl timeline;

    public BattleImpl() {
        timeline = new BattleTimelineImpl();
    }

    private BattleImpl(final BattleTimelineImpl timeline) {
        this.timeline = timeline;
    }

    public void init(final BattleHandle handle) {
        timeline.init(handle);
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
