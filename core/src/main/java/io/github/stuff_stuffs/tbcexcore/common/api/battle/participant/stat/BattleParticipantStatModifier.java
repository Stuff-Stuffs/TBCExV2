package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat;

import com.mojang.serialization.Codec;
import io.github.stuff_stuffs.tbcexutil.common.CodecUtil;

public interface BattleParticipantStatModifier {
    double modify(double current, BattleParticipantStat stat);

    static BattleParticipantStatModifier add(final double val) {
        return (current, stat) -> current + val;
    }

    static BattleParticipantStatModifier multiplier(final double val) {
        return (current, stat) -> current * val;
    }

    enum Phase {
        ADDERS,
        MULTIPLIERS,
        POST_MULT_ADDERS,
        POST_MULT_MULTIPLIERS,
        LAST;
        public static final Codec<Phase> CODEC = CodecUtil.createEnumCodec(Phase.class);
    }
}
