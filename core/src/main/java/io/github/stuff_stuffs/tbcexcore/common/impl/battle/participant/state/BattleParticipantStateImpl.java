package io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipant;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipantTeam;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffectContainer;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.event.BattleParticipantEvents;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.health.BattleParticipantHealthContainer;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantInventory;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.restore.RestoreData;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStat;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStatContainer;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleState;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.effect.BattleParticipantEffectContainerImpl;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.health.BattleParticipantHealthContainerImpl;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.inventory.BattleParticipantInventoryImpl;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;
import io.github.stuff_stuffs.tbcexutil.common.event.map.EventMapImpl;
import io.github.stuff_stuffs.tbcexutil.common.event.map.MutEventMap;

public class BattleParticipantStateImpl implements BattleParticipantState {
    public static final Codec<BattleParticipantStateImpl> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BattleParticipantEffectContainerImpl.CODEC.fieldOf("effects").forGetter(participant -> participant.effectContainer),
            BattleParticipantInventoryImpl.CODEC.fieldOf("inventory").forGetter(participant -> participant.inventory),
            BattleParticipantHealthContainerImpl.CODEC.fieldOf("health").forGetter(participant -> participant.healthContainer),
            BattleParticipantTeam.CODEC.fieldOf("team").forGetter(participant -> participant.team),
            RestoreData.CODEC.fieldOf("restore").forGetter(participant -> participant.restoreData)
    ).apply(instance, BattleParticipantStateImpl::new));
    private final EventMapImpl eventMap;
    private final BattleParticipantEffectContainerImpl effectContainer;
    private final BattleParticipantStatContainer statContainer;
    private final BattleParticipantInventoryImpl inventory;
    private final BattleParticipantHealthContainerImpl healthContainer;
    private final RestoreData restoreData;
    private BattleParticipantTeam team;

    private BattleParticipantHandle handle;
    private BattleState battleState;

    public BattleParticipantStateImpl(final BattleParticipant participant, final RestoreData restoreData) {
        eventMap = new EventMapImpl();
        effectContainer = new BattleParticipantEffectContainerImpl();
        statContainer = new BattleParticipantStatContainer();
        inventory = new BattleParticipantInventoryImpl();
        healthContainer = new BattleParticipantHealthContainerImpl(participant);
        team = participant.tbcex$getTeam();
        this.restoreData = restoreData;
    }

    public BattleParticipantStateImpl(final BattleParticipantEffectContainerImpl effectContainer, final BattleParticipantInventoryImpl inventory, final BattleParticipantHealthContainerImpl healthContainer, final BattleParticipantTeam team, final RestoreData restoreData) {
        eventMap = new EventMapImpl();
        this.effectContainer = effectContainer;
        statContainer = new BattleParticipantStatContainer();
        this.inventory = inventory;
        this.healthContainer = healthContainer;
        this.team = team;
        this.restoreData = restoreData;
    }

    public void init(final BattleParticipantHandle handle, final BattleState state, final Tracer<ActionTrace> tracer) {
        BattleParticipantStateView.BATTLE_PARTICIPANT_EVENT_INIT.invoker().register(eventMap::register);
        this.handle = handle;
        battleState = state;
        effectContainer.init(this, tracer);
        healthContainer.init(this);
    }

    @Override
    public BattleParticipantHandle getHandle() {
        if (handle == null) {
            throw new TBCExException("Tried to use a battle participant without initializing it");
        }
        return handle;
    }

    @Override
    public MutEventMap getEventMap() {
        return eventMap;
    }

    @Override
    public BattleState getBattleState() {
        if (battleState == null) {
            throw new TBCExException("Tried to use a battle participant without initializing it");
        }
        return battleState;
    }

    @Override
    public BattleParticipantInventory getInventory() {
        if (handle == null) {
            throw new TBCExException("Tried to use a battle participant without initializing it");
        }
        return inventory;
    }

    @Override
    public double getStat(final BattleParticipantStat stat) {
        if (handle == null) {
            throw new TBCExException("Tried to use a battle participant without initializing it");
        }
        return statContainer.getStat(stat);
    }

    @Override
    public BattleParticipantStatContainer getStatContainer() {
        if (handle == null) {
            throw new TBCExException("Tried to use a battle participant without initializing it");
        }
        return statContainer;
    }

    @Override
    public BattleParticipantEffectContainer getEffectContainer() {
        if (handle == null) {
            throw new TBCExException("Tried to use a battle participant without initializing it");
        }
        return effectContainer;
    }

    @Override
    public BattleParticipantHealthContainer getHealthContainer() {
        if (handle == null) {
            throw new TBCExException("Tried to use a battle participant without initializing it");
        }
        return healthContainer;
    }

    @Override
    public BattleParticipantTeam getTeam() {
        if (handle == null) {
            throw new TBCExException("Tried to use a battle participant without initializing it");
        }
        return team;
    }

    @Override
    public boolean setTeam(final BattleParticipantTeam team, final Tracer<ActionTrace> tracer) {
        if (handle == null) {
            throw new TBCExException("Tried to use a battle participant without initializing it");
        }
        if (getEventMap().getEventMut(BattleParticipantEvents.BATTLE_PARTICIPANT_PRE_CHANGE_TEAM_EVENT).getInvoker().beforeTeamChange(this, this.team, team, tracer)) {
            this.team = team;
            getEventMap().getEventMut(BattleParticipantEvents.BATTLE_PARTICIPANT_POST_CHANGE_TEAM_EVENT).getInvoker().afterTeamChange(this, this.team, team, tracer);
            return true;
        }
        return false;
    }
}
