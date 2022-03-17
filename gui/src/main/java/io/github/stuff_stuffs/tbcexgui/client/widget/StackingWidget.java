package io.github.stuff_stuffs.tbcexgui.client.widget;

import io.github.stuff_stuffs.tbcexgui.client.api.GuiContext;
import io.github.stuff_stuffs.tbcexgui.client.api.GuiInputContext;
import io.github.stuff_stuffs.tbcexgui.client.api.GuiQuadEmitter;
import io.github.stuff_stuffs.tbcexgui.client.api.GuiRenderMaterial;
import io.github.stuff_stuffs.tbcexgui.client.api.text.TextDrawer;
import io.github.stuff_stuffs.tbcexgui.client.api.text.TextDrawers;
import io.github.stuff_stuffs.tbcexutil.common.Rect2d;
import io.github.stuff_stuffs.tbcexutil.common.Vec2d;
import io.github.stuff_stuffs.tbcexutil.common.colour.IntRgbColour;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class StackingWidget extends AbstractWidget {
    private static final Quaternion QUARTER_TURN = Vec3f.POSITIVE_Z.getDegreesQuaternion(90);
    private final Entry root;
    private final double buttonThickness;
    private final Supplier<OrderedText> rootButtonText;
    private final Runnable onClickRootButton;
    private final List<Entry> entries = new ArrayList<>();

    public StackingWidget(final Widget root, final Text rootName, final double buttonThickness, final Supplier<OrderedText> rootButtonText, final Runnable onClickRootButton) {
        this.rootButtonText = rootButtonText;
        this.onClickRootButton = onClickRootButton;
        this.root = new Entry(rootName, root);
        this.buttonThickness = buttonThickness;
    }

    public void push(final Widget widget, final Text name) {
        double x = getScreenWidth() - buttonThickness;
        final double y;
        if (x < 1) {
            y = getScreenHeight() / x;
            x = 1;
        } else {
            y = getScreenHeight();
        }
        widget.resize(x, y);
        entries.add(new Entry(name, widget));
    }

    public void pop() {
        if (!entries.isEmpty()) {
            entries.remove(entries.size() - 1);
        }
    }

    private @Nullable Entry getLast() {
        return entries.isEmpty() ? null : entries.size() == 1 ? root : entries.get(entries.size() - 1);
    }

    private Entry getTop() {
        return entries.isEmpty() ? root : entries.get(entries.size() - 1);
    }

    public Widget getTopWidget() {
        return getTop().widget;
    }

    @Override
    public void resize(final double width, final double height) {
        super.resize(width, height);
        final double x = getScreenWidth() - buttonThickness;
        final double y;
        y = getScreenHeight();
        root.widget.resize(x, y);
        for (final Entry entry : entries) {
            entry.widget.resize(x, y);
        }
    }

    @Override
    public void render(final GuiContext context) {
        final String decorationSection = getDebugName() + ".decoration";
        context.enterSection(decorationSection);
        final Entry last = getLast();
        final GuiQuadEmitter emitter = context.getEmitter();
        final Rect2d rect = new Rect2d(1 + (getScreenWidth() - 1) / 2 - buttonThickness, -(getScreenHeight() - 1) / 2, getScreenWidth(), 1 + (getScreenHeight() - 1) / 2);
        processEvents(context, event -> {
            if (event instanceof GuiInputContext.MouseClick click) {
                final Vec2d mouse = context.transformMouseCursor(new Vec2d(click.mouseX, click.mouseY));
                if (rect.isIn(mouse.x, mouse.y)) {
                    if (entries.isEmpty()) {
                        onClickRootButton.run();
                    } else {
                        pop();
                    }
                    return true;
                }
            }
            return false;
        });
        final Vec2d mouseCursor = context.transformMouseCursor();
        final int c = (rect.isIn(mouseCursor.x, mouseCursor.y) ? HOVER : NON_HOVER).pack(192);
        emitter.rectangle(1 + (getScreenWidth() - 1) / 2 - buttonThickness, -(getScreenHeight() - 1) / 2, buttonThickness, getScreenHeight(), c, c, c, c);
        emitter.renderMaterial(GuiRenderMaterial.POS_COLOUR_TRANSLUCENT);
        emitter.emit();

        final TextDrawer drawer = TextDrawers.oneShot(TextDrawers.HorizontalJustification.CENTER, TextDrawers.VerticalJustification.CENTER, IntRgbColour.WHITE.pack(), 0, false);
        context.pushTranslate(1 + (getScreenWidth() - 1) / 2.0 - buttonThickness / 2.0, 0.5, 0.01);
        context.pushRotate(QUARTER_TURN);
        final OrderedText text;
        if (last != null) {
            text = last.name.asOrderedText();
        } else {
            text = rootButtonText.get();
        }
        drawer.draw(getScreenHeight(), buttonThickness, text, context);
        context.popGuiTransform();
        context.popGuiTransform();
        context.exitSection(decorationSection);

        final String childSection = getDebugName() + ".child";
        context.enterSection(childSection);
        context.pushTranslate(-buttonThickness / 2.0, 0, 1);
        getTop().widget.render(context);
        context.popGuiTransform();

        processEvents(context, event -> {
            if(event instanceof GuiInputContext.KeyPress keyPress) {
                if(keyPress.keyCode== GLFW.GLFW_KEY_ESCAPE) {
                    if (entries.isEmpty()) {
                        onClickRootButton.run();
                    } else {
                        pop();
                    }
                    return true;
                }
            }
            return false;
        });

        context.exitSection(childSection);
    }

    @Override
    public String getDebugName() {
        return "StackingWidget";
    }

    private static final class Entry {
        private final Text name;
        private final Widget widget;

        private Entry(final Text name, final Widget widget) {
            this.name = name;
            this.widget = widget;
        }
    }
}
