package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant;

public final class BattleParticipantUtil {
    public static final long HEALTH_PER_HP = 1_000;

    public static double healthToDouble(final long health) {
        return health / (double) HEALTH_PER_HP;
    }

    public static long doubleToHealth(final double health) {
        return (long) Math.floor(health * HEALTH_PER_HP);
    }

    private BattleParticipantUtil() {
    }
}
