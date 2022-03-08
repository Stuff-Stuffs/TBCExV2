package io.github.stuff_stuffs.tbcexcore.mixin.api;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;

public interface BattlePlayerEntity {
    BattleHandle tbcex$getCurrentBattle();

    void tbcex$setCurrentBattle(BattleHandle handle);

    boolean tbcex$isDirty();

    void tbcex$setDirty(boolean dirty);
}
