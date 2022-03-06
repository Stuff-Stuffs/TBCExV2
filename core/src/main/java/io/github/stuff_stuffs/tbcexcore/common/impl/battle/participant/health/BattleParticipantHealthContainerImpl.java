package io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.health;

import com.mojang.serialization.Codec;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipant;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipantUtil;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.health.BattleParticipantHealthContainer;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStats;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.state.BattleParticipantStateImpl;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;

public class BattleParticipantHealthContainerImpl implements BattleParticipantHealthContainer {
    public static final Codec<BattleParticipantHealthContainerImpl> CODEC = Codec.LONG.xmap(BattleParticipantHealthContainerImpl::new, container -> container.health);
    private long health;
    private boolean init = false;
    private BattleParticipantStateImpl state;

    public BattleParticipantHealthContainerImpl(final BattleParticipant participant) {
        health = participant.getHealth();
    }

    private BattleParticipantHealthContainerImpl(final long health) {
        this.health = health;
    }

    public void init(final BattleParticipantStateImpl state) {
        init = true;
        this.state = state;
        health = Math.min(health, BattleParticipantUtil.doubleToHealth(state.getStat(BattleParticipantStats.MAX_HEALTH)));
    }

    @Override
    public long getHealth() {
        if (!init) {
            throw new TBCExException("Tried to access a health container before initialization!");
        }
        return health;
    }
}
