package io.github.stuff_stuffs.tbcexcore.mixin.impl;

import io.github.stuff_stuffs.tbcexcore.common.TBCExCore;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipant;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipantTeam;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipantUtil;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.restore.RestoreData;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.participant.restore.PlayerRestoreData;
import io.github.stuff_stuffs.tbcexcore.mixin.api.BattlePlayerEntity;
import io.github.stuff_stuffs.tbcexutil.common.BattleParticipantBounds;
import io.github.stuff_stuffs.tbcexutil.common.PairIterator;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Objects;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements BattlePlayerEntity, BattleParticipant {
    private static final BattleParticipantBounds DEFAULT = BattleParticipantBounds.builder().add(TBCExCore.createId("body"), new Box(0,0,0, 1, 1.75, 1)).build();
    @Shadow
    public abstract PlayerInventory getInventory();

    @Unique
    private boolean dirty;
    @Unique
    private BattleHandle currentBattle;

    protected MixinPlayerEntity(final EntityType<? extends LivingEntity> entityType, final World world) {
        super(entityType, world);
    }

    @Override
    public BattleHandle tbcex$getCurrentBattle() {
        return currentBattle;
    }

    @Override
    public void tbcex$setCurrentBattle(final BattleHandle handle) {
        if (!Objects.equals(currentBattle, handle)) {
            dirty = true;
        }
        currentBattle = handle;
    }

    @Override
    public boolean tbcex$isDirty() {
        return dirty;
    }

    @Override
    public void tbcex$setDirty(final boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public PairIterator<ItemStack, Integer> tbcex$getInventoryIterator() {
        return PairIterator.fromInventory(getInventory());
    }

    @Override
    public RestoreData tbcex$getRestoreData(final BattleHandle handle) {
        return new PlayerRestoreData(getUuid());
    }

    @Override
    public long tbcex$getHealth() {
        return BattleParticipantUtil.doubleToHealth(getHealth());
    }

    @Override
    public BattleParticipantTeam tbcex$getTeam() {
        return TBCExCore.PLAYER_TEAM;
    }

    @Override
    public boolean tbcex$canJoinBattle() {
        return currentBattle == null;
    }

    @Override
    public BattleParticipantBounds tbcex$getBattleBounds() {
        return DEFAULT;
    }

    @Override
    public BlockPos tbcex$getPos() {
        return this.getBlockPos();
    }
}
