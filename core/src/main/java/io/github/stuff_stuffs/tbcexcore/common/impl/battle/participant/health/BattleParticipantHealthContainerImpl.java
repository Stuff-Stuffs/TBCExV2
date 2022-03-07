package io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.health;

import com.mojang.serialization.Codec;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipant;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipantUtil;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.damage.BattleParticipantDamagePacket;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.event.BattleParticipantEvents;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.health.BattleParticipantHealthContainer;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStats;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.state.BattleParticipantStateImpl;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;

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

    //TODO death
    @Override
    public boolean damage(BattleParticipantDamagePacket damage, Tracer<ActionTrace> tracer) {
        damage = state.getEventMap().getEventMut(BattleParticipantEvents.BATTLE_PARTICIPANT_PRE_DAMAGE_EVENT).getInvoker().beforeDamage(state, damage, tracer);
        if (damage.getSum() == 0) {
            return false;
        }
        health = Math.max(0, health - damage.getSum());
        state.getEventMap().getEventMut(BattleParticipantEvents.BATTLE_PARTICIPANT_POST_DAMAGE_EVENT).getInvoker().postDamage(state, damage, tracer);
        return true;
    }
}
