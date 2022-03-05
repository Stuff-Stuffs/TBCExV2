package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.action;

import net.minecraft.text.Text;

import java.util.function.Function;

public final class BattleParticipantActionTargetType<T> {
    private final Text name;
    private final Function<T, BattleParticipantActionTarget<T>> factory;

    public BattleParticipantActionTargetType(final Text name, final Function<T, BattleParticipantActionTarget<T>> factory) {
        this.name = name;
        this.factory = factory;
    }
}
