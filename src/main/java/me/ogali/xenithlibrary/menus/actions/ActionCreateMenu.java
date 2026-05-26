package me.ogali.xenithlibrary.menus.actions;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.action.domain.ActionRegistry;
import me.ogali.xenithlibrary.action.domain.ActionType;
import me.ogali.xenithlibrary.shared.DomainConfig;
import me.ogali.xenithlibrary.utilities.Chat;
import me.ogali.xenithlibrary.utilities.GuiUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActionCreateMenu {

    // -------------------------------------------------------------------------
    // Step 1 — Pick a type
    // -------------------------------------------------------------------------

    public static void show(Player player, String id) {
        List<ActionType> types = new ArrayList<>(ActionRegistry.allTypes().values());

        ChestGui gui = new ChestGui(4, "Create Action — Pick Type");
        gui.setOnTopClick(e -> e.setCancelled(true));

        OutlinePane border = new OutlinePane(9, 4, Pane.Priority.LOWEST);
        border.addItem(new GuiItem(GuiUtil.filler()));
        border.setRepeat(true);
        gui.addPane(Slot.fromXY(0, 0), border);

        StaticPane content = new StaticPane(7, 2);

        for (int i = 0; i < types.size(); i++) {
            ActionType type = types.get(i);
            content.addItem(new GuiItem(
                    GuiUtil.item(
                            type.icon(),
                            "&f" + type.key(),
                            "&7Click to create a &e" + type.key() + " &7action",
                            "&7ID: &e" + id
                    ),
                    e -> openFieldEditor(player, type, id)
            ), i % 7, i / 7);
        }

        gui.addPane(Slot.fromXY(1, 1), content);

        StaticPane bottom = new StaticPane(9, 1);
        bottom.addItem(new GuiItem(GuiUtil.back(), e -> player.closeInventory()), 0, 0);
        gui.addPane(Slot.fromXY(0, 3), bottom);

        gui.show(player);
    }

    // -------------------------------------------------------------------------
    // Step 2 — Fill in fields
    // -------------------------------------------------------------------------

    private static void openFieldEditor(Player player, ActionType type, String id) {
        AbstractAction action = buildDefault(type, id);

        ChestGui gui = new ChestGui(4, "Create Action — Configure");
        gui.setOnTopClick(e -> e.setCancelled(true));

        OutlinePane border = new OutlinePane(9, 4, Pane.Priority.LOWEST);
        border.addItem(new GuiItem(GuiUtil.filler()));
        border.setRepeat(true);
        gui.addPane(Slot.fromXY(0, 0), border);

        StaticPane fields = new StaticPane(7, 2);

        var serialized = action.serialize();
        int slot = 0;
        for (var entry : serialized.entrySet()) {
            if (entry.getKey().equals("type")) continue;
            String fieldKey = entry.getKey();
            fields.addItem(new GuiItem(
                    GuiUtil.item(
                            Material.PAPER,
                            "&f" + fieldKey,
                            "&7Value: &e" + entry.getValue(),
                            "",
                            "&aClick to edit"
                    ),
                    e -> ActionEditMenu.show(player, action, fieldKey, gui)
            ), slot % 7, slot / 7);
            slot++;
        }

        gui.addPane(Slot.fromXY(1, 1), fields);

        StaticPane bottom = new StaticPane(9, 1);

        // Back — return to type picker
        bottom.addItem(new GuiItem(
                GuiUtil.back(),
                e -> show(player, id)
        ), 0, 0);

        // Save
        bottom.addItem(new GuiItem(
                GuiUtil.item(
                        Material.EMERALD,
                        "&a&lSave Action",
                        "&7ID: &e" + id,
                        "&7Type: &e" + type.key(),
                        "",
                        "&aClick to save."
                ),
                e -> {
                    ActionRegistry.register(action);
                    Chat.tellFormatted(player, "&aAction &e%s &acreated successfully!", id);
                    ActionListMenu.show(player);
                }
        ), 8, 0);

        gui.addPane(Slot.fromXY(0, 3), bottom);
        gui.show(player);
    }

    // -------------------------------------------------------------------------
    // Default instance builder
    // -------------------------------------------------------------------------

    private static AbstractAction buildDefault(ActionType type, String id) {
        AbstractAction action = type.builder().build(new DomainConfig(
                Map.of("type", type.key())
        ));
        action.setId(id);
        action.setTypeKey(type.key());
        return action;
    }
}