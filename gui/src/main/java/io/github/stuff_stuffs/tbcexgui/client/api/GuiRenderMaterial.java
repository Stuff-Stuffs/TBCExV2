package io.github.stuff_stuffs.tbcexgui.client.api;

import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;

public interface GuiRenderMaterial extends Comparable<GuiRenderMaterial> {
    GuiRenderMaterial POS_COLOUR = GuiRenderMaterialFinder.finder().depthTest(true).ignoreLight(true).ignoreTexture(true).translucent(false).find().remember(new Identifier("tbcexgui", "pos_colour"));
    GuiRenderMaterial POS_COLOUR_TRANSLUCENT = GuiRenderMaterialFinder.finder().depthTest(true).ignoreLight(true).ignoreTexture(true).translucent(true).find().remember(new Identifier("tbcexgui", "pos_colour_translucent"));
    GuiRenderMaterial POS_COLOUR_TEXTURE = GuiRenderMaterialFinder.finder().depthTest(true).ignoreLight(true).texture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).translucent(false).find().remember(new Identifier("tbcexgui", "pos_colour_texture"));
    GuiRenderMaterial POS_COLOUR_TEXTURE_TRANSLUCENT = GuiRenderMaterialFinder.finder().depthTest(true).ignoreLight(true).texture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).translucent(true).find().remember(new Identifier("tbcexgui", "pos_colour_texture_translucent"));
    GuiRenderMaterial POS_COLOUR_TEXTURE_TRANSLUCENT_LIGHT_CULL = GuiRenderMaterialFinder.finder().cull(true).depthTest(true).ignoreLight(false).texture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).translucent(true).find().remember(new Identifier("tbcexgui", "pos_colour_texture_translucent"));

    boolean depthTest();

    boolean cull();

    boolean translucent();

    boolean ignoreTexture();

    boolean ignoreLight();

    String shader();

    Identifier texture();

    GuiRenderMaterial remember(Identifier id);
}
