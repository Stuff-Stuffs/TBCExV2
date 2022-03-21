package io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.race;

import com.mojang.serialization.Lifecycle;
import io.github.stuff_stuffs.tbcexcharacter.common.TBCExCharacter;
import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.CharacterBattleParticipantStats;
import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.effect.CharacterBattleParticipantLevelEffect;
import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.effect.CharacterBattleParticipantRaceEffect;
import io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant.effect.CharacterDefaultEffect;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipant;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.effect.BattleParticipantEffect;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

import java.util.Collection;
import java.util.List;

public final class BattleParticipantRaces {
    public static final RegistryKey<Registry<BattleParticipantRace>> REGISTRY_KEY = RegistryKey.ofRegistry(TBCExCharacter.createId("race"));
    public static final Registry<BattleParticipantRace> REGISTRY = FabricRegistryBuilder.from(new SimpleRegistry<>(REGISTRY_KEY, Lifecycle.stable(), BattleParticipantRace::getReference)).buildAndRegister();
    public static final BattleParticipantRace HUMAN;

    public static void init() {
        Registry.register(REGISTRY, TBCExCharacter.createId("human"), HUMAN);
    }

    private BattleParticipantRaces() {
    }

    static {
        final BattleParticipantRace.ModifierBuilder humanBuilder = BattleParticipantRace.modifierBuilder();
        humanBuilder.set(CharacterBattleParticipantStats.DEXTERITY, 5, 1);
        humanBuilder.set(CharacterBattleParticipantStats.INTELLIGENCE, 5, 1);
        humanBuilder.set(CharacterBattleParticipantStats.PERCEPTION, 5, 1);
        humanBuilder.set(CharacterBattleParticipantStats.STRENGTH, 5, 1);
        humanBuilder.set(CharacterBattleParticipantStats.VITALITY, 5, 1);
        humanBuilder.set(CharacterBattleParticipantStats.WISDOM, 5, 1);
        HUMAN = new BattleParticipantRace(new LiteralText("Human"), p -> humanBuilder.build(), new BattleParticipantRace.EffectExtractor() {
            @Override
            public <E extends Entity & BattleParticipant> Collection<BattleParticipantEffect> extract(final E entity) {
                return List.of(CharacterDefaultEffect.extract(entity), CharacterBattleParticipantLevelEffect.extract(entity), CharacterBattleParticipantRaceEffect.extract(entity));
            }
        });
    }
}
