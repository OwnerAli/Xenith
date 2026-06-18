package me.ogali.xenithlibrary.menus.conditions;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.ConditionRegistry;
import me.ogali.xenithlibrary.utilities.Chat;
import me.ogali.xenithlibrary.utilities.GuiUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class ConditionSettingsMenu {

    public static void show(Player player, AbstractCondition condition, ChestGui previousGui) {
        ChestGui gui = new ChestGui(4, "Editing: " + condition.getId());
        gui.setOnTopClick(e -> e.setCancelled(true));

        OutlinePane border = new OutlinePane(9, 4, Pane.Priority.LOWEST);
        border.addItem(new GuiItem(GuiUtil.filler()));
        border.setRepeat(true);
        gui.addPane(Slot.fromXY(0, 0), border);

        // --- Fields ---
        StaticPane fields = new StaticPane(7, 2);

        List<Map.Entry<String, Object>> entries = condition.serialize().entrySet().stream()
                .filter(e -> !e.getKey().equals("type")
                        && !e.getKey().equals("id")
                        && !e.getKey().equals("evaluator"))
                .toList();

        for (int i = 0; i < entries.size(); i++) {
            String key = entries.get(i).getKey();
            Object value = entries.get(i).getValue();
            int finalI = i;

            fields.addItem(new GuiItem(
                    GuiUtil.item(
                            Material.PAPER,
                            "&f" + key,
                            "&7Current: &e" + value,
                            "",
                            "&aClick to edit"
                    ),
                    e -> ConditionEditMenu.show(player, condition, key, gui)
            ), finalI % 7, finalI / 7);
        }

        gui.addPane(Slot.fromXY(1, 1), fields);

        // --- Bottom bar ---
        StaticPane bottom = new StaticPane(9, 1);

        // Back
        bottom.addItem(new GuiItem(
                GuiUtil.back(),
                e -> {
                    if (previousGui != null) previousGui.show(player);
                    else ConditionListMenu.show(player);
                }
        ), 0, 0);

        // Evaluator — reads fresh from condition every time menu opens
        String evaluatorDisplay = condition.getEvaluator() != null
                ? condition.getEvaluator().name()
                : "EQUAL";

        bottom.addItem(new GuiItem(
                GuiUtil.item(Material.COMPARATOR, "&fEvaluator: &e" + evaluatorDisplay, "&7Click to change."),
                // Returns to settings menu fresh
                e -> EvaluatorPickerMenu.show(player, condition,
                        () -> ConditionSettingsMenu.show(player, condition, previousGui))
        ), 4, 0);

        // Delete
        bottom.addItem(new GuiItem(
                GuiUtil.item(
                        Material.RED_DYE,
                        "&c&lDelete Condition",
                        "&7This condition will be permanently deleted."
                ),
                e -> {
                    ConditionRegistry.delete(condition.getId());
                    player.closeInventory();
                    Chat.tellFormatted(player, "&cCondition &e%s &cdeleted.", condition.getId());
                }
        ), 8, 0);

        gui.addPane(Slot.fromXY(0, 3), bottom);
        gui.show(player);
    }
}