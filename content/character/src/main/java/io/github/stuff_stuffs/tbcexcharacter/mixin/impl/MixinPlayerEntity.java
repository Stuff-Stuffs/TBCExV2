package io.github.stuff_stuffs.tbcexcharacter.mixin.impl;

import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.BattleCharacter;
import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.CharacterLevelInfo;
import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.race.BattleParticipantRaces;
import io.github.stuff_stuffs.tbcexcharacter.mixin.api.BattleCharacterHolder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipant;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends Entity implements BattleCharacterHolder, BattleParticipant {
    private final BattleCharacter.SyncImpl character = new BattleCharacter.SyncImpl(BattleParticipantRaces.HUMAN, CharacterLevelInfo.builder().build(0, 0));

    public MixinPlayerEntity(final EntityType<?> type, final World world) {
        super(type, world);
    }

    @Override
    public BattleCharacter getCharacter() {
        return character;
    }
}
