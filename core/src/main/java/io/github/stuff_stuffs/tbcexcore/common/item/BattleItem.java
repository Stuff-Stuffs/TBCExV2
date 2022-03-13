package io.github.stuff_stuffs.tbcexcore.common.item;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItemStack;
import net.minecraft.item.ItemStack;

public interface BattleItem {
    BattleParticipantItemStack toBattleParticipantItem(ItemStack stack);
}
