package me.ogali.xenithlibrary.menus.conditions;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.ConditionRegistry;
import me.ogali.xenithlibrary.conditions.evaluator.Evaluator;
import me.ogali.xenithlibrary.utilities.Chat;
import me.ogali.xenithlibrary.utilities.GuiUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class EvaluatorPickerMenu {

    private static final Evaluator[] EVALUATORS = Evaluator.values();

    private static final Material[] ICONS = {
            Material.LIME_STAINED_GLASS_PANE,    // EQUAL
            Material.RED_STAINED_GLASS_PANE,     // NOT_EQUAL
            Material.BLUE_STAINED_GLASS_PANE,    // LESS_THAN
            Material.ORANGE_STAINED_GLASS_PANE,  // GREATER_THAN
            Material.CYAN_STAINED_GLASS_PANE,    // LESS_OR_EQUAL
            Material.YELLOW_STAINED_GLASS_PANE,  // GREATER_OR_EQUAL
    };

    /**
     * onReturn is called both on back press and after selecting an evaluator.
     * Callers decide where to go — creation menu or settings menu.
     */
    public static void show(Player player, AbstractCondition condition, Runnable onReturn) {
        ChestGui gui = new ChestGui(3, "Pick Evaluator");
        gui.setOnTopClick(e -> e.setCancelled(true));

        OutlinePane border = new OutlinePane(9, 3, Pane.Priority.LOWEST);
        border.addItem(new GuiItem(GuiUtil.filler()));
        border.setRepeat(true);
        gui.addPane(Slot.fromXY(0, 0), border);

        StaticPane content = new StaticPane(7, 1);

        for (int i = 0; i < EVALUATORS.length; i++) {
            Evaluator evaluator = EVALUATORS[i];
            boolean selected = evaluator == condition.getEvaluator();
            int finalI = i;

            content.addItem(new GuiItem(
                    GuiUtil.item(
                            ICONS[finalI],
                            (selected ? "&a✔ " : "&f") + evaluator.name(),
                            "&7" + describe(evaluator),
                            "",
                            selected ? "&aCurrently selected" : "&eClick to select"
                    ),
                    e -> {
                        condition.setEvaluator(evaluator);
                        ConditionRegistry.register(condition);
                        Chat.tellFormatted(player, "&aEvaluator set to &e%s", evaluator.name());
                        onReturn.run(); // caller decides where to go
                    }
            ), i, 0);
        }

        gui.addPane(Slot.fromXY(1, 1), content);

        StaticPane bottom = new StaticPane(9, 1);
        bottom.addItem(new GuiItem(
                GuiUtil.back(),
                e -> onReturn.run() // caller decides where to go
        ), 0, 0);
        gui.addPane(Slot.fromXY(0, 2), bottom);

        gui.show(player);
    }

    private static String describe(Evaluator evaluator) {
        return switch (evaluator) {
            case EQUAL -> "value == expected";
            case NOT_EQUAL -> "value != expected";
            case LESS_THAN -> "value < expected";
            case GREATER_THAN -> "value > expected";
            case LESS_OR_EQUAL -> "value <= expected";
            case GREATER_OR_EQUAL -> "value >= expected";
        };
    }
}