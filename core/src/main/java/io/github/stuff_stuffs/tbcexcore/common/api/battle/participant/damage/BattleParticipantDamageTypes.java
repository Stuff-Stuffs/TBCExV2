package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.damage;

import com.mojang.serialization.Lifecycle;
import io.github.stuff_stuffs.tbcexcore.common.TBCExCore;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

import java.util.Set;

public final class BattleParticipantDamageTypes {
    public static final Registry<BattleParticipantDamageType> REGISTRY = FabricRegistryBuilder.from(new SimpleRegistry<>(RegistryKey.ofRegistry(TBCExCore.createId("battle_participant_damage_type")), Lifecycle.stable(), BattleParticipantDamageType::getReference)).buildAndRegister();
    public static final BattleParticipantDamageType ROOT = new BattleParticipantDamageType(Set.of());

    public static void init() {
        Registry.register(REGISTRY, TBCExCore.createId("root"), ROOT);
    }

    private BattleParticipantDamageTypes() {
    }
}
