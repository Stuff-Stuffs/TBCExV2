package io.github.stuff_stuffs.tbcexgui.client.widget;

import io.github.stuff_stuffs.tbcexgui.client.api.GuiContext;

import java.util.function.BooleanSupplier;

public class HidingWidget extends AbstractWidget {
    private final Widget delegate;
    private final BooleanSupplier enabled;

    public HidingWidget(final Widget delegate, final BooleanSupplier enabled) {
        this.delegate = delegate;
        this.enabled = enabled;
    }

    @Override
    public void render(final GuiContext context) {
        context.enterSection(getDebugName());
        if (enabled.getAsBoolean()) {
            delegate.render(context);
        }
        context.exitSection(getDebugName());
    }

    @Override
    public String getDebugName() {
        return "HidingWidget";
    }

    @Override
    public void resize(final double width, final double height) {
        super.resize(width, height);
        delegate.resize(width, height);
    }
}
