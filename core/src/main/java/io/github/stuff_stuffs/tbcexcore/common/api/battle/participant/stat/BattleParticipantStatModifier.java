package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat;

public interface BattleParticipantStatModifier {
    double modify(double current, BattleParticipantStat stat);
}
