package io.github.stuff_stuffs.tbcexgui.client.widget;

import io.github.stuff_stuffs.tbcexgui.client.api.GuiContext;
import io.github.stuff_stuffs.tbcexutil.common.colour.Colour;
import io.github.stuff_stuffs.tbcexutil.common.colour.IntRgbColour;

public interface Widget {
    Colour NON_HOVER = new IntRgbColour(0x000000);
    Colour HOVER = new IntRgbColour(0x373737);

    void resize(double width, double height);

    void render(GuiContext context);

    String getDebugName();
}
