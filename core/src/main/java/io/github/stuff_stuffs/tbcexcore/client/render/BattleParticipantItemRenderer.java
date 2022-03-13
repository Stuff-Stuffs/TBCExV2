package io.github.stuff_stuffs.tbcexcore.client.render;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItemStack;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexgui.client.api.GuiContext;
import org.jetbrains.annotations.Nullable;

public interface BattleParticipantItemRenderer {
    void renderInGui(GuiContext context, BattleParticipantItemStack stack, @Nullable BattleParticipantStateView state);
}
