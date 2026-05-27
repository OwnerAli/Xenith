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
import me.ogali.xenithlibrary.utilities.Chat;
import me.ogali.xenithlibrary.utilities.GuiUtil;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ConditionEditMenu {

    public static void show(Player player, AbstractCondition condition,
                            String field, ChestGui previousGui) {
        ChestGui gui = new ChestGui(3, "Edit: " + field);
        gui.setOnTopClick(e -> e.setCancelled(true));

        OutlinePane border = new OutlinePane(9, 3, Pane.Priority.LOWEST);
        border.addItem(new GuiItem(GuiUtil.filler()));
        border.setRepeat(true);
        gui.addPane(Slot.fromXY(0, 0), border);

        StaticPane content = new StaticPane(7, 1);

        content.addItem(new GuiItem(GuiUtil.item(
                Material.ITEM_FRAME,
                "&fField: &e" + field,
                "&7Current value: &e" + condition.serialize().get(field),
                "",
                "&7Type a new value in chat."
        )), 1, 0);

        content.addItem(new GuiItem(
                GuiUtil.item(Material.LIME_DYE, "&a&lEdit in Chat",
                        "&7Close this menu and type the new value."),
                e -> {
                    player.closeInventory();
                    startConversation(player, condition, field, previousGui);
                }
        ), 3, 0);

        content.addItem(new GuiItem(
                GuiUtil.item(Material.RED_DYE, "&c&lCancel",
                        "&7Go back without changes."),
                e -> ConditionSettingsMenu.show(player, condition, previousGui)
        ), 5, 0);

        gui.addPane(Slot.fromXY(1, 1), content);

        StaticPane bottom = new StaticPane(9, 1);
        bottom.addItem(new GuiItem(
                GuiUtil.back(),
                e -> ConditionSettingsMenu.show(player, condition, previousGui)
        ), 0, 0);
        gui.addPane(Slot.fromXY(0, 2), bottom);

        gui.show(player);
    }

    private static void startConversation(Player player, AbstractCondition condition,
                                          String field, ChestGui previousGui) {
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
                            ConditionRegistry.register(condition);
                            Chat.tellFormatted(player, "&aUpdated &e%s &ato: &f%s", field, input);
                        } catch (Exception ex) {
                            Chat.tellFormatted(player,
                                    "&cInvalid value for &e%s&c: &f%s", field, ex.getMessage());
                        }
                        ConditionSettingsMenu.show(player, condition, previousGui);
                        return Prompt.END_OF_CONVERSATION;
                    }
                })
                .addConversationAbandonedListener(event -> {
                    if (!event.gracefulExit()) {
                        Chat.tell(player, "&cEdit cancelled.");
                        ConditionSettingsMenu.show(player, condition, previousGui);
                    }
                })
                .buildConversation(player)
                .begin();
    }
}