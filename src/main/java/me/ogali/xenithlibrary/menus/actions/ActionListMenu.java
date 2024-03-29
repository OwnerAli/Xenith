package me.ogali.xenithlibrary.menus.actions;

import me.despical.inventoryframework.Gui;
import me.despical.inventoryframework.GuiItem;
import me.despical.inventoryframework.pane.PaginatedPane;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.holder.impl.ActionHolder;
import me.ogali.xenithlibrary.menus.displayItems.BackButton;
import me.ogali.xenithlibrary.menus.displayItems.NavigationButton;
import me.ogali.xenithlibrary.menus.guiItems.ActionEditListItem;
import me.ogali.xenithlibrary.menus.panes.TopAndBottomSixPane;
import me.ogali.xenithlibrary.registiry.impl.ActionRegistry;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ActionListMenu {

    public static void show(Player player) {
        Gui gui = new Gui(XenithLibrary.getInstance(), 6, Chat.colorize("&8Server Actions"));
        TopAndBottomSixPane pane = new TopAndBottomSixPane();
        PaginatedPane pgPane = new PaginatedPane(0, 1, 9, 4);

        List<GuiItem> actionListItems = new ArrayList<>();
        XenithLibrary.getInstance().getRegistryManager()
                .getRegistry(ActionRegistry.class)
                .getObjectMap()
                .values()
                .forEach(action -> actionListItems.add(new ActionEditListItem(action)));

        populateAndShowMenu(player, gui, pane, pgPane, actionListItems);
    }

    public static void show(Player player, ActionHolder actionHolder) {
        setupMenu(player, actionHolder);
    }

    public static void show(Player player, ActionHolder actionHolder, Object... args) {
        setupMenu(player, actionHolder);
    }

    private static void setupMenu(Player player, ActionHolder actionHolder) {
        Gui gui = new Gui(XenithLibrary.getInstance(), 6, Chat.colorize("&8Server Actions"));
        TopAndBottomSixPane pane = new TopAndBottomSixPane();
        PaginatedPane pgPane = new PaginatedPane(0, 1, 9, 4);
        gui.setOnGlobalClick(click -> click.setCancelled(true));

        List<GuiItem> actionListItems = new ArrayList<>();
        ActionRegistry actionRegistry = XenithLibrary.getInstance().getRegistryManager()
                .getRegistry(ActionRegistry.class);

        actionRegistry.getObjectMap()
                .values()
                .forEach(action -> actionListItems.add(new ActionEditListItem(action, actionHolder)));

        populateAndShowMenu(player, gui, pane, pgPane, actionListItems);
    }

    private static void populateAndShowMenu(Player player, Gui gui, TopAndBottomSixPane pane, PaginatedPane pgPane, List<GuiItem> actionListItems) {
        pgPane.populateWithGuiItems(actionListItems);

        if (pgPane.getPages() > 1) {
            pane.addItem(new GuiItem(new NavigationButton("&a&lNEXT PAGE").build(), click -> {
                click.setCancelled(true);
                if (!(pgPane.getPage() + 2 > pgPane.getPages())) {
                    pgPane.setPage(pgPane.getPage() + 1);
                    gui.update();
                }
            }), 8, 5);
        }

        pane.addItem(new GuiItem(new BackButton().build(), click -> {
            click.setCancelled(true);
            if (pgPane.getPage() > 0) {
                pgPane.setPage(pgPane.getPage() - 1);
                gui.update();
            }
        }), 4, 5);

        gui.addPane(pane);
        gui.addPane(pgPane);
        gui.show(player);
    }

}
