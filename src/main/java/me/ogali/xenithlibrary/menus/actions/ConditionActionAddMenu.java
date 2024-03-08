package me.ogali.xenithlibrary.menus.actions;

import me.despical.inventoryframework.Gui;
import me.despical.inventoryframework.GuiItem;
import me.despical.inventoryframework.pane.PaginatedPane;
import me.despical.inventoryframework.pane.StaticPane;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.manager.RegistryManager;
import me.ogali.xenithlibrary.menus.conditions.ConditionSettingsMenu;
import me.ogali.xenithlibrary.menus.displayItems.BackButton;
import me.ogali.xenithlibrary.menus.displayItems.NavigationButton;
import me.ogali.xenithlibrary.menus.guiItems.ActionAddToConditionItem;
import me.ogali.xenithlibrary.registiry.impl.ActionRegistry;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ConditionActionAddMenu {

    public void show(Player player, AbstractCondition<?, ?> condition, boolean addToPassList) {
        Gui gui = new Gui(XenithLibrary.getInstance(), 4, Chat.colorize("&8Pick an Action Type"));
        StaticPane pane = new StaticPane(0, 0, 9, 4);
        PaginatedPane pgPane = new PaginatedPane(0, 0, 9, 4);

        RegistryManager registryManager = XenithLibrary.getInstance().getRegistryManager();
        ActionRegistry actionRegistry = registryManager.getRegistry(ActionRegistry.class);

        List<GuiItem> items = new ArrayList<>();

        actionRegistry
                .getObjectMap()
                .values()
                .forEach(action -> items.add(new ActionAddToConditionItem(player, condition, action, addToPassList)));

        pgPane.populateWithGuiItems(items);

        if (pgPane.getPages() > 1) {
            pane.addItem(new GuiItem(new NavigationButton("&a&lNEXT PAGE").build(), click -> {
                click.setCancelled(true);
                if (pgPane.getPage() + 2 > pgPane.getPages()) return;
                pgPane.setPage(pgPane.getPage() + 1);
                gui.update();
            }), 8, 5);
        }

        pane.addItem(new GuiItem(new BackButton().build(), click -> {
            click.setCancelled(true);
            if (pgPane.getPage() > 0) {
                pgPane.setPage(pgPane.getPage() - 1);
                gui.update();
                return;
            }
            new ConditionSettingsMenu().show(player, condition);
        }), 4, 3);

        gui.addPane(pane);
        gui.addPane(pgPane);
        gui.show(player);
    }

}
