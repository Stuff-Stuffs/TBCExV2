package io.github.stuff_stuffs.tbcexcore.common.api.battle.action;

import com.mojang.serialization.Lifecycle;
import io.github.stuff_stuffs.tbcexcore.common.TBCExCore;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.action.ParticipantJoinBattleAction;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public final class BattleActionTypes {
    public static final Registry<BattleActionType<?>> REGISTRY = FabricRegistryBuilder.from(new SimpleRegistry<BattleActionType<?>>(RegistryKey.ofRegistry(TBCExCore.createId("battle_action")), Lifecycle.stable(), BattleActionType::getReference)).buildAndRegister();
    public static final BattleActionType<ParticipantJoinBattleAction> PARTICIPANT_JOIN_BATTLE_TYPE = new BattleActionType<>(ParticipantJoinBattleAction.CODEC);

    public static void init() {
        Registry.register(REGISTRY, TBCExCore.createId("join"), PARTICIPANT_JOIN_BATTLE_TYPE);
    }

    private BattleActionTypes() {
    }
}
