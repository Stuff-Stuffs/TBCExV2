package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.stat;

import com.mojang.serialization.Lifecycle;
import io.github.stuff_stuffs.tbcexcore.common.TBCExCore;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.text.LiteralText;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public final class BattleParticipantStats {
    public static final Registry<BattleParticipantStat> REGISTRY = FabricRegistryBuilder.from(new SimpleRegistry<>(RegistryKey.ofRegistry(TBCExCore.createId("stats")), Lifecycle.stable(), BattleParticipantStat::getEntry)).buildAndRegister();
    public static final BattleParticipantStat MAX_HEALTH = new BattleParticipantStat(new LiteralText("Max Health"), true, false);
    public static final BattleParticipantStat MAX_ENERGY = new BattleParticipantStat(new LiteralText("Max Energy"), true, false);
    public static final BattleParticipantStat SHROUD = new BattleParticipantStat(new LiteralText("Shroud"), true, false);


    public static void init() {
        Registry.register(REGISTRY, TBCExCore.createId("max_health"), MAX_HEALTH);
        Registry.register(REGISTRY, TBCExCore.createId("max_energy"), MAX_ENERGY);
        Registry.register(REGISTRY, TBCExCore.createId("shroud"), SHROUD);
    }

    private BattleParticipantStats() {
    }
}
