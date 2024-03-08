package me.ogali.xenithlibrary.menus.actions;

import me.despical.inventoryframework.Gui;
import me.despical.inventoryframework.GuiItem;
import me.despical.inventoryframework.pane.PaginatedPane;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.manager.RegistryManager;
import me.ogali.xenithlibrary.menus.guiItems.ActionCreateListGuiItem;
import me.ogali.xenithlibrary.registiry.impl.ActionRegistry;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ActionCreateMenu {

    private final RegistryManager registryManager;

    public ActionCreateMenu(RegistryManager registryManager) {
        this.registryManager = registryManager;
    }

    public void show(Player player, String id) {
        Gui gui = new Gui(XenithLibrary.getInstance(), 4, Chat.colorize("&8Pick an Action Type"));
        PaginatedPane pgPane = new PaginatedPane(0, 0, 9, 4);

        List<GuiItem> guiItems = new ArrayList<>();

        ActionRegistry actionRegistry = registryManager.getRegistry(ActionRegistry.class);
        actionRegistry.getActionTypesList().forEach(abstractActionClass -> guiItems.add(new ActionCreateListGuiItem(abstractActionClass, id)));

        pgPane.populateWithGuiItems(guiItems);
        gui.addPane(pgPane);
        gui.show(player);
    }

}