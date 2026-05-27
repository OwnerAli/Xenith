package me.ogali.xenithlibrary.menus.actions;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.action.domain.ActionRegistry;
import me.ogali.xenithlibrary.action.domain.ActionType;
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
    // Step 2 — Fill in fields (rebuilds itself on every return from edit)
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

        // Collect to list for stable index — fixes slot counter bug
        List<Map.Entry<String, Object>> entries = action.serialize().entrySet().stream()
                .filter(e -> !e.getKey().equals("type"))
                .toList();

        for (int i = 0; i < entries.size(); i++) {
            String key = entries.get(i).getKey();
            Object value = entries.get(i).getValue();

            fields.addItem(new GuiItem(
                    GuiUtil.item(
                            Material.PAPER,
                            "&f" + key,
                            "&7Value: &e" + value,
                            "",
                            "&aClick to edit"
                    ),
                    // Inline conversation — never opens ActionEditMenu
                    e -> startFieldConversation(player, action, type, key)
            ), i % 7, i / 7);
        }

        gui.addPane(Slot.fromXY(1, 1), fields);

        StaticPane bottom = new StaticPane(9, 1);

        // Back — return to type picker
        bottom.addItem(new GuiItem(
                GuiUtil.back(),
                e -> show(player, action.getId())
        ), 0, 0);

        // Save
        bottom.addItem(new GuiItem(
                GuiUtil.item(
                        Material.EMERALD,
                        "&a&lSave Action",
                        "&7ID: &e" + action.getId(),
                        "&7Type: &e" + type.key(),
                        "",
                        "&aClick to save."
                ),
                e -> {
                    ActionRegistry.register(action);
                    Chat.tellFormatted(player, "&aAction &e%s &acreated successfully!", action.getId());
                    ActionListMenu.show(player);
                }
        ), 8, 0);

        gui.addPane(Slot.fromXY(0, 3), bottom);
        gui.show(player);
    }

    // -------------------------------------------------------------------------
    // Inline field edit — bypasses ActionEditMenu entirely during creation
    // -------------------------------------------------------------------------

    private static void startFieldConversation(Player player, AbstractAction action,
                                               ActionType type, String field) {
        player.closeInventory();

        new ConversationFactory(XenithLibrary.getInstance())
                .withModality(false)
                .withEscapeSequence("cancel")
                .withTimeout(60)
                .withFirstPrompt(new StringPrompt() {

                    @Override
                    public @NotNull String getPromptText(@NotNull ConversationContext context) {
                        return Chat.colorize(
                                "&aEnter a new value for &e" + field + "&a:\n" +
                                        "&7Type &ccancel &7to abort."
                        );
                    }

                    @Override
                    public Prompt acceptInput(@NotNull ConversationContext context, String input) {
                        try {
                            action.applyEdit(field, input);
                            Chat.tellFormatted(player, "&aUpdated &e%s &ato: &f%s", field, input);
                        } catch (Exception ex) {
                            Chat.tellFormatted(player,
                                    "&cInvalid value for &e%s&c: &f%s", field, ex.getMessage());
                        }
                        // Rebuild creation menu fresh — papers now show updated values
                        openFieldEditorWithAction(player, action, type);
                        return Prompt.END_OF_CONVERSATION;
                    }
                })
                .addConversationAbandonedListener(event -> {
                    if (!event.gracefulExit()) {
                        Chat.tell(player, "&cEdit cancelled.");
                        openFieldEditorWithAction(player, action, type);
                    }
                })
                .buildConversation(player)
                .begin();
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