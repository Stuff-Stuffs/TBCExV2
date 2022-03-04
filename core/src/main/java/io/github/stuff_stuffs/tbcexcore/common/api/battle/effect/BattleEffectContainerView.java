package io.github.stuff_stuffs.tbcexcore.common.api.battle.effect;

import org.jetbrains.annotations.Nullable;

public interface BattleEffectContainerView {
    <T extends BattleEffect> @Nullable T getEffect(BattleEffectType<T, ?> effectType);

    boolean hasEffect(BattleEffectType<?, ?> effectType);
}
