package me.ogali.xenithlibrary.menus.conditions;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
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
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ConditionListMenu {

    public static void show(Player player) {
        ChestGui gui = new ChestGui(6, "Condition Manager");
        gui.setOnTopClick(e -> e.setCancelled(true));

        OutlinePane border = new OutlinePane(9, 6, Pane.Priority.LOWEST);
        border.addItem(new GuiItem(GuiUtil.filler()));
        border.setRepeat(true);
        gui.addPane(Slot.fromXY(0, 0), border);

        PaginatedPane pages = new PaginatedPane(7, 4);
        populatePages(pages, player, gui);
        gui.addPane(Slot.fromXY(1, 1), pages);

        StaticPane bottom = new StaticPane(9, 1);

        bottom.addItem(new GuiItem(
                GuiUtil.item(Material.ARROW, "&7← &fPrevious"),
                e -> {
                    if (pages.getPage() > 0) {
                        pages.setPage(pages.getPage() - 1);
                        gui.update();
                    }
                }
        ), 0, 0);

        // Bug 3 fix — start conversation to ask for id
        bottom.addItem(new GuiItem(
                GuiUtil.item(Material.LIME_DYE, "&a&lCreate Condition",
                        "&7Click to create a new condition."),
                e -> {
                    player.closeInventory();
                    startCreateConversation(player);
                }
        ), 4, 0);

        bottom.addItem(new GuiItem(
                GuiUtil.item(Material.ARROW, "&fNext &7→"),
                e -> {
                    if (pages.getPage() < pages.getPages() - 1) {
                        pages.setPage(pages.getPage() + 1);
                        gui.update();
                    }
                }
        ), 8, 0);

        gui.addPane(Slot.fromXY(0, 5), bottom);
        gui.show(player);
    }

    private static void populatePages(PaginatedPane pages, Player player, ChestGui gui) {
        List<GuiItem> items = new ArrayList<>();
        for (AbstractCondition condition : ConditionRegistry.allInstances().values()) {
            items.add(new GuiItem(buildConditionIcon(condition),
                    e -> ConditionSettingsMenu.show(player, condition, gui)));
        }
        pages.populateWithGuiItems(items);
    }

    private static ItemStack buildConditionIcon(AbstractCondition condition) {
        String evaluator = condition.getEvaluator() != null
                ? condition.getEvaluator().name()
                : "NONE";
        return GuiUtil.item(
                ConditionRegistry.getType(condition.getTypeKey()).getIcon(),
                "&f" + condition.getId(),
                "&7Type: &e" + condition.getTypeKey(),
                "&7Evaluator: &e" + evaluator,
                "",
                "&eClick to edit"
        );
    }

    private static void startCreateConversation(Player player) {
        new ConversationFactory(XenithLibrary.getInstance())
                .withModality(false)
                .withEscapeSequence("cancel")
                .withTimeout(60)
                .withFirstPrompt(new StringPrompt() {

                    @Override
                    public String getPromptText(ConversationContext context) {
                        return Chat.colorize(
                                "&aEnter a unique &eid &afor your new condition:\n" +
                                        "&7Type &ccancel &7to abort."
                        );
                    }

                    @Override
                    public Prompt acceptInput(@NotNull ConversationContext context, String input) {
                        if (ConditionRegistry.isRegistered(input)) {
                            Chat.tellFormatted(player,
                                    "&cA condition with id &e%s &calready exists. Try another.", input);
                            return this; // ask again
                        }
                        ConditionCreateMenu.show(player, input);
                        return Prompt.END_OF_CONVERSATION;
                    }
                })
                .addConversationAbandonedListener(event -> {
                    if (!event.gracefulExit()) {
                        Chat.tell(player, "&cCondition creation cancelled.");
                        show(player);
                    }
                })
                .buildConversation(player)
                .begin();
    }
}