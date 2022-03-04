package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat;

public interface BattleParticipantStatModifier {
    double modify(double current, BattleParticipantStat stat);

    enum Phase {
        ADDERS,
        MULTIPLIERS,
        POST_MULT_ADDERS,
        POST_MULT_MULTIPLIERS,
        LAST
    }
}
