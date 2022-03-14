package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.restore.RestoreData;
import io.github.stuff_stuffs.tbcexutil.common.BattleParticipantBounds;
import io.github.stuff_stuffs.tbcexutil.common.PairIterator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public interface BattleParticipant {
    PairIterator<ItemStack, Integer> tbcex$getInventoryIterator();

    RestoreData tbcex$getRestoreData(BattleHandle handle);

    long tbcex$getHealth();

    BattleParticipantTeam tbcex$getTeam();

    boolean tbcex$canJoinBattle();

    BattleParticipantBounds tbcex$getBattleBounds();

    BlockPos tbcex$getPos();
}
