package io.github.stuff_stuffs.tbcexgui.client.impl;

import io.github.stuff_stuffs.tbcexgui.client.api.GuiTextRenderer;
import io.github.stuff_stuffs.tbcexgui.client.api.text.OrderedTextUtil;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.text.OrderedText;
import net.minecraft.util.math.Matrix4f;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.List;

public class GuiTextRendererImpl implements GuiTextRenderer {
    private static final Matrix4f SCALE = Matrix4f.scale(1, 1, 1);
    private final TextRenderer delegate;
    private final GuiContextImpl context;

    public GuiTextRendererImpl(final TextRenderer delegate, final GuiContextImpl context) {
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    public int getHeight(final OrderedText text) {
        final MutableInt lines = new MutableInt(1);
        text.accept((index, style, codePoint) -> {
            if (codePoint == '\n') {
                lines.increment();
            }
            return true;
        });
        return delegate.fontHeight * lines.intValue();
    }

    @Override
    public int getWidth(final OrderedText text) {
        final List<OrderedText> split = OrderedTextUtil.split(text, codepoint -> codepoint == '\n');
        return split.stream().mapToInt(delegate::getWidth).max().orElse(0);
    }

    @Override
    public void render(final OrderedText text, final int colour, final boolean shadow, final int backgroundColour) {
        final boolean seeThrough = (colour & 0xFF_00_00_00) != 0xFF_00_00_00 || (backgroundColour != 0 && (backgroundColour & 0xFF_00_00_00) != 0xFF_00_00_00);
        delegate.draw(text, 0, 0, colour, shadow, SCALE, context.getTextAdapter(), seeThrough, backgroundColour, LightmapTextureManager.MAX_LIGHT_COORDINATE);
    }
}
