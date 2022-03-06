package io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.restore;

import com.mojang.serialization.Lifecycle;
import io.github.stuff_stuffs.tbcexcore.common.TBCExCore;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public final class RestoreDataTypes {
    public static final Registry<RestoreDataType<?>> REGISTRY = FabricRegistryBuilder.from(new SimpleRegistry<RestoreDataType<?>>(RegistryKey.ofRegistry(TBCExCore.createId("battle_participant_restore_data")), Lifecycle.stable(), null)).buildAndRegister();

    public static void init() {
    }

    private RestoreDataTypes() {
    }
}
