package me.ogali.xenithlibrary.menus.actions;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import me.ogali.xenithlibrary.actions.domain.AbstractAction;
import me.ogali.xenithlibrary.actions.domain.ActionContext;
import me.ogali.xenithlibrary.actions.domain.ActionRegistry;
import me.ogali.xenithlibrary.utilities.Chat;
import me.ogali.xenithlibrary.utilities.GuiUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class ActionSettingsMenu {

    public static void show(Player player, AbstractAction action, ChestGui previousGui) {
        ChestGui gui = new ChestGui(4, "Editing: " + action.getId());
        gui.setOnTopClick(e -> e.setCancelled(true));

        OutlinePane border = new OutlinePane(9, 4, Pane.Priority.LOWEST);
        border.addItem(new GuiItem(GuiUtil.filler()));
        border.setRepeat(true);
        gui.addPane(Slot.fromXY(0, 0), border);

        StaticPane settings = new StaticPane(7, 2);

        // Collect to list for stable index — fixes slot counter bug
        List<Map.Entry<String, Object>> entries = action.serialize().entrySet().stream()
                .filter(e -> !e.getKey().equals("type"))
                .toList();

        for (int i = 0; i < entries.size(); i++) {
            String key = entries.get(i).getKey();
            Object value = entries.get(i).getValue();

            settings.addItem(new GuiItem(
                    GuiUtil.item(
                            Material.PAPER,
                            "&f" + key,
                            "&7Current: &e" + value,
                            "",
                            "&aClick to edit"
                    ),
                    _ -> ActionEditMenu.show(player, action, key, gui)
            ), i % 7, i / 7);
        }

        gui.addPane(Slot.fromXY(1, 1), settings);

        StaticPane bottom = new StaticPane(9, 1);

        bottom.addItem(new GuiItem(
                GuiUtil.back(),
                _ -> {
                    if (previousGui != null) previousGui.show(player);
                    else ActionListMenu.show(player);
                }
        ), 0, 0);

        bottom.addItem(new GuiItem(
                GuiUtil.item(Material.LIME_DYE, "&a&lTest Action",
                        "&7Execute this action against yourself."),
                _ -> {
                    action.execute(ActionContext.of(player));
                    Chat.tellFormatted(player, "&aExecuted action: &e%s", action.getId());
                }
        ), 4, 0);

        bottom.addItem(new GuiItem(
                GuiUtil.item(Material.RED_DYE, "&c&lDelete Action",
                        "&7This action will be permanently deleted."),
                _ -> {
                    ActionRegistry.delete(action.getId());
                    player.closeInventory();
                    Chat.tellFormatted(player, "&cAction &e%s &cdeleted.", action.getId());
                }
        ), 8, 0);

        gui.addPane(Slot.fromXY(0, 3), bottom);
        gui.show(player);
    }
}