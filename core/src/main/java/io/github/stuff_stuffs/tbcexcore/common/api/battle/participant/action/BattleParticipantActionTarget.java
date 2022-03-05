package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.action;

public final class BattleParticipantActionTarget<T> {
    private final T value;
    private final BattleParticipantActionTargetType<T> type;

    public BattleParticipantActionTarget(final T value, final BattleParticipantActionTargetType<T> type) {
        this.value = value;
        this.type = type;
    }

    public T getValue() {
        return value;
    }

    public BattleParticipantActionTargetType<T> getType() {
        return type;
    }
}
