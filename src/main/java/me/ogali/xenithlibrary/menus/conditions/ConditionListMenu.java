package me.ogali.xenithlibrary.menus.conditions;

import me.despical.inventoryframework.Gui;
import me.despical.inventoryframework.GuiItem;
import me.despical.inventoryframework.pane.PaginatedPane;
import me.despical.inventoryframework.pane.StaticPane;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.menus.displayItems.DoneItem;
import me.ogali.xenithlibrary.menus.guiItems.ConditionListGuiItem;
import me.ogali.xenithlibrary.menus.panes.TopAndBottomSixPane;
import me.ogali.xenithlibrary.registiry.impl.ConditionRegistry;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ConditionListMenu {

    public static void show(Player player) {
        Gui gui = new Gui(XenithLibrary.getInstance(), 6, Chat.colorize("&8Server Conditions"));
        StaticPane staticPane = new TopAndBottomSixPane();
        PaginatedPane pgPane = new PaginatedPane(0, 1, 9, 4);
        gui.setOnTopClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));

        List<GuiItem> guiItems = new ArrayList<>();
        ConditionRegistry conditionRegistry = XenithLibrary.getInstance().getRegistryManager()
                .getRegistry(ConditionRegistry.class);
        conditionRegistry.getObjectMap().values()
                .forEach(abstractCondition -> guiItems.add(new ConditionListGuiItem(abstractCondition)));

        staticPane.addItem(new GuiItem(new DoneItem().build(),
                click -> player.closeInventory()), 4, 5);

        pgPane.populateWithGuiItems(guiItems);
        gui.addPane(staticPane);
        gui.addPane(pgPane);
        gui.show(player);
    }

}
