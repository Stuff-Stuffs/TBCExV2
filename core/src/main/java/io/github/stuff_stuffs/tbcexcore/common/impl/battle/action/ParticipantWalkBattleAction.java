package io.github.stuff_stuffs.tbcexcore.common.impl.battle.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.BattleAction;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.BattleActionType;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattlePath;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleState;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;

public class ParticipantWalkBattleAction implements BattleAction {
    public static final Codec<ParticipantWalkBattleAction> CODEC = RecordCodecBuilder.create(instance -> instance.group(BattleParticipantHandle.CODEC.fieldOf("handle").forGetter(action -> action.handle), BattlePath.CODEC.fieldOf("path").forGetter(action -> action.path)).apply(instance, ParticipantWalkBattleAction::new));
    private final BattleParticipantHandle handle;
    private final BattlePath path;

    public ParticipantWalkBattleAction(BattleParticipantHandle handle, BattlePath path) {
        this.handle = handle;
        this.path = path;
    }

    @Override
    public BattleActionType<?> getType() {
        return null;
    }

    @Override
    public void apply(BattleState state, Tracer<ActionTrace> tracer) {
        double cost = path.getTotalCost();
        BattleParticipantState participant = state.getParticipant(handle);
        if (participant == null) {
            BattleAction.error();
        }
        if (!participant.useEnergy(cost, tracer)) {
            BattleAction.error();
        } else {
            int size = path.getSize();
            for (int i = 0; i < size; i++) {
                if(!participant.setPos(path.getPos(size), tracer)) {
                    BattleAction.error();
                }
            }
        }
    }
}
