package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItemStack;
import io.github.stuff_stuffs.tbcexutil.common.PairIterator;

public interface BattleParticipantInventoryView {
    BattleParticipantItemStack getStack(BattleParticipantInventoryHandle handle);

    boolean isEquipped(BattleParticipantInventoryHandle handle);

    boolean isEquipped(BattleParticipantEquipmentSlot slot);

    PairIterator<BattleParticipantItemStack, BattleParticipantInventoryHandle> getIterator();
}
