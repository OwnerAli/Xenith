package me.ogali.xenithlibrary.menus.actions;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import me.ogali.xenithlibrary.actions.domain.AbstractAction;
import me.ogali.xenithlibrary.actions.domain.ActionRegistry;
import me.ogali.xenithlibrary.actions.domain.ActionType;
import me.ogali.xenithlibrary.menus.editors.FieldInput;
import me.ogali.xenithlibrary.menus.editors.FieldInputs;
import me.ogali.xenithlibrary.shared.DomainConfig;
import me.ogali.xenithlibrary.utilities.Chat;
import me.ogali.xenithlibrary.utilities.GuiUtil;
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
                    _ -> openFieldEditor(player, type, id)
            ), i % 7, i / 7);
        }

        gui.addPane(Slot.fromXY(1, 1), content);

        StaticPane bottom = new StaticPane(9, 1);
        bottom.addItem(new GuiItem(GuiUtil.back(), _ -> player.closeInventory()), 0, 0);
        gui.addPane(Slot.fromXY(0, 3), bottom);

        gui.show(player);
    }

    // -------------------------------------------------------------------------
    // Step 2 — Configure fields
    // -------------------------------------------------------------------------

    private static void openFieldEditor(Player player, ActionType type, String id) {
        AbstractAction action = buildDefault(type, id);
        openFieldEditorWithAction(player, action, type);
    }

    static void openFieldEditorWithAction(Player player, AbstractAction action, ActionType type) {
        ChestGui gui = new ChestGui(4, "Create Action — Configure");
        gui.setOnTopClick(e -> e.setCancelled(true));

        OutlinePane border = new OutlinePane(9, 4, Pane.Priority.LOWEST);
        border.addItem(new GuiItem(GuiUtil.filler()));
        border.setRepeat(true);
        gui.addPane(Slot.fromXY(0, 0), border);

        StaticPane fields = new StaticPane(7, 2);

        List<Map.Entry<String, Object>> entries = action.serialize().entrySet().stream()
                .filter(e -> !e.getKey().equals("type"))
                .toList();

        for (int i = 0; i < entries.size(); i++) {
            String key = entries.get(i).getKey();
            Object value = entries.get(i).getValue();

            // Ask the action how this field wants to be edited
            FieldInput input = FieldInputs.find(action, key);

            fields.addItem(new GuiItem(
                    GuiUtil.item(
                            input.icon(),                          // icon reflects input type
                            "&f" + key,
                            "&7Value: &e" + value,
                            "",
                            input.hint()                           // hint reflects input type
                    ),
                    _ -> FieldInputs.resolve(
                            player,
                            action,
                            key,
                            () -> openFieldEditorWithAction(player, action, type) // rebuild fresh
                    )
            ), i % 7, i / 7);
        }

        gui.addPane(Slot.fromXY(1, 1), fields);

        StaticPane bottom = new StaticPane(9, 1);

        bottom.addItem(new GuiItem(
                GuiUtil.back(),
                _ -> show(player, action.getId())
        ), 0, 0);

        bottom.addItem(new GuiItem(
                GuiUtil.item(
                        org.bukkit.Material.EMERALD,
                        "&a&lSave Action",
                        "&7ID: &e" + action.getId(),
                        "&7Type: &e" + type.key(),
                        "",
                        "&aClick to save."
                ),
                _ -> {
                    ActionRegistry.register(action);
                    Chat.tellFormatted(player, "&aAction &e%s &acreated successfully!", action.getId());
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