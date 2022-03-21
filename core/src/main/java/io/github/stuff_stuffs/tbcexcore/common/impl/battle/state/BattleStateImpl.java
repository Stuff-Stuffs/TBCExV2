package io.github.stuff_stuffs.tbcexcore.common.impl.battle.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.effect.BattleEffectContainer;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.event.BattleEvents;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleState;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.effect.BattleEffectContainerImpl;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.state.BattleParticipantStateImpl;
import io.github.stuff_stuffs.tbcexcore.common.util.BattleShapeCache;
import io.github.stuff_stuffs.tbcexutil.common.BattleBounds;
import io.github.stuff_stuffs.tbcexutil.common.CodecUtil;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;
import io.github.stuff_stuffs.tbcexutil.common.event.map.EventMapImpl;
import io.github.stuff_stuffs.tbcexutil.common.event.map.MutEventMap;
import it.unimi.dsi.fastutil.objects.Object2ReferenceLinkedOpenHashMap;
import net.minecraft.world.RegistryWorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Spliterator;

public class BattleStateImpl implements BattleState {
    public static final Codec<BattleStateImpl> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CodecUtil.createLinkedMapCodec(BattleParticipantHandle.CODEC, BattleParticipantStateImpl.CODEC).fieldOf("participants").forGetter(state -> state.participantStateByHandle),
            BattleEffectContainerImpl.CODEC.fieldOf("effects").forGetter(state -> state.effectContainer),
            TurnChooser.CODEC.fieldOf("turnChooser").forGetter(state -> state.turnChooser)
    ).apply(instance, BattleStateImpl::new));
    private final EventMapImpl eventMap = new EventMapImpl();
    private final Object2ReferenceLinkedOpenHashMap<BattleParticipantHandle, BattleParticipantStateImpl> participantStateByHandle;
    private final BattleEffectContainerImpl effectContainer;
    private final TurnChooser turnChooser;
    private BattleShapeCache shapeCache;
    private boolean init = false;
    private BattleBounds bounds = new BattleBounds(0, 0, 0, 0, 0, 0);
    private BattleHandle handle;

    public BattleStateImpl() {
        participantStateByHandle = new Object2ReferenceLinkedOpenHashMap<>();
        effectContainer = new BattleEffectContainerImpl();
        turnChooser = new TurnChooser();
    }

    private BattleStateImpl(final Map<BattleParticipantHandle, BattleParticipantStateImpl> map, final BattleEffectContainerImpl effectContainer, final TurnChooser turnChooser) {
        participantStateByHandle = new Object2ReferenceLinkedOpenHashMap<>();
        for (final Map.Entry<BattleParticipantHandle, BattleParticipantStateImpl> entry : map.entrySet()) {
            participantStateByHandle.putAndMoveToLast(entry.getKey(), entry.getValue());
        }
        this.effectContainer = effectContainer;
        this.turnChooser = turnChooser;
    }

    public void init(final RegistryWorldView worldView, final BattleHandle handle, final Tracer<ActionTrace> tracer) {
        if (!init) {
            shapeCache = new BattleShapeCache(worldView, this);
            this.handle = handle;
            init = true;
            BattleState.BATTLE_EVENT_INIT.invoker().register(eventMap::register);
            for (final Map.Entry<BattleParticipantHandle, BattleParticipantStateImpl> entry : participantStateByHandle.entrySet()) {
                entry.getValue().init(entry.getKey(), this, tracer);
            }
        }
    }

    @Override
    public MutEventMap getEventMap() {
        if (!init) {
            throw new TBCExException("Attempted to access not initialized battle!");
        }
        return eventMap;
    }

    @Override
    public BattleParticipantState getParticipant(final BattleParticipantHandle handle) {
        if (!init) {
            throw new TBCExException("Attempted to access not initialized battle!");
        }
        return participantStateByHandle.get(handle);
    }

    @Override
    public BattleEffectContainer getEffects() {
        if (!init) {
            throw new TBCExException("Attempted to access not initialized battle!");
        }
        return effectContainer;
    }

    @Override
    public BattleBounds getBounds() {
        if (!init) {
            throw new TBCExException("Attempted to access not initialized battle!");
        }
        return bounds;
    }

    @Override
    public @Nullable BattleParticipantHandle getCurrentTurnParticipant() {
        if (!init) {
            throw new TBCExException("Attempted to access not initialized battle!");
        }
        return turnChooser.getCurrent(participantStateByHandle.keySet(), participantStateByHandle::get);
    }

    @Override
    public Spliterator<BattleParticipantHandle> getSpliteratorParticipants() {
        if (!init) {
            throw new TBCExException("Attempted to access not initialized battle!");
        }
        return participantStateByHandle.keySet().spliterator();
    }

    @Override
    public BattleShapeCache getShapeCache() {
        if (!init) {
            throw new TBCExException("Attempted to access not initialized battle!");
        }
        return shapeCache;
    }

    @Override
    public boolean setBounds(final BattleBounds bounds, final Tracer<ActionTrace> tracer) {
        if (!init) {
            throw new TBCExException("Attempted to access not initialized battle!");
        }
        if (!checkNewBounds(bounds)) {
            return false;
        }
        if (getEventMap().getEventMut(BattleEvents.BATTLE_PRE_SET_BOUNDS_EVENT).getInvoker().onSetBounds(this, bounds, tracer)) {
            final BattleBounds old = this.bounds;
            this.bounds = bounds;
            getEventMap().getEventMut(BattleEvents.BATTLE_POST_SET_BOUNDS_EVENT).getInvoker().onSetBounds(this, old, tracer);
            return true;
        }
        return false;
    }

    private boolean checkNewBounds(final BattleBounds bounds) {
        for (final BattleParticipantStateImpl state : participantStateByHandle.values()) {
            if (!bounds.isValid(state.getBounds())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean join(final BattleParticipantState state, final BattleParticipantHandle handle, final Tracer<ActionTrace> tracer) {
        if (!init) {
            throw new TBCExException("Attempted to access not initialized battle!");
        }
        final BattleParticipantStateImpl copy = CodecUtil.copy((BattleParticipantStateImpl) state, BattleParticipantStateImpl.CODEC);
        if (participantStateByHandle.containsKey(handle)) {
            return false;
        }
        participantStateByHandle.putAndMoveToLast(handle, copy);
        copy.init(handle, this, tracer);
        final boolean b = eventMap.getEventMut(BattleEvents.BATTLE_PRE_JOIN_EVENT).getInvoker().beforeJoin(copy, tracer);
        if (!b) {
            participantStateByHandle.removeLast();
            return false;
        }
        eventMap.getEventMut(BattleEvents.BATTLE_POST_JOIN_EVENT).getInvoker().afterJoin(copy, tracer);
        return true;
    }

    @Override
    public boolean advanceTurn(final Tracer<ActionTrace> tracer) {
        if (!init) {
            throw new TBCExException("Attempted to access not initialized battle!");
        }
        if (participantStateByHandle.size() > 0) {
            final BattleParticipantHandle current = turnChooser.getCurrent(participantStateByHandle.keySet(), participantStateByHandle::get);
            return !turnChooser.advance(participantStateByHandle.keySet(), participantStateByHandle::get).equals(current);
        }
        return false;
    }

    @Override
    public BattleHandle getHandle() {
        if (!init) {
            throw new TBCExException("Attempted to access not initialized battle!");
        }
        return handle;
    }
}
