package io.github.stuff_stuffs.tbcexcore.common.impl.battle.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.effect.BattleEffectContainer;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleState;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.effect.BattleEffectContainerImpl;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.state.BattleParticipantStateImpl;
import io.github.stuff_stuffs.tbcexutil.common.CodecUtil;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import io.github.stuff_stuffs.tbcexutil.common.event.map.EventMapImpl;
import io.github.stuff_stuffs.tbcexutil.common.event.map.MutEventMap;
import it.unimi.dsi.fastutil.objects.Object2ReferenceLinkedOpenHashMap;

import java.util.Map;

public class BattleStateImpl implements BattleState {
    public static final Codec<BattleStateImpl> CODEC = RecordCodecBuilder.create(instance -> instance.group(CodecUtil.createLinkedMapCodec(BattleParticipantHandle.CODEC, BattleParticipantStateImpl.CODEC).fieldOf("participants").forGetter(state -> state.participantStateByHandle), BattleEffectContainerImpl.CODEC.fieldOf("effects").forGetter(state -> state.effectContainer)).apply(instance, BattleStateImpl::new));
    private final EventMapImpl eventMap = new EventMapImpl();
    private final Object2ReferenceLinkedOpenHashMap<BattleParticipantHandle, BattleParticipantStateImpl> participantStateByHandle;
    private final BattleEffectContainerImpl effectContainer;
    private boolean init = false;
    private BattleHandle handle;

    public BattleStateImpl() {
        participantStateByHandle = new Object2ReferenceLinkedOpenHashMap<>();
        effectContainer = new BattleEffectContainerImpl();
    }

    private BattleStateImpl(final Map<BattleParticipantHandle, BattleParticipantStateImpl> map, final BattleEffectContainerImpl effectContainer) {
        participantStateByHandle = new Object2ReferenceLinkedOpenHashMap<>();
        for (final Map.Entry<BattleParticipantHandle, BattleParticipantStateImpl> entry : map.entrySet()) {
            participantStateByHandle.putAndMoveToLast(entry.getKey(), entry.getValue());
        }
        this.effectContainer = effectContainer;
    }

    public void init(final BattleHandle handle) {
        if (!init) {
            this.handle = handle;
            init = true;
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
    public BattleHandle getHandle() {
        if (!init) {
            throw new TBCExException("Attempted to access not initialized battle!");
        }
        return handle;
    }
}
