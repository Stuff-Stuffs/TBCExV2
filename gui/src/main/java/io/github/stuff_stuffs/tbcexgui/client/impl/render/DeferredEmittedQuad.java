package io.github.stuff_stuffs.tbcexgui.client.impl.render;

import io.github.stuff_stuffs.tbcexgui.client.api.GuiQuad;
import io.github.stuff_stuffs.tbcexgui.client.api.GuiRenderMaterial;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;

import java.util.Arrays;

public class DeferredEmittedQuad {
    private final MutableGuiQuadImpl delegate = new MutableGuiQuadImpl();

    public DeferredEmittedQuad() {
    }

    public void copy(final MutableGuiQuadImpl quad) {
        delegate.renderMaterial(quad.renderMaterial());
        delegate.tag(quad.tag());
        for (int i = 0; i < 4; i++) {
            delegate.pos(i, quad.x(i), quad.y(i));
            delegate.sprite(i, quad.spriteU(i), quad.spriteV(i));
            delegate.colour(i, quad.colour(i));
            delegate.light(i, quad.light(i));
            delegate.depth(i, quad.depthByIndex(i));
        }
    }

    public void emit(final VertexConsumerProvider vertexConsumers) {
        final GuiRenderMaterialImpl renderMaterial = (GuiRenderMaterialImpl) delegate.renderMaterial();
        final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderMaterial.getRenderLayer());
        final int colourModifier = renderMaterial.translucent() ? 0 : 0xFF000000;
        for (int i = 0; i < 4; i++) {
            vertexConsumer.vertex(delegate.x(i), delegate.y(i), delegate.depthByIndex(i));
            vertexConsumer.color(delegate.colour(i) | colourModifier);
            if (!renderMaterial.ignoreTexture()) {
                vertexConsumer.texture(delegate.spriteU(i), delegate.spriteV(i));
            }
            if (!renderMaterial.ignoreLight()) {
                final int packedLight = delegate.light(i);
                vertexConsumer.light(LightmapTextureManager.getBlockLightCoordinates(packedLight), LightmapTextureManager.getSkyLightCoordinates(packedLight));
            }
            vertexConsumer.next();
        }
    }

    public double depth(int vertexIndex) {
        return delegate.depthByIndex(vertexIndex);
    }

    public double depth() {
        float sum = 0;
        for (int i = 0; i < 4; i++) {
            sum += depth(i);
        }
        return sum * 0.25;
    }

    public GuiRenderMaterial material() {
        return delegate.renderMaterial();
    }
}
