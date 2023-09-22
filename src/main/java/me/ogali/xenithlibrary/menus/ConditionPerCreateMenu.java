package me.ogali.xenithlibrary.menus;

import me.despical.inventoryframework.Gui;
import me.despical.inventoryframework.GuiItem;
import me.despical.inventoryframework.pane.StaticPane;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.utilities.Chat;
import me.ogali.xenithlibrary.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ConditionPerCreateMenu {

    private boolean invertCondition = false;

    public void show(Player player, String id) {
        Gui gui = new Gui(XenithLibrary.getInstance(), 1, Chat.colorize("&8Select An Option"));
        StaticPane staticPane = new StaticPane(0, 0, 9, 1);
        gui.setOnGlobalClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));

        staticPane.addItem(new GuiItem(new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("&a&lIF").build(),
                inventoryClickEvent -> {
                    new ConditionCreateMenu(XenithLibrary.getInstance().getRegistryManager()).show(player, id, invertCondition);
                    invertCondition = true;
                }), 0, 0);

        staticPane.addItem(new GuiItem(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("&c&lIF NOT").build(),
                inventoryClickEvent -> new ConditionCreateMenu(XenithLibrary.getInstance().getRegistryManager())
                        .show(player, id, invertCondition)), 1, 0);

    }

}
