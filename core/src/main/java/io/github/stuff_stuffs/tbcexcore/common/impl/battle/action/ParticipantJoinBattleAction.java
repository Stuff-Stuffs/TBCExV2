package io.github.stuff_stuffs.tbcexcore.common.impl.battle.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.BattleAction;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.BattleActionType;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.BattleActionTypes;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleState;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.state.BattleParticipantStateImpl;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;

public final class ParticipantJoinBattleAction implements BattleAction {
    public static final Codec<ParticipantJoinBattleAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(BattleParticipantHandle.CODEC.fieldOf("handle").forGetter(action -> action.handle), BattleParticipantStateImpl.CODEC.fieldOf("state").forGetter(action -> (BattleParticipantStateImpl) action.state)).apply(instance, ParticipantJoinBattleAction::new));
    private final BattleParticipantHandle handle;
    private final BattleParticipantState state;

    public ParticipantJoinBattleAction(final BattleParticipantHandle handle, final BattleParticipantState state) {
        this.handle = handle;
        this.state = state;
    }

    @Override
    public BattleActionType<?> getType() {
        return BattleActionTypes.PARTICIPANT_JOIN_BATTLE_TYPE;
    }

    @Override
    public void apply(final BattleState state, Tracer<ActionTrace> tracer) {
        final boolean join = state.join(this.state, handle, tracer);
        if (!join) {
            throw new TBCExException("Error while adding participant to battle!");
        }
    }
}
