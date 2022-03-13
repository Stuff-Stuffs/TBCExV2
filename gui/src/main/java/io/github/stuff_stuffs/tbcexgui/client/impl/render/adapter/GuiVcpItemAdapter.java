package io.github.stuff_stuffs.tbcexgui.client.impl.render.adapter;

import io.github.stuff_stuffs.tbcexgui.client.api.GuiRenderMaterial;
import io.github.stuff_stuffs.tbcexgui.client.impl.GuiContextImpl;
import io.github.stuff_stuffs.tbcexgui.client.impl.render.MutableGuiQuadImpl;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import io.github.stuff_stuffs.tbcexutil.common.colour.IntRgbColour;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;

public class GuiVcpItemAdapter implements VertexConsumerProvider {
    private final GuiContextImpl context;
    private final Adapter entityTranslucentCullAdapter = new Adapter(GuiRenderMaterial.POS_COLOUR_TEXTURE_TRANSLUCENT_LIGHT_CULL);
    private final Adapter itemEntityTranslucentCullAdapter = new Adapter(GuiRenderMaterial.POS_COLOUR_TEXTURE_TRANSLUCENT_LIGHT_CULL);
    private final MatrixStack stack = new MatrixStack();

    public GuiVcpItemAdapter(final GuiContextImpl context) {
        this.context = context;
    }

    public MatrixStack getStack() {
        return stack;
    }

    @Override
    public VertexConsumer getBuffer(final RenderLayer layer) {
        if (layer == TexturedRenderLayers.getEntityTranslucentCull()) {
            return entityTranslucentCullAdapter;
        } else if (layer == TexturedRenderLayers.getItemEntityTranslucentCull()) {
            return itemEntityTranslucentCullAdapter;
        }
        throw new TBCExException("Tried getting arbitrary render layer from item adapter");
    }

    private final class Adapter implements VertexConsumer {
        private final MutableGuiQuadImpl quadDelegate = new MutableGuiQuadImpl();
        private final GuiRenderMaterial renderMaterial;
        private int index = 0;

        private Adapter(final GuiRenderMaterial renderMaterial) {
            this.renderMaterial = renderMaterial;
        }

        @Override
        public VertexConsumer vertex(final double x, final double y, final double z) {
            quadDelegate.pos(index, (float) x, (float) y);
            quadDelegate.depth(index, (float) z);
            return this;
        }

        @Override
        public VertexConsumer color(final int red, final int green, final int blue, final int alpha) {
            quadDelegate.colour(index, new IntRgbColour(red, green, blue).pack(alpha));
            return this;
        }

        @Override
        public VertexConsumer texture(final float u, final float v) {
            quadDelegate.sprite(index, u, v);
            return this;
        }

        @Override
        public VertexConsumer overlay(final int u, final int v) {
            return this;
        }

        @Override
        public VertexConsumer light(final int u, final int v) {
            quadDelegate.light(index, LightmapTextureManager.pack(u, v));
            return this;
        }

        @Override
        public VertexConsumer normal(final float x, final float y, final float z) {
            return this;
        }

        @Override
        public void next() {
            index++;
            if (index == 4) {
                quadDelegate.renderMaterial(renderMaterial);
                if (context.transformQuad(quadDelegate)) {
                    context.acquireDeferred().copy(quadDelegate);
                }
                index = 0;
                quadDelegate.reset();
            }
        }

        @Override
        public void fixedColor(final int red, final int green, final int blue, final int alpha) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void unfixColor() {
            throw new UnsupportedOperationException();
        }
    }
}
