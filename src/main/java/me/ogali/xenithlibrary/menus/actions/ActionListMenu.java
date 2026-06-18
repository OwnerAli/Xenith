package me.ogali.xenithlibrary.menus.actions;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import me.ogali.xenithlibrary.actions.domain.AbstractAction;
import me.ogali.xenithlibrary.actions.domain.ActionRegistry;
import me.ogali.xenithlibrary.utilities.Chat;
import me.ogali.xenithlibrary.utilities.GuiUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ActionListMenu {

    public static void show(Player player) {
        ChestGui gui = new ChestGui(6, "Action Manager");
        gui.setOnTopClick(e -> e.setCancelled(true));

        // --- Border ---
        OutlinePane border = new OutlinePane(9, 6, Pane.Priority.LOWEST);
        border.addItem(new GuiItem(GuiUtil.filler()));
        border.setRepeat(true);
        gui.addPane(Slot.fromXY(0, 0), border);

        // --- Paginated content area (rows 1-4, cols 1-7) ---
        PaginatedPane pages = new PaginatedPane(7, 4);
        populatePages(pages, player, gui);
        gui.addPane(Slot.fromXY(1, 1), pages);

        // --- Bottom bar ---
        StaticPane bottom = new StaticPane(9, 1);

        // Previous page
        bottom.addItem(new GuiItem(
                GuiUtil.item(Material.ARROW, "&7← &fPrevious"),
                _ -> {
                    if (pages.getPage() > 0) {
                        pages.setPage(pages.getPage() - 1);
                        gui.update();
                    }
                }
        ), 0, 0);

        // Create new action
        bottom.addItem(new GuiItem(
                GuiUtil.item(Material.LIME_DYE, "&a&lCreate Action", "&7Click to create a new action"),
                _ -> {
                    player.closeInventory();
                    Chat.tell(player, "&aType the &eid &afor your new action in chat:");
                }
        ), 4, 0);

        // Next page
        bottom.addItem(new GuiItem(
                GuiUtil.item(Material.ARROW, "&fNext &7→"),
                _ -> {
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

        for (AbstractAction action : ActionRegistry.allInstances().values()) {
            ItemStack icon = buildActionIcon(action);
            items.add(new GuiItem(icon, _ -> ActionSettingsMenu.show(player, action, gui)));
        }

        // Fill empty slots with filler
        pages.populateWithGuiItems(items);
    }

    private static ItemStack buildActionIcon(AbstractAction action) {
        // Use the icon from the registered ActionType
        Material icon = ActionRegistry.getType(action.getTypeKey()).icon();
        return GuiUtil.item(
                icon,
                "&f" + action.getId(),
                "&7Type: &e" + action.getTypeKey(),
                "&7Chance: &e" + action.getChance() + "%",
                "",
                "&eClick to edit"
        );
    }
}