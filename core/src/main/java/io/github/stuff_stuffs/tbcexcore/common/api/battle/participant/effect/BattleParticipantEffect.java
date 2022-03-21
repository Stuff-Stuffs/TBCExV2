package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.action.BattleParticipantAction;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexutil.common.CodecUtil;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;

import java.util.List;

public interface BattleParticipantEffect {
    Codec<BattleParticipantEffect> CODEC = CodecUtil.createDependentPairCodecFirst(BattleParticipantEffectTypes.REGISTRY.getCodec(), BattleParticipantEffectType::getCodec, BattleParticipantEffect::getType);

    BattleParticipantEffectType<?, ?> getType();

    void init(BattleParticipantState state, Tracer<ActionTrace> tracer);

    void deinit(Tracer<ActionTrace> tracer);

    default List<BattleParticipantAction> getActions() {
        return List.of();
    }
}
