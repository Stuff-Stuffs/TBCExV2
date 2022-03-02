package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat;

import com.mojang.serialization.Lifecycle;
import io.github.stuff_stuffs.tbcexcore.common.TBCExCore;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public final class BattleParticipantStats {
    public static final Registry<BattleParticipantStat> REGISTRY = FabricRegistryBuilder.from(new SimpleRegistry<>(RegistryKey.ofRegistry(TBCExCore.createId("stats")), Lifecycle.stable(), BattleParticipantStat::getEntry)).buildAndRegister();
    public static final BattleParticipantStat MAX_HEALTH = new BattleParticipantStat();
    public static final BattleParticipantStat INTELLIGENCE = new BattleParticipantStat();
    public static final BattleParticipantStat DEXTERITY = new BattleParticipantStat();
    public static final BattleParticipantStat VITALITY = new BattleParticipantStat();
    public static final BattleParticipantStat STRENGTH = new BattleParticipantStat();
    public static final BattleParticipantStat PERCEPTION = new BattleParticipantStat();
    public static final BattleParticipantStat ENERGY = new BattleParticipantStat();

    public static void init() {
        Registry.register(REGISTRY, TBCExCore.createId("max_health"), MAX_HEALTH);
        Registry.register(REGISTRY, TBCExCore.createId("intelligence"), INTELLIGENCE);
        Registry.register(REGISTRY, TBCExCore.createId("dexterity"), DEXTERITY);
        Registry.register(REGISTRY, TBCExCore.createId("vitality"), VITALITY);
        Registry.register(REGISTRY, TBCExCore.createId("strength"), STRENGTH);
        Registry.register(REGISTRY, TBCExCore.createId("perception"), PERCEPTION);
        Registry.register(REGISTRY, TBCExCore.createId("energy"), ENERGY);
    }

    private BattleParticipantStats() {
    }
}
