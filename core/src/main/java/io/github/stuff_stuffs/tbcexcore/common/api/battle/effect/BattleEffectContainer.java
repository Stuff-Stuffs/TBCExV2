package io.github.stuff_stuffs.tbcexcore.common.api.battle.effect;

import org.jetbrains.annotations.Nullable;

public interface BattleEffectContainer extends BattleEffectContainerView {
    <View extends BattleEffect, Mut extends View> @Nullable Mut getEffectMut(BattleEffectType<View, Mut> effectType);

    boolean addEffect(BattleEffect effect);

    boolean removeEffect(BattleEffectType<?, ?> effectType);
}
