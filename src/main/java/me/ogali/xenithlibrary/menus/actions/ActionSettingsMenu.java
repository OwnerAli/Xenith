package me.ogali.xenithlibrary.menus.actions;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.action.domain.ActionContext;
import me.ogali.xenithlibrary.action.domain.ActionRegistry;
import me.ogali.xenithlibrary.utilities.Chat;
import me.ogali.xenithlibrary.utilities.GuiUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

public class ActionSettingsMenu {

    public static void show(Player player, AbstractAction action, ChestGui previousGui) {
        ChestGui gui = new ChestGui(4, "Editing: " + action.getId());
        gui.setOnTopClick(e -> e.setCancelled(true));

        // --- Border ---
        OutlinePane border = new OutlinePane(9, 4, Pane.Priority.LOWEST);
        border.addItem(new GuiItem(GuiUtil.filler()));
        border.setRepeat(true);
        gui.addPane(Slot.fromXY(0, 0), border);

        // --- Settings pane ---
        StaticPane settings = new StaticPane(7, 2);

        // Each serialized field becomes a clickable button
        Map<String, Object> serialized = action.serialize();
        int slot = 0;

        for (Map.Entry<String, Object> entry : serialized.entrySet()) {
            String fieldKey = entry.getKey();
            Object fieldValue = entry.getValue();

            // type key is not editable
            if (fieldKey.equals("type")) continue;

            GuiItem fieldItem = new GuiItem(
                    GuiUtil.item(
                            Material.PAPER,
                            "&f" + fieldKey,
                            "&7Current: &e" + fieldValue,
                            "",
                            "&aClick to edit"
                    ),
                    e -> ActionEditMenu.show(player, action, fieldKey, gui)
            );

            settings.addItem(fieldItem, slot % 7, slot / 7);
            slot++;
        }

        gui.addPane(Slot.fromXY(1, 1), settings);

        // --- Bottom bar ---
        StaticPane bottom = new StaticPane(9, 1);

        // Back button
        bottom.addItem(new GuiItem(GuiUtil.back(),
                e -> previousGui.show(player)
        ), 0, 0);

        // Delete button
        bottom.addItem(new GuiItem(
                GuiUtil.item(Material.RED_DYE, "&c&lDelete Action",
                        "&7This action will be permanently deleted."),
                e -> {
                    ActionRegistry.delete(action.getId());
                    player.closeInventory();
                    Chat.tellFormatted(player, "&cAction &e%s &cdeleted.", action.getId());
                }
        ), 8, 0);

        // Test button
        bottom.addItem(new GuiItem(
                GuiUtil.item(Material.LIME_DYE, "&a&lTest Action",
                        "&7Execute this action against yourself."),
                e -> {
                    action.execute(ActionContext.of(player));
                    Chat.tellFormatted(player, "&aExecuted action: &e%s", action.getId());
                }
        ), 4, 0);

        gui.addPane(Slot.fromXY(0, 3), bottom);
        gui.show(player);
    }
}