package io.github.stuff_stuffs.tbcexcore.mixin.api;

import io.github.stuff_stuffs.tbcexcore.client.render.hud.BattleHudScreen;
import org.jetbrains.annotations.Nullable;

public interface BattleHudSupplier {
    @Nullable BattleHudScreen tbcex$getBattleHud();
}
