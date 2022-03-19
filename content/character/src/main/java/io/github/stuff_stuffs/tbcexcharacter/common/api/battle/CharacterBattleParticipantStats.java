package io.github.stuff_stuffs.tbcexcharacter.common.api.battle;

import io.github.stuff_stuffs.tbcexcharacter.common.TBCExCharacter;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat.BattleParticipantStat;
import net.minecraft.util.registry.Registry;

public final class CharacterBattleParticipantStats {
    public static final BattleParticipantStat VITALITY = new BattleParticipantStat();
    public static final BattleParticipantStat INTELLIGENCE = new BattleParticipantStat();
    public static final BattleParticipantStat DEXTERITY = new BattleParticipantStat();
    public static final BattleParticipantStat WISDOM = new BattleParticipantStat();
    public static final BattleParticipantStat STRENGTH = new BattleParticipantStat();
    public static final BattleParticipantStat PERCEPTION = new BattleParticipantStat();

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
