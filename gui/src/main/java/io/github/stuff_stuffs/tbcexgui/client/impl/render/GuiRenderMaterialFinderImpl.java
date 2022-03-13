package io.github.stuff_stuffs.tbcexgui.client.impl.render;

import io.github.stuff_stuffs.tbcexgui.client.api.GuiRenderMaterial;
import io.github.stuff_stuffs.tbcexgui.client.api.GuiRenderMaterialFinder;
import io.github.stuff_stuffs.tbcexgui.client.render.GuiRenderLayers;
import io.github.stuff_stuffs.tbcexutil.common.StringInterpolator;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class GuiRenderMaterialFinderImpl implements GuiRenderMaterialFinder {
    private static final MaterialFactory[] FACTORIES;
    private static final Map<Identifier, GuiRenderMaterialImpl> NAMED_MATERIALS = new Object2ReferenceOpenHashMap<>();
    private final boolean depthTest;
    private final boolean translucent;
    private final boolean ignoreTexture;
    private final boolean ignoreLight;
    private final boolean cull;
    private final String textureShader;
    private final String noTextureShader;
    private final Identifier texture;

    public GuiRenderMaterialFinderImpl(final boolean depthTest, final boolean translucent, final boolean ignoreTexture, final boolean ignoreLight, boolean cull, final String textureShader, final String noTextureShader, final Identifier texture) {
        this.depthTest = depthTest;
        this.translucent = translucent;
        this.ignoreTexture = ignoreTexture;
        this.ignoreLight = ignoreLight;
        this.cull = cull;
        this.textureShader = textureShader;
        this.noTextureShader = noTextureShader;
        this.texture = texture;
    }

    @Override
    public GuiRenderMaterialFinder depthTest(final boolean depthTest) {
        return new GuiRenderMaterialFinderImpl(depthTest, translucent, ignoreTexture, ignoreLight, cull, textureShader, noTextureShader, texture);
    }

    public GuiRenderMaterialFinder cull(boolean cull) {
        return new GuiRenderMaterialFinderImpl(depthTest, translucent, ignoreTexture, ignoreLight, cull, textureShader, noTextureShader, texture);
    }

    @Override
    public GuiRenderMaterialFinder translucent(final boolean translucent) {
        return new GuiRenderMaterialFinderImpl(depthTest, translucent, ignoreTexture, ignoreLight, cull, textureShader, noTextureShader, texture);
    }

    @Override
    public GuiRenderMaterialFinder ignoreTexture(final boolean ignoreTexture) {
        return new GuiRenderMaterialFinderImpl(depthTest, translucent, ignoreTexture, ignoreLight, cull, textureShader, noTextureShader, texture);
    }

    @Override
    public GuiRenderMaterialFinder shader(final String textureShader, final String noTextureShader) {
        return new GuiRenderMaterialFinderImpl(depthTest, translucent, ignoreTexture, ignoreLight, cull, textureShader, noTextureShader, texture);
    }

    @Override
    public GuiRenderMaterialFinder texture(final Identifier texture) {
        return new GuiRenderMaterialFinderImpl(depthTest, translucent, ignoreTexture, ignoreLight, cull, textureShader, noTextureShader, texture);
    }

    @Override
    public GuiRenderMaterialFinder ignoreLight(final boolean ignoreLight) {
        return new GuiRenderMaterialFinderImpl(depthTest, translucent, ignoreTexture, ignoreLight, cull, textureShader, noTextureShader, texture);
    }

    @Override
    public GuiRenderMaterial find() {
        return FACTORIES[getFactoryIndex(depthTest, translucent, ignoreTexture, ignoreLight, cull)].create(ignoreTexture ? noTextureShader : textureShader, texture);
    }

    @Override
    public @Nullable GuiRenderMaterial find(final Identifier id) {
        return retrieve(id);
    }

    public static void remember(final Identifier id, final GuiRenderMaterialImpl material) {
        if (NAMED_MATERIALS.putIfAbsent(id, material) == null) {
            GuiRenderLayers.addBuffer(material.getRenderLayer(), 1024);
        }
    }

    public static @Nullable GuiRenderMaterialImpl retrieve(final Identifier id) {
        return NAMED_MATERIALS.get(id);
    }

    private static int getFactoryIndex(final boolean depthTest, final boolean translucent, final boolean ignoreTexture, final boolean ignoreLight, boolean cull) {
        int key = 0;
        if (depthTest) {
            key |= 1;
        }
        if (translucent) {
            key |= 2;
        }
        if (ignoreTexture) {
            key |= 4;
        }
        if (ignoreLight) {
            key |= 8;
        }
        if(cull) {
            key |= 16;
        }
        return key;
    }

    private static class MaterialFactory {
        private static final StringInterpolator RENDER_LAYER_NAME = new StringInterpolator("gui_depthtest{}_translucent{}_ignoretexture{}_ignorelight{}_shader{}");
        private final boolean depthTest;
        private final boolean translucent;
        private final boolean ignoreTexture;
        private final boolean ignoreLight;
        private final boolean cull;
        private final Map<String, RenderLayer> cache;

        private MaterialFactory(final boolean depthTest, final boolean translucent, final boolean ignoreTexture, final boolean ignoreLight, boolean cull) {
            this.depthTest = depthTest;
            this.translucent = translucent;
            this.ignoreTexture = ignoreTexture;
            this.ignoreLight = ignoreLight;
            this.cull = cull;
            cache = new Object2ReferenceOpenHashMap<>(2);
        }

        public GuiRenderMaterialImpl create(final String shader, final Identifier texture) {
            return new GuiRenderMaterialImpl(depthTest, translucent, ignoreTexture, ignoreLight, cull, shader, texture, createRenderLayer(shader, texture));
        }

        private RenderLayer createRenderLayer(final String shader, final Identifier texture) {
            final VertexFormat vertexFormat = getVertexFormat();
            return cache.computeIfAbsent(shader + ":" + texture.toString(), s -> RenderLayer.of(
                            RENDER_LAYER_NAME.interpolate(depthTest, translucent, ignoreTexture, ignoreLight, shader),
                            vertexFormat,
                            VertexFormat.DrawMode.QUADS,
                            1024,
                            false,
                            translucent,
                            RenderLayer.MultiPhaseParameters.builder().
                                    cull(cull?GuiRenderLayers.CULL:GuiRenderLayers.NO_CULL).
                                    shader(GuiRenderLayers.getShader(shader, vertexFormat)).
                                    lightmap(ignoreLight ? GuiRenderLayers.DISABLE_LIGHTMAP : GuiRenderLayers.ENABLE_LIGHTMAP).
                                    depthTest(depthTest ? GuiRenderLayers.DEPTH_TEST : GuiRenderLayers.NO_DEPTH_TEST).
                                    texture(GuiRenderLayers.getTexture(texture)).
                                    target(translucent ? GuiRenderLayers.TRANSLUCENT_TARGET : GuiRenderLayers.MAIN_TARGET).
                                    transparency(translucent ? GuiRenderLayers.TRANSLUCENT_TRANSPARENCY : GuiRenderLayers.NO_TRANSPARENCY).
                                    writeMaskState(translucent ? GuiRenderLayers.COLOR_MASK : GuiRenderLayers.ALL_MASK).
                                    build(false)
                    )
            );
        }

        private VertexFormat getVertexFormat() {
            if (ignoreTexture) {
                return ignoreLight ? VertexFormats.POSITION_COLOR : VertexFormats.POSITION_COLOR_LIGHT;
            } else {
                return ignoreLight ? VertexFormats.POSITION_COLOR_TEXTURE : VertexFormats.POSITION_COLOR_TEXTURE_LIGHT;
            }
        }
    }

    static {
        FACTORIES = new MaterialFactory[32];
        boolean cull = false;
        FACTORIES[getFactoryIndex(false, false, false, false, cull)] = new MaterialFactory(false, false, false, false, cull);
        FACTORIES[getFactoryIndex(false, false, true, false, cull)] = new MaterialFactory(false, false, true, false, cull);
        FACTORIES[getFactoryIndex(false, true, false, false, cull)] = new MaterialFactory(false, true, false, false, cull);
        FACTORIES[getFactoryIndex(false, true, true, false, cull)] = new MaterialFactory(false, true, true, false, cull);
        FACTORIES[getFactoryIndex(true, false, false, false, cull)] = new MaterialFactory(true, false, false, false, cull);
        FACTORIES[getFactoryIndex(true, false, true, false, cull)] = new MaterialFactory(true, false, true, false, cull);
        FACTORIES[getFactoryIndex(true, true, false, false, cull)] = new MaterialFactory(true, true, false, false, cull);
        FACTORIES[getFactoryIndex(true, true, true, false, cull)] = new MaterialFactory(true, true, true, false, cull);
        FACTORIES[getFactoryIndex(false, false, false, true, cull)] = new MaterialFactory(false, false, false, true, cull);
        FACTORIES[getFactoryIndex(false, false, true, true, cull)] = new MaterialFactory(false, false, true, true, cull);
        FACTORIES[getFactoryIndex(false, true, false, true, cull)] = new MaterialFactory(false, true, false, true, cull);
        FACTORIES[getFactoryIndex(false, true, true, true, cull)] = new MaterialFactory(false, true, true, true, cull);
        FACTORIES[getFactoryIndex(true, false, false, true, cull)] = new MaterialFactory(true, false, false, true, cull);
        FACTORIES[getFactoryIndex(true, false, true, true, cull)] = new MaterialFactory(true, false, true, true, cull);
        FACTORIES[getFactoryIndex(true, true, false, true, cull)] = new MaterialFactory(true, true, false, true, cull);
        FACTORIES[getFactoryIndex(true, true, true, true, cull)] = new MaterialFactory(true, true, true, true, cull);

        cull = true;
        FACTORIES[getFactoryIndex(false, false, false, false, cull)] = new MaterialFactory(false, false, false, false, cull);
        FACTORIES[getFactoryIndex(false, false, true, false, cull)] = new MaterialFactory(false, false, true, false, cull);
        FACTORIES[getFactoryIndex(false, true, false, false, cull)] = new MaterialFactory(false, true, false, false, cull);
        FACTORIES[getFactoryIndex(false, true, true, false, cull)] = new MaterialFactory(false, true, true, false, cull);
        FACTORIES[getFactoryIndex(true, false, false, false, cull)] = new MaterialFactory(true, false, false, false, cull);
        FACTORIES[getFactoryIndex(true, false, true, false, cull)] = new MaterialFactory(true, false, true, false, cull);
        FACTORIES[getFactoryIndex(true, true, false, false, cull)] = new MaterialFactory(true, true, false, false, cull);
        FACTORIES[getFactoryIndex(true, true, true, false, cull)] = new MaterialFactory(true, true, true, false, cull);
        FACTORIES[getFactoryIndex(false, false, false, true, cull)] = new MaterialFactory(false, false, false, true, cull);
        FACTORIES[getFactoryIndex(false, false, true, true, cull)] = new MaterialFactory(false, false, true, true, cull);
        FACTORIES[getFactoryIndex(false, true, false, true, cull)] = new MaterialFactory(false, true, false, true, cull);
        FACTORIES[getFactoryIndex(false, true, true, true, cull)] = new MaterialFactory(false, true, true, true, cull);
        FACTORIES[getFactoryIndex(true, false, false, true, cull)] = new MaterialFactory(true, false, false, true, cull);
        FACTORIES[getFactoryIndex(true, false, true, true, cull)] = new MaterialFactory(true, false, true, true, cull);
        FACTORIES[getFactoryIndex(true, true, false, true, cull)] = new MaterialFactory(true, true, false, true, cull);
        FACTORIES[getFactoryIndex(true, true, true, true, cull)] = new MaterialFactory(true, true, true, true, cull);
    }
}
