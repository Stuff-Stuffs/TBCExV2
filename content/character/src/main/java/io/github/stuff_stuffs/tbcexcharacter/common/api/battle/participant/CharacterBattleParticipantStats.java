package io.github.stuff_stuffs.tbcexcharacter.common.api.battle.participant;

import io.github.stuff_stuffs.tbcexcharacter.common.TBCExCharacter;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStat;
import net.minecraft.text.LiteralText;
import net.minecraft.util.registry.Registry;

public final class CharacterBattleParticipantStats {
    public static final BattleParticipantStat VITALITY = new BattleParticipantStat(new LiteralText("Vitality"), false, false);
    public static final BattleParticipantStat INTELLIGENCE = new BattleParticipantStat(new LiteralText("Intelligence"), false, false);
    public static final BattleParticipantStat DEXTERITY = new BattleParticipantStat(new LiteralText("Dexterity"), false, false);
    public static final BattleParticipantStat WISDOM = new BattleParticipantStat(new LiteralText("Wisdom"), false, false);
    public static final BattleParticipantStat STRENGTH = new BattleParticipantStat(new LiteralText("Strength"), false, false);
    public static final BattleParticipantStat PERCEPTION = new BattleParticipantStat(new LiteralText("Perception"), false, false);

    public static void init() {
        final Registry<BattleParticipantStat> registry = io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStats.REGISTRY;
        Registry.register(registry, TBCExCharacter.createId("vitality"), VITALITY);
        Registry.register(registry, TBCExCharacter.createId("intelligence"), INTELLIGENCE);
        Registry.register(registry, TBCExCharacter.createId("dexterity"), DEXTERITY);
        Registry.register(registry, TBCExCharacter.createId("wisdom"), WISDOM);
        Registry.register(registry, TBCExCharacter.createId("strength"), STRENGTH);
        Registry.register(registry, TBCExCharacter.createId("perception"), PERCEPTION);
    }

    private CharacterBattleParticipantStats() {
    }
}
