package io.github.stuff_stuffs.tbcextest.common;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleTimeline;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.action.ActionTrace;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.BattleParticipant;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItemType;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItemTypes;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.action.BattleResizeAction;
import io.github.stuff_stuffs.tbcexcore.common.impl.battle.world.ServerBattleWorldImpl;
import io.github.stuff_stuffs.tbcexcore.mixin.api.BattleWorldHolder;
import io.github.stuff_stuffs.tbcextest.common.battle.item.TestBattleItem;
import io.github.stuff_stuffs.tbcextest.common.item.TestItem;
import io.github.stuff_stuffs.tbcexutil.common.BattleBounds;
import io.github.stuff_stuffs.tbcexutil.common.Tracer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class TBCExTest implements ModInitializer {
    public static final BattleParticipantItemType<TestBattleItem> TEST_BATTLE_ITEM_TYPE = new BattleParticipantItemType<>(TestBattleItem.CODEC);
    public static final String MOD_ID = "tbcextest";
    public static final ItemGroup TEST_ITEM_GROUP = FabricItemGroupBuilder.create(createId("test_group")).build();
    public static final int TEST_ITEM_COUNT = 32;
    public static final Item[] TEST_ITEMS = new Item[TEST_ITEM_COUNT];

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> dispatcher.register(CommandManager.literal("createBattle").requires(source -> source.hasPermissionLevel(2)).executes(context -> {
            final ServerWorld world = context.getSource().getWorld();
            final ServerBattleWorldImpl battleWorld = (ServerBattleWorldImpl) ((BattleWorldHolder) world).tbcex$getBattleWorld();
            final BattleHandle handle = battleWorld.create();
            BlockPos pos = context.getSource().getEntity().getBlockPos();
            ((BattleTimeline)battleWorld.getBattle(handle).getTimeline()).push(new BattleResizeAction(new BattleBounds(pos.getX()-10, pos.getY()-10, pos.getZ()-1, pos.getX()+10, pos.getY()+10, pos.getZ()+10)), new Tracer<>(ActionTrace.TRACE, i -> false));
            final Entity entity = context.getSource().getEntity();
            if (entity instanceof BattleParticipant participant) {
                battleWorld.tryJoin(participant, handle);
            }
            return 0;
        })));
        Registry.register(BattleParticipantItemTypes.REGISTRY, createId("test_item"), TEST_BATTLE_ITEM_TYPE);
        for (int i = 0; i < TEST_ITEM_COUNT; i++) {
            TEST_ITEMS[i] = new TestItem(new FabricItemSettings().group(TEST_ITEM_GROUP), i);
            Registry.register(Registry.ITEM, createId("test_" + i), TEST_ITEMS[i]);
        }
    }

    public static Identifier createId(final String path) {
        return new Identifier(MOD_ID, path);
    }
}
