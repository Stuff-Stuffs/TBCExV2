package io.github.stuff_stuffs.tbcexcharacter.common;

import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.BattleCharacter;
import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.CharacterBattleParticipantItemCategories;
import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.CharacterBattleParticipantStats;
import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.effect.CharacterBattleParticipantEffects;
import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.race.BattleParticipantRaces;
import io.github.stuff_stuffs.tbcexcharacter.mixin.api.BattleCharacterHolder;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipant;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipantJoinEvent;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffect;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.function.Consumer;

public class TBCExCharacter implements ModInitializer {
    public static final String MOD_ID = "tbcex_character";

    @Override
    public void onInitialize() {
        CharacterBattleParticipantStats.init();
        CharacterBattleParticipantItemCategories.init();
        CharacterBattleParticipantEffects.init();
        BattleParticipantRaces.init();
        BattleParticipantJoinEvent.EVENT.register(new BattleParticipantJoinEvent() {
            @Override
            public <E extends Entity & BattleParticipant> void onJoinBattle(final E entity, final Consumer<BattleParticipantEffect> effectConsumer) {
                final BattleCharacter character;
                if (entity instanceof BattleCharacter c) {
                    character = c;
                } else if (entity instanceof BattleCharacterHolder holder) {
                    character = holder.getCharacter();
                } else {
                    character = null;
                }
                if (character != null) {
                    final Collection<BattleParticipantEffect> effects = character.getRace().getEffects(entity);
                    for (final BattleParticipantEffect effect : effects) {
                        effectConsumer.accept(effect);
                    }
                }
            }
        });
    }

    public static Identifier createId(final String path) {
        return new Identifier(MOD_ID, path);
    }
}
