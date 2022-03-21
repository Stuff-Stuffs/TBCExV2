package io.github.stuff_stuffs.tbcexcharacter.mixin.impl;

import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.BattleCharacter;
import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.CharacterLevelInfo;
import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.race.BattleParticipantRaces;
import io.github.stuff_stuffs.tbcexcharacter.mixin.api.BattleCharacterHolder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipant;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.EffectedBattleParticipant;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Consumer;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends Entity implements BattleCharacterHolder, EffectedBattleParticipant, BattleParticipant {
    private final BattleCharacter.SyncImpl character = new BattleCharacter.SyncImpl(BattleParticipantRaces.HUMAN, CharacterLevelInfo.builder().build(0, 0));

    public MixinPlayerEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public BattleCharacter getCharacter() {
        return character;
    }

    @Override
    public void tbcex$addEffects(Consumer<BattleParticipantEffect> effectAdder) {
        for (BattleParticipantEffect effect : getCharacter().getRace().getEffects(this)) {
            effectAdder.accept(effect);
        }
    }
}
