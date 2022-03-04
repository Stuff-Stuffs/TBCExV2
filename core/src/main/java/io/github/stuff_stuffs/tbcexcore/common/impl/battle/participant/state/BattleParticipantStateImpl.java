package io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffect;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffectContainer;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffectType;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStat;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStatContainer;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleState;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import io.github.stuff_stuffs.tbcexutil.common.event.map.EventMapImpl;
import io.github.stuff_stuffs.tbcexutil.common.event.map.MutEventMap;
import org.jetbrains.annotations.Nullable;

public class BattleParticipantStateImpl implements BattleParticipantState {
    public static final Codec<BattleParticipantStateImpl> CODEC = RecordCodecBuilder.create(instance -> instance.group(BattleParticipantEffectContainer.CODEC.fieldOf("effects").forGetter(participant -> participant.effectContainer)).apply(instance, BattleParticipantStateImpl::new));
    private final EventMapImpl eventMap;
    private final BattleParticipantEffectContainer effectContainer;
    private final BattleParticipantStatContainer statContainer;
    private BattleParticipantHandle handle;
    private BattleState battleState;

    public BattleParticipantStateImpl() {
        eventMap = new EventMapImpl();
        effectContainer = new BattleParticipantEffectContainer();
        statContainer = new BattleParticipantStatContainer();
    }

    public BattleParticipantStateImpl(final BattleParticipantEffectContainer effectContainer) {
        eventMap = new EventMapImpl();
        this.effectContainer = effectContainer;
        statContainer = new BattleParticipantStatContainer();
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
    public double getStat(final BattleParticipantStat stat) {
        return statContainer.getStat(stat);
    }

    @Override
    public <T extends BattleParticipantEffect> @Nullable T getEffect(final BattleParticipantEffectType<T> type) {
        return effectContainer.getEffect(type);
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
