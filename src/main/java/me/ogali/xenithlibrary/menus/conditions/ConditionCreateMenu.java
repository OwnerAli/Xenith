package me.ogali.xenithlibrary.menus.conditions;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.ConditionRegistry;
import me.ogali.xenithlibrary.conditions.domain.ConditionType;
import me.ogali.xenithlibrary.shared.DomainConfig;
import me.ogali.xenithlibrary.utilities.Chat;
import me.ogali.xenithlibrary.utilities.GuiUtil;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConditionCreateMenu {

    // -------------------------------------------------------------------------
    // Step 1 — Pick a type
    // -------------------------------------------------------------------------

    public static void show(Player player, String id) {
        List<ConditionType> types = new ArrayList<>(ConditionRegistry.allTypes().values());

        ChestGui gui = new ChestGui(5, "Create Condition — Pick Type");
        gui.setOnTopClick(e -> e.setCancelled(true));

        OutlinePane border = new OutlinePane(9, 5, Pane.Priority.LOWEST);
        border.addItem(new GuiItem(GuiUtil.filler()));
        border.setRepeat(true);
        gui.addPane(Slot.fromXY(0, 0), border);

        StaticPane content = new StaticPane(7, 3);

        for (int i = 0; i < types.size(); i++) {
            ConditionType type = types.get(i);
            content.addItem(new GuiItem(
                    GuiUtil.item(
                            type.getIcon(),
                            "&f" + type.getKey(),
                            "&7Click to create a &e" + type.getKey() + " &7condition",
                            "&7ID: &e" + id
                    ),
                    e -> openFieldEditor(player, type, id)
            ), i % 7, i / 7);
        }

        gui.addPane(Slot.fromXY(1, 1), content);

        StaticPane bottom = new StaticPane(9, 1);
        bottom.addItem(new GuiItem(GuiUtil.back(), e -> player.closeInventory()), 0, 0);
        gui.addPane(Slot.fromXY(0, 4), bottom);

        gui.show(player);
    }

    // -------------------------------------------------------------------------
    // Step 2 — Configure fields
    // -------------------------------------------------------------------------

    static void openFieldEditor(Player player, ConditionType type, String id) {
        AbstractCondition condition = buildDefault(type, id);
        openFieldEditorWithCondition(player, condition, type);
    }

    static void openFieldEditorWithCondition(Player player, AbstractCondition condition, ConditionType type) {
        ChestGui gui = new ChestGui(4, "Create Condition — Configure");
        gui.setOnTopClick(e -> e.setCancelled(true));

        OutlinePane border = new OutlinePane(9, 4, Pane.Priority.LOWEST);
        border.addItem(new GuiItem(GuiUtil.filler()));
        border.setRepeat(true);
        gui.addPane(Slot.fromXY(0, 0), border);

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
                            "&7Value: &e" + value,
                            "",
                            "&aClick to edit"
                    ),
                    // Pass gui as previousGui AND a callback to reopen this menu fresh after edit
                    e -> openEditAndReturn(player, condition, type, key, gui)
            ), finalI % 7, finalI / 7);
        }

        gui.addPane(Slot.fromXY(1, 1), fields);

        StaticPane bottom = new StaticPane(9, 1);

        // Back — return to type picker
        bottom.addItem(new GuiItem(
                GuiUtil.back(),
                e -> show(player, condition.getId())
        ), 0, 0);

        // Evaluator
        String evaluatorDisplay = condition.getEvaluator() != null
                ? condition.getEvaluator().name() : "EQUAL";

        bottom.addItem(new GuiItem(
                GuiUtil.item(Material.COMPARATOR, "&fEvaluator: &e" + evaluatorDisplay, "&7Click to change."),
                // Returns to creation menu fresh — not settings
                e -> EvaluatorPickerMenu.show(player, condition,
                        () -> openFieldEditorWithCondition(player, condition, type))
        ), 4, 0);

        // Save
        bottom.addItem(new GuiItem(
                GuiUtil.item(
                        Material.EMERALD,
                        "&a&lSave Condition",
                        "&7ID: &e" + condition.getId(),
                        "&7Type: &e" + type.getKey(),
                        "",
                        "&aClick to save."
                ),
                e -> {
                    ConditionRegistry.register(condition);
                    Chat.tellFormatted(player, "&aCondition &e%s &acreated!", condition.getId());
                    ConditionListMenu.show(player);
                }
        ), 8, 0);

        gui.addPane(Slot.fromXY(0, 3), bottom);
        gui.show(player);
    }

    /**
     * Opens the edit menu for a field, then returns to a freshly rebuilt
     * creation menu — not the stale captured gui reference.
     */
    private static void openEditAndReturn(Player player, AbstractCondition condition,
                                          ConditionType type, String field, ChestGui returnGui) {
        // Build a throwaway gui just to satisfy ConditionEditMenu's signature —
        // we never show it; instead we rebuild creation menu fresh on return
        ChestGui dummyReturn = returnGui;

        new ConversationFactory(XenithLibrary.getInstance())
                .withModality(false)
                .withEscapeSequence("cancel")
                .withTimeout(60)
                .withFirstPrompt(new StringPrompt() {

                    @Override
                    public String getPromptText(ConversationContext context) {
                        return Chat.colorize(
                                "&aEnter a new value for &e" + field + "&a:\n" +
                                        "&7Type &ccancel &7to abort."
                        );
                    }

                    @Override
                    public Prompt acceptInput(@NotNull ConversationContext context, String input) {
                        try {
                            condition.applyEdit(field, input);
                            Chat.tellFormatted(player, "&aUpdated &e%s &ato: &f%s", field, input);
                        } catch (Exception ex) {
                            Chat.tellFormatted(player,
                                    "&cInvalid value for &e%s&c: &f%s", field, ex.getMessage());
                        }
                        // Always rebuild creation menu fresh so papers show updated values
                        openFieldEditorWithCondition(player, condition, type);
                        return Prompt.END_OF_CONVERSATION;
                    }
                })
                .addConversationAbandonedListener(event -> {
                    if (!event.gracefulExit()) {
                        Chat.tell(player, "&cEdit cancelled.");
                        openFieldEditorWithCondition(player, condition, type);
                    }
                })
                .buildConversation(player)
                .begin();

        player.closeInventory();
    }

    private static void populateFields(StaticPane fields, AbstractCondition condition,
                                       ChestGui gui, Player player) {
        fields.clear();

        // Collect entries to a list so we have a stable index — fixes slot counter bug
        List<Map.Entry<String, Object>> entries = condition.serialize().entrySet().stream()
                .filter(e -> !e.getKey().equals("type")
                        && !e.getKey().equals("id")
                        && !e.getKey().equals("evaluator"))
                .toList();

        for (int i = 0; i < entries.size(); i++) {
            String key = entries.get(i).getKey();
            Object value = entries.get(i).getValue();
            int finalI = i; // effectively final for lambda

            fields.addItem(new GuiItem(
                    GuiUtil.item(
                            Material.PAPER,
                            "&f" + key,
                            "&7Value: &e" + value,
                            "",
                            "&aClick to edit"
                    ),
                    e -> ConditionEditMenu.show(player, condition, key, gui)
            ), finalI % 7, finalI / 7);
        }
    }

    private static AbstractCondition buildDefault(ConditionType type, String id) {
        AbstractCondition condition = type.getBuilder().build(
                new DomainConfig(Map.of("type", type.getKey()))
        );
        condition.setId(id);
        condition.setTypeKey(type.getKey());
        return condition;
    }
}