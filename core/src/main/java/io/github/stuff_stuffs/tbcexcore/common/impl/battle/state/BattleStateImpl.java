package io.github.stuff_stuffs.tbcexcore.common.impl.battle.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleWorld;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleState;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleStateView;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.state.BattleParticipantStateImpl;
import io.github.stuff_stuffs.tbcexutil.common.CodecUtil;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import io.github.stuff_stuffs.tbcexutil.common.event.map.EventMapImpl;
import io.github.stuff_stuffs.tbcexutil.common.event.map.MutEventMap;
import it.unimi.dsi.fastutil.objects.Object2ReferenceLinkedOpenHashMap;

import java.util.Map;

public class BattleStateImpl implements BattleState {
    public static final Codec<BattleStateImpl> CODEC = RecordCodecBuilder.create(instance -> instance.group(CodecUtil.createLinkedMapCodec(BattleParticipantHandle.CODEC, BattleParticipantStateImpl.CODEC).fieldOf("participants").forGetter(state -> state.participantStateByHandle)).apply(instance, BattleStateImpl::new));
    private final EventMapImpl eventMap = new EventMapImpl();
    private final Object2ReferenceLinkedOpenHashMap<BattleParticipantHandle, BattleParticipantStateImpl> participantStateByHandle;
    private boolean init = false;
    private BattleHandle handle;
    private BattleWorld battleWorld;

    public BattleStateImpl() {
        participantStateByHandle = new Object2ReferenceLinkedOpenHashMap<>();
    }

    private BattleStateImpl(final Map<BattleParticipantHandle, BattleParticipantStateImpl> map) {
        participantStateByHandle = new Object2ReferenceLinkedOpenHashMap<>();
        for (final Map.Entry<BattleParticipantHandle, BattleParticipantStateImpl> entry : map.entrySet()) {
            participantStateByHandle.putAndMoveToLast(entry.getKey(), entry.getValue());
        }
    }

    public void init(final BattleWorld battleWorld, final BattleHandle handle) {
        if (!init) {
            BattleStateView.BATTLE_EVENT_INIT.invoker().initializeEvents(eventMap::register);
            this.battleWorld = battleWorld;
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
    public BattleHandle getHandle() {
        if (!init) {
            throw new TBCExException("Attempted to access not initialized battle!");
        }
        return handle;
    }
}
