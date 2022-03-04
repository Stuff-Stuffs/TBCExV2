package io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffectContainer;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantInventory;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStat;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStatContainer;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleState;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.effect.BattleParticipantEffectContainerImpl;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.inventory.BattleParticipantInventoryImpl;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import io.github.stuff_stuffs.tbcexutil.common.event.map.EventMapImpl;
import io.github.stuff_stuffs.tbcexutil.common.event.map.MutEventMap;

public class BattleParticipantStateImpl implements BattleParticipantState {
    public static final Codec<BattleParticipantStateImpl> CODEC = RecordCodecBuilder.create(instance -> instance.group(BattleParticipantEffectContainerImpl.CODEC.fieldOf("effects").forGetter(participant -> participant.effectContainer), BattleParticipantInventoryImpl.CODEC.fieldOf("inventory").forGetter(participant -> participant.inventory)).apply(instance, BattleParticipantStateImpl::new));
    private final EventMapImpl eventMap;
    private final BattleParticipantEffectContainerImpl effectContainer;
    private final BattleParticipantStatContainer statContainer;
    private final BattleParticipantInventoryImpl inventory;
    private BattleParticipantHandle handle;
    private BattleState battleState;

    public BattleParticipantStateImpl() {
        eventMap = new EventMapImpl();
        effectContainer = new BattleParticipantEffectContainerImpl();
        statContainer = new BattleParticipantStatContainer();
        inventory = new BattleParticipantInventoryImpl();
    }

    public BattleParticipantStateImpl(final BattleParticipantEffectContainerImpl effectContainer, final BattleParticipantInventoryImpl inventory) {
        eventMap = new EventMapImpl();
        this.effectContainer = effectContainer;
        statContainer = new BattleParticipantStatContainer();
        this.inventory = inventory;
    }

    public void init(final BattleParticipantHandle handle, final BattleState state) {
        BattleParticipantStateView.PARTICIPANT_EVENT_INIT.invoker().initializeEvents(eventMap::register);
        this.handle = handle;
        battleState = state;
        effectContainer.init(this);
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
        return inventory;
    }

    @Override
    public double getStat(final BattleParticipantStat stat) {
        return statContainer.getStat(stat);
    }

    @Override
    public BattleParticipantStatContainer getStatContainer() {
        return statContainer;
    }

    @Override
    public BattleParticipantEffectContainer getEffectContainer() {
        return effectContainer;
    }
}
