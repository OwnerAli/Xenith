package me.ogali.xenithlibrary.menus.conditions;

import me.despical.inventoryframework.Gui;
import me.despical.inventoryframework.GuiItem;
import me.despical.inventoryframework.pane.PaginatedPane;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.menus.guiItems.ConditionListGuiItem;
import me.ogali.xenithlibrary.registiry.impl.ConditionRegistry;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ConditionListMenu {

    public static void show(Player player) {
        Gui gui = new Gui(XenithLibrary.getInstance(), 4, Chat.colorize("&8Server Conditions"));
        PaginatedPane pgPane = new PaginatedPane(0, 0, 9, 4);
        gui.setOnGlobalClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));

        List<GuiItem> guiItems = new ArrayList<>();
        ConditionRegistry conditionRegistry = XenithLibrary.getInstance().getRegistryManager()
                .getRegistry(ConditionRegistry.class);
        conditionRegistry.getObjectMap().values()
                .forEach(abstractCondition -> guiItems.add(new ConditionListGuiItem(abstractCondition)));

        pgPane.populateWithGuiItems(guiItems);
        gui.addPane(pgPane);
        gui.show(player);
    }

}
