package io.github.stuff_stuffs.tbcexgui.client.impl.render;

import io.github.stuff_stuffs.tbcexgui.client.api.GuiRenderMaterial;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class GuiRenderMaterialImpl implements GuiRenderMaterial {
    private final boolean depthTest;
    private final boolean translucent;
    private final boolean ignoreTexture;
    private final boolean ignoreLight;
    private final boolean cull;
    private final String shader;
    private final Identifier texture;
    private final RenderLayer renderLayer;

    public GuiRenderMaterialImpl(final boolean depthTest, final boolean translucent, final boolean ignoreTexture, final boolean ignoreLight, boolean cull, final String shader, final Identifier texture, final RenderLayer renderLayer) {
        this.depthTest = depthTest;
        this.translucent = translucent;
        this.ignoreTexture = ignoreTexture;
        this.ignoreLight = ignoreLight;
        this.cull = cull;
        this.shader = shader;
        this.texture = texture;
        this.renderLayer = renderLayer;
    }

    @Override
    public boolean depthTest() {
        return depthTest;
    }

    @Override
    public boolean cull() {
        return cull;
    }

    @Override
    public boolean translucent() {
        return translucent;
    }

    @Override
    public boolean ignoreTexture() {
        return ignoreTexture;
    }

    @Override
    public boolean ignoreLight() {
        return ignoreLight;
    }

    @Override
    public String shader() {
        return shader;
    }

    @Override
    public Identifier texture() {
        return texture;
    }

    @Override
    public GuiRenderMaterial remember(final Identifier id) {
        GuiRenderMaterialFinderImpl.remember(id, this);
        return this;
    }

    public RenderLayer getRenderLayer() {
        return renderLayer;
    }

    @Override
    public int compareTo(final GuiRenderMaterial o) {
        if (depthTest ^ o.depthTest()) {
            return depthTest ? -1 : 1;
        }
        return shader.compareTo(o.shader());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GuiRenderMaterialImpl that)) {
            return false;
        }

        if (depthTest != that.depthTest) {
            return false;
        }
        if (translucent != that.translucent) {
            return false;
        }
        if (ignoreTexture != that.ignoreTexture) {
            return false;
        }
        if (ignoreLight != that.ignoreLight) {
            return false;
        }
        if (!shader.equals(that.shader)) {
            return false;
        }
        return texture.equals(that.texture);
    }

    @Override
    public int hashCode() {
        int result = (depthTest ? 1 : 0);
        result = 31 * result + (translucent ? 1 : 0);
        result = 31 * result + (ignoreTexture ? 1 : 0);
        result = 31 * result + (ignoreLight ? 1 : 0);
        result = 31 * result + shader.hashCode();
        result = 31 * result + texture.hashCode();
        return result;
    }
}
