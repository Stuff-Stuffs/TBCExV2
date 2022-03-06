package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.restore;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantState;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import net.minecraft.world.World;

import java.util.function.Function;

public final class RestoreDataType<T extends RestoreData> {
    private final Codec<T> codec;
    private final RestoreFunction<T> restoreFunction;

    public RestoreDataType(final Codec<T> codec, final RestoreFunction<T> restoreFunction) {
        this.codec = codec;
        this.restoreFunction = restoreFunction;
    }

    public <K> DataResult<RestoreData> decode(final DynamicOps<K> ops, final K val) {
        return codec.parse(ops, val).map(Function.identity());
    }

    public <K> DataResult<K> encode(final DynamicOps<K> ops, final RestoreData data) {
        if (data.getType() != this) {
            throw new TBCExException("Type mismatch");
        }
        return codec.encodeStart(ops, (T) data);
    }

    public void restore(final RestoreData data, final BattleParticipantState state, final World world) {
        if (data.getType() != this) {
            throw new TBCExException("Type mismatch");
        }
        restoreFunction.restore((T) data, state, world);
    }

    public interface RestoreFunction<T extends RestoreData> {
        void restore(T data, BattleParticipantState state, World world);
    }
}
