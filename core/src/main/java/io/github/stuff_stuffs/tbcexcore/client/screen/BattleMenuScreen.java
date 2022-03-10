package io.github.stuff_stuffs.tbcexcore.client.screen;

import io.github.stuff_stuffs.tbcexgui.client.screen.TBCExScreen;
import io.github.stuff_stuffs.tbcexgui.client.widget.PositionedWidget;
import io.github.stuff_stuffs.tbcexgui.client.widget.StackingWidget;
import io.github.stuff_stuffs.tbcexgui.client.widget.Widget;
import io.github.stuff_stuffs.tbcexgui.client.widget.interaction.SelectionWheelWidget;
import io.github.stuff_stuffs.tbcexgui.client.widget.panel.RootPanelWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.List;

public class BattleMenuScreen extends TBCExScreen {
    private final PlayerEntity entity;
    private StackingWidget menuHolder;

    public BattleMenuScreen(final PlayerEntity entity) {
        super(new LiteralText("Battle Menu"), new RootPanelWidget(false));
        this.entity = entity;
        final SelectionWheelWidget menuSelector = SelectionWheelWidget.builder().addEntry(Widget.NON_HOVER, 192, Widget.HOVER, 192, () -> new LiteralText("Inventory"), List.of(new LiteralText("Open your inventory").asOrderedText()), () -> true, () -> {
            menuHolder.push(new RootPanelWidget(true), new LiteralText("Inventory"));
        }).addEntry(Widget.NON_HOVER, 192, Widget.HOVER, 192, () -> new LiteralText("Self Stats"), List.of(new LiteralText("View Your Own Stats").asOrderedText()), () -> true, () -> {

        }).addEntry(Widget.NON_HOVER, 192, Widget.HOVER, 192, () -> new LiteralText("Stats"), List.of(new LiteralText("View Another Creature Or Your Own Stats").asOrderedText()), () -> true, () -> {

        }).addEntry(Widget.NON_HOVER, 192, Widget.HOVER, 192, () -> new LiteralText("Move"), List.of(new LiteralText("View Another Creature Or Your Own Stats").asOrderedText()), () -> true, () -> {

        }).build(0.1, 0.3, 0.3125);
        final RootPanelWidget selectorHolder = new RootPanelWidget(false);
        selectorHolder.addChild(PositionedWidget.wrap(menuSelector, 0.5, 0.5));
        menuHolder = new StackingWidget(selectorHolder, new LiteralText("Main Menu"), 0.05, () -> new LiteralText("Back To Game").asOrderedText(), this::close);
        ((RootPanelWidget) widget).addChild(PositionedWidget.wrap(menuHolder, 0.0, 0.0));
    }
}
