package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.restore;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.github.stuff_stuffs.tbcexutil.common.CodecUtil;

public interface RestoreData {
    Codec<RestoreData> CODEC = CodecUtil.createDependentPairCodecFirst(RestoreDataTypes.REGISTRY.getCodec(), new CodecUtil.DependentEncoder<>() {
        @Override
        public <T> DataResult<T> encode(final RestoreDataType<?> coValue, final RestoreData value, final DynamicOps<T> ops) {
            return coValue.encode(ops, value);
        }
    }, new CodecUtil.DependentDecoder<>() {
        @Override
        public <T> DataResult<RestoreData> decode(final RestoreDataType<?> coValue, final T value, final DynamicOps<T> ops) {
            return coValue.decode(ops, value);
        }
    }, RestoreData::getType);

    RestoreDataType<?> getType();
}
