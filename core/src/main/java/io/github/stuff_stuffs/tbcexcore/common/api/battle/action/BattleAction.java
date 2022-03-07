package io.github.stuff_stuffs.tbcexcore.common.api.battle.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleState;
import io.github.stuff_stuffs.tbcexutil.common.CodecUtil;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;

public interface BattleAction {
    Codec<BattleAction> CODEC = CodecUtil.createDependentPairCodecFirst(BattleActionTypes.REGISTRY.getCodec(), new CodecUtil.DependentEncoder<>() {
        @Override
        public <T> DataResult<T> encode(final BattleActionType<?> coValue, final BattleAction value, final DynamicOps<T> ops) {
            return coValue.encode(ops, value);
        }
    }, new CodecUtil.DependentDecoder<>() {
        @Override
        public <T> DataResult<BattleAction> decode(final BattleActionType<?> coValue, final T value, final DynamicOps<T> ops) {
            return coValue.decode(ops, value);
        }
    }, BattleAction::getType);

    BattleActionType<?> getType();

    void apply(BattleState state, Tracer<ActionTrace> tracer);
}
