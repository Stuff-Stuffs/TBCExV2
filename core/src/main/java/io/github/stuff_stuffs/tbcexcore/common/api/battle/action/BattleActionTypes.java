package io.github.stuff_stuffs.tbcexcore.common.api.battle.action;

import com.mojang.serialization.Lifecycle;
import io.github.stuff_stuffs.tbcexcore.common.TBCExCore;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.action.BattleResizeAction;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.action.ParticipantJoinBattleAction;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.action.ParticipantWalkBattleAction;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public final class BattleActionTypes {
    public static final Registry<BattleActionType<?>> REGISTRY = FabricRegistryBuilder.from(new SimpleRegistry<BattleActionType<?>>(RegistryKey.ofRegistry(TBCExCore.createId("battle_action")), Lifecycle.stable(), BattleActionType::getReference)).buildAndRegister();
    public static final BattleActionType<ParticipantJoinBattleAction> PARTICIPANT_JOIN_BATTLE_ACTION_TYPE = new BattleActionType<>(ParticipantJoinBattleAction.CODEC);
    public static final BattleActionType<BattleResizeAction> BATTLE_RESIZE_ACTION_TYPE = new BattleActionType<>(BattleResizeAction.CODEC);
    public static final BattleActionType<ParticipantWalkBattleAction> PARTICIPANT_WALK_BATTLE_ACTION_TYPE = new BattleActionType<>(ParticipantWalkBattleAction.CODEC);

    public static void init() {
        Registry.register(REGISTRY, TBCExCore.createId("join"), PARTICIPANT_JOIN_BATTLE_ACTION_TYPE);
        Registry.register(REGISTRY, TBCExCore.createId("resize"), BATTLE_RESIZE_ACTION_TYPE);
        Registry.register(REGISTRY, TBCExCore.createId("walk"), PARTICIPANT_WALK_BATTLE_ACTION_TYPE);
    }

    private BattleActionTypes() {
    }
}
