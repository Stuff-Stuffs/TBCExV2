package io.github.stuff_stuffs.tbcexcore.client.render.screen;

import io.github.stuff_stuffs.tbcexcore.client.render.screen.widget.inventory.BattleInventoryWidget;
import io.github.stuff_stuffs.tbcexcore.client.render.screen.widget.move.BattleMoveWidget;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.Battle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.BattleWorld;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantInventoryView;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.state.BattleParticipantStateView;
import io.github.stuff_stuffs.tbcexcore.mixin.api.BattleHudSupplier;
import io.github.stuff_stuffs.tbcexcore.mixin.api.BattlePlayerEntity;
import io.github.stuff_stuffs.tbcexcore.mixin.api.BattleWorldHolder;
import io.github.stuff_stuffs.tbcexgui.client.api.MouseLockableScreen;
import io.github.stuff_stuffs.tbcexgui.client.screen.TBCExScreen;
import io.github.stuff_stuffs.tbcexgui.client.widget.PositionedWidget;
import io.github.stuff_stuffs.tbcexgui.client.widget.StackingWidget;
import io.github.stuff_stuffs.tbcexgui.client.widget.Widget;
import io.github.stuff_stuffs.tbcexgui.client.widget.interaction.SelectionWheelWidget;
import io.github.stuff_stuffs.tbcexgui.client.widget.panel.RootPanelWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BattleMenuScreen extends TBCExScreen implements MouseLockableScreen {
    private final PlayerEntity entity;
    private StackingWidget menuHolder;

    public BattleMenuScreen(final PlayerEntity entity) {
        super(new LiteralText("Battle Menu"), new RootPanelWidget(false));
        this.entity = entity;
        final SelectionWheelWidget menuSelector = SelectionWheelWidget.builder().addEntry(Widget.NON_HOVER, 192, Widget.HOVER, 192, () -> new LiteralText("Inventory"), List.of(new LiteralText("Open your inventory").asOrderedText()), () -> true, () -> {
            menuHolder.push(new BattleInventoryWidget(this::getInventory, this::close), new LiteralText("Inventory"));
        }).addEntry(Widget.NON_HOVER, 192, Widget.HOVER, 192, () -> new LiteralText("Self Stats"), List.of(new LiteralText("View Your Own Stats").asOrderedText()), () -> true, () -> {

        }).addEntry(Widget.NON_HOVER, 192, Widget.HOVER, 192, () -> new LiteralText("Stats"), List.of(new LiteralText("View Another Creature Or Your Own Stats").asOrderedText()), () -> true, () -> {

        }).addEntry(Widget.NON_HOVER, 192, Widget.HOVER, 192, () -> new LiteralText("Move"), List.of(new LiteralText("Move Around The Map ").asOrderedText()), () -> true, () -> {
            menuHolder.push(new BattleMoveWidget(this::getState, entity.world, ((BattleHudSupplier) MinecraftClient.getInstance().inGameHud).tbcex$getBattleHud().getContext(), this::setPassEvents), new LiteralText("MoveScreen"));
        }).build(0.1, 0.3, 0.3125);
        final RootPanelWidget selectorHolder = new RootPanelWidget(false);
        selectorHolder.addChild(PositionedWidget.wrap(menuSelector, 0.5, 0.5));
        menuHolder = new StackingWidget(selectorHolder, new LiteralText("Main Menu"), 0.05, () -> new LiteralText("Back To Game").asOrderedText(), this::close);
        ((RootPanelWidget) widget).addChild(PositionedWidget.wrap(menuHolder, 0.0, 0.0));
    }

    @Override
    public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        if (!(menuHolder.getTopWidget() instanceof BattleMoveWidget)) {
            passEvents = false;
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private BattleParticipantStateView getState() {
        final BattleHandle handle = ((BattlePlayerEntity) entity).tbcex$getCurrentBattle();
        if (handle == null) {
            return null;
        }
        final BattleWorld world = ((BattleWorldHolder) entity.world).tbcex$getBattleWorld();
        final Battle battle = world.getBattle(handle);
        if (battle == null) {
            return null;
        }
        return battle.getState().getParticipant(new BattleParticipantHandle(handle, entity.getUuid()));
    }

    private @Nullable BattleParticipantInventoryView getInventory() {
        final BattleHandle handle = ((BattlePlayerEntity) entity).tbcex$getCurrentBattle();
        if (handle == null) {
            return null;
        }
        final BattleWorld world = ((BattleWorldHolder) entity.world).tbcex$getBattleWorld();
        final Battle battle = world.getBattle(handle);
        if (battle == null) {
            return null;
        }
        final BattleParticipantStateView participant = battle.getState().getParticipant(new BattleParticipantHandle(handle, entity.getUuid()));
        if (participant == null) {
            return null;
        }
        return participant.getInventory();
    }

    private void setPassEvents(boolean val) {
        passEvents = val;
    }

    @Override
    public boolean shouldLockMouse() {
        return passEvents;
    }
}
