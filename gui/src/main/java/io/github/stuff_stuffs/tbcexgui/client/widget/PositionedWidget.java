package io.github.stuff_stuffs.tbcexgui.client.widget;

import io.github.stuff_stuffs.tbcexgui.client.api.GuiContext;

public interface PositionedWidget extends Widget {
    double getX();

    double getY();

    static PositionedWidget wrap(final Widget widget, final double x, final double y) {
        return new PositionedWidget() {
            @Override
            public double getX() {
                return x;
            }

            @Override
            public double getY() {
                return y;
            }

            @Override
            public void resize(final double width, final double height) {
                widget.resize(width, height);
            }

            @Override
            public void render(final GuiContext context) {
                widget.render(context);
            }

            @Override
            public String getDebugName() {
                return widget.getDebugName();
            }
        };
    }
}
