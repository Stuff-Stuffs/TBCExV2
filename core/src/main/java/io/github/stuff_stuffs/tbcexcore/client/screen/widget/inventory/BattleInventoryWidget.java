package io.github.stuff_stuffs.tbcexcore.client.screen.widget.inventory;

import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantInventoryHandle;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.BattleParticipantInventoryView;
import io.github.stuff_stuffs.tbcexcore.common.api.battle.participant.inventory.item.BattleParticipantItemStack;
import io.github.stuff_stuffs.tbcexgui.client.api.GuiContext;
import io.github.stuff_stuffs.tbcexgui.client.api.GuiRenderMaterial;
import io.github.stuff_stuffs.tbcexgui.client.widget.AbstractWidget;
import io.github.stuff_stuffs.tbcexutil.common.PairIterator;
import io.github.stuff_stuffs.tbcexutil.common.TBCExException;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class BattleInventoryWidget extends AbstractWidget {
    private static final double CATEGORY_WIDTH_FACTOR = 1 / 8.0;
    private static final double CATEGORY_HEIGHT_FACTOR = 2 / 3.0;
    private static final double SORTER_HEIGHT_FACTOR = 1 / 3.0;
    private static final double LIST_WIDTH_FACTOR = 4 / 8.0;
    private static final double PREVIEW_WIDTH_FACTOR = 3 / 8.0;
    private final Supplier<@Nullable BattleParticipantInventoryView> inventorySupplier;
    private final Runnable onInventoryNull;
    private final BattleInventoryCategorySelector categorySelector;
    private final BattleInventorySorterWidget sorter;
    private final BattleInventoryItemListWidget itemList;
    private final BattleInventoryPreviewWidget preview;
    private List<BattleParticipantInventoryHandle> prevHandles = List.of();
    private @Nullable BattleParticipantInventoryHandle lastSelected = null;

    public BattleInventoryWidget(final Supplier<@Nullable BattleParticipantInventoryView> inventorySupplier, final Runnable onInventoryNull) {
        this.inventorySupplier = inventorySupplier;
        this.onInventoryNull = onInventoryNull;
        categorySelector = new BattleInventoryCategorySelector(CATEGORY_WIDTH_FACTOR, CATEGORY_HEIGHT_FACTOR);
        sorter = new BattleInventorySorterWidget(CATEGORY_WIDTH_FACTOR, SORTER_HEIGHT_FACTOR);
        itemList = new BattleInventoryItemListWidget(LIST_WIDTH_FACTOR);
        preview = new BattleInventoryPreviewWidget(PREVIEW_WIDTH_FACTOR);
    }

    @Override
    public void render(final GuiContext context) {
        final BattleParticipantInventoryView inventory = inventorySupplier.get();
        if (inventory == null) {
            onInventoryNull.run();
            return;
        }
        context.enterSection(getDebugName());

        final int c = 0x7F000000;
        context.getEmitter().rectangle(-(getScreenWidth() - 1) / 2.0, -(getScreenHeight() - 1) / 2.0, getScreenWidth(), getScreenHeight(), c, c, c, c).renderMaterial(GuiRenderMaterial.POS_COLOUR_TRANSLUCENT).depth(-1f).emit();

        final double screenWidth = getScreenWidth();
        final double screenHeight = getScreenHeight();

        context.pushTranslate(-(screenWidth - 1) / 2.0, -(screenHeight - 1) / 2.0, 0);

        categorySelector.render(context, screenWidth, screenHeight);

        sorter.render(context, screenWidth, screenHeight);

        final List<BattleParticipantInventoryHandle> handles = getHandles();
        categorySelector.filter(handles, inventory::getStack);
        sorter.sort(handles, inventory::getStack);
        if (!prevHandles.equals(handles)) {
            prevHandles = handles;
            itemList.setHandles(handles, inventory::getStack);
        }

        context.pushTranslate(screenWidth * CATEGORY_WIDTH_FACTOR, 0, 0);
        itemList.render(context, screenWidth, screenHeight);
        context.popGuiTransform();

        if (!Objects.equals(lastSelected, itemList.getSelectedHandle())) {
            lastSelected = itemList.getSelectedHandle();
            preview.setSelected(lastSelected, inventory::getStack);
        }

        context.pushTranslate(screenWidth * (CATEGORY_WIDTH_FACTOR + LIST_WIDTH_FACTOR), 0, 0);
        preview.render(context, screenWidth, screenHeight);
        context.popGuiTransform();

        context.popGuiTransform();

        context.exitSection(getDebugName());
    }

    @Override
    public String getDebugName() {
        return "BattleInventoryWidget";
    }

    private List<BattleParticipantInventoryHandle> getHandles() {
        final BattleParticipantInventoryView inventory = inventorySupplier.get();
        if (inventory == null) {
            throw new TBCExException("Null inventory!");
        }
        final List<BattleParticipantInventoryHandle> handles = new ArrayList<>();
        final PairIterator<BattleParticipantItemStack, BattleParticipantInventoryHandle> iterator = inventory.getIterator();
        while (iterator.next()) {
            handles.add(iterator.getRight());
        }
        return handles;
    }
}
