package me.ogali.xenithlibrary.menus.conditions;

import me.despical.inventoryframework.Gui;
import me.despical.inventoryframework.GuiItem;
import me.despical.inventoryframework.pane.StaticPane;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.menus.displayItems.NextItem;
import me.ogali.xenithlibrary.utilities.Chat;
import me.ogali.xenithlibrary.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ConditionPreCreateSettingsMenu {

    private int priority = 0;

    public void show(Player player, String id, boolean negate) {
        Gui gui = new Gui(XenithLibrary.getInstance(), 1, Chat.colorize("&8Select An Option"));
        StaticPane staticPane = new StaticPane(0, 0, 9, 1);
        gui.setOnGlobalClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));

        staticPane.addItem(new GuiItem(new ItemBuilder(Material.FIREWORK_STAR).setName("&a&lPriority:&f " + priority)
                .addLoreLines("&fClick to change condition priority.", "&f&nLower Priority = Evaluated First!", "",
                        "&aLeft-Click +1", "&aShift-Left-Click +10", "&cRight-Click -1",
                        "&cShift-Right-Click -10").build(),
                inventoryClickEvent -> {
                    if (inventoryClickEvent.isShiftClick()) {
                        if (inventoryClickEvent.isLeftClick()) {
                            priority += 10;
                        } else if (inventoryClickEvent.isRightClick()) {
                            priority -= 10;
                        }
                        show(player, id, negate);
                        return;
                    }
                    if (inventoryClickEvent.isLeftClick()) {
                        priority++;
                    } else if (inventoryClickEvent.isRightClick()) {
                        priority--;
                    }
                    show(player, id, negate);
                }), 4, 0);

        staticPane.addItem(new GuiItem(new NextItem().build(), inventoryClickEvent -> new ConditionCreateMenu(XenithLibrary.getInstance()
                .getRegistryManager())
                .show(player, id, priority, negate)), 6, 0);

        gui.addPane(staticPane);
        gui.show(player);
    }

}
