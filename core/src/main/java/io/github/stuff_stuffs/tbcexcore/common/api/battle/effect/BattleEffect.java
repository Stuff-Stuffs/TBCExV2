package io.github.stuff_stuffs.tbcexcore.common.api.battle.effect;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.state.BattleState;

public interface BattleEffect {
    BattleEffectType<?, ?> getType();

    void init(BattleState state);

    void deinit();
}
