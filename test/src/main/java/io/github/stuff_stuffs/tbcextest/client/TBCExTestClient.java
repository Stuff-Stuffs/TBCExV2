package io.github.stuff_stuffs.tbcextest.client;

import io.github.stuff_stuffs.tbcexcore.client.render.TBCExCoreRenderRegistries;
import io.github.stuff_stuffs.tbcextest.common.TBCExTest;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.item.ItemStack;

public class TBCExTestClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TBCExCoreRenderRegistries.addItemRenderer(TBCExTest.TEST_BATTLE_ITEM_TYPE, (context, stack, state) -> {
            final ItemStack itemStack = stack.getItem().convert(stack.getCount()).get(0);
            context.renderItem(itemStack, LightmapTextureManager.MAX_LIGHT_COORDINATE, ModelTransformation.Mode.FIXED);
        });
    }
}
