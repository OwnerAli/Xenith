package me.ogali.xenithlibrary.menus.conditions;

import me.despical.inventoryframework.Gui;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.condition.impl.ItemStackCondition;
import me.ogali.xenithlibrary.condition.impl.impl.ItemLoreMatchCondition;
import me.ogali.xenithlibrary.condition.impl.impl.ItemMatchCondition;
import me.ogali.xenithlibrary.condition.impl.impl.ItemMaterialCondition;
import me.ogali.xenithlibrary.condition.impl.impl.ItemNameCondition;
import me.ogali.xenithlibrary.registiry.impl.ConditionRegistry;
import me.ogali.xenithlibrary.utilities.Chat;
import me.ogali.xenithlibrary.utilities.ItemBuilder;
import me.ogali.xenithlibrary.utilities.ItemMetaUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemInputConditionMenu {

    private ItemStack lastItem;

    public void show(Player player, ItemStackCondition<?> itemStackCondition) {
        Gui gui = new Gui(XenithLibrary.getInstance(), 1, Chat.colorize("&cItem Input Condition"));

        for (int i = 0; i < gui.getInventory().getSize(); i++) {
            gui.getInventory().setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("&fLeft-Click Item")
                    .addLoreLines("&fFrom your inventory to", "&fset condition parameters.")
                    .build());
        }

        gui.setOnGlobalClick(click -> {
            click.setCancelled(true);

            if (click.getClickedInventory() == click.getView().getTopInventory()) {
                ItemStack item = click.getInventory().getItem(4);
                if (item != null && item.getType() != Material.GRAY_STAINED_GLASS_PANE && item.getType() != Material.AIR) {
                    ItemStack updatedItem = ItemMetaUtils.removeIfNbtData(item);

                    if (updatedItem == null) return;

                    if (itemStackCondition instanceof ItemMatchCondition itemMatchCondition) {
                        itemMatchCondition.setValue(updatedItem);
                    }

                    ItemMeta itemMeta = updatedItem.getItemMeta();

                    if (itemMeta == null) {
                        Chat.tell(player, "&cItem meta is null!");
                        return;
                    }

                    if (itemStackCondition instanceof ItemMaterialCondition itemMaterialCondition) {
                        itemMaterialCondition.setValue(updatedItem.getType().toString());
                    } else if (itemStackCondition instanceof ItemNameCondition itemNameCondition) {
                        if (!itemMeta.hasDisplayName()) {
                            Chat.tell(player, "&cDisplay name is null!");
                            return;
                        }
                        itemNameCondition.setValue(itemMeta.getDisplayName());
                    } else if (itemStackCondition instanceof ItemLoreMatchCondition itemLoreMatchCondition) {
                        if (itemMeta.getLore() == null) {
                            Chat.tell(player, "&cItem lore is null!");
                            return;
                        }
                        itemLoreMatchCondition.setValue(itemMeta.getLore());
                    }
                    Chat.tell(player, "&aCondition successfully created! &7(" + itemStackCondition.getId() + ")");
                    XenithLibrary.getInstance().getRegistryManager().getRegistry(ConditionRegistry.class)
                            .register(itemStackCondition);
                    player.closeInventory();
                    return;
                }
            }

            if (click.getClickedInventory() != click.getView().getBottomInventory()) return;
            if (click.getCurrentItem() == lastItem) return;
            if (click.getCurrentItem() == null) return;

            lastItem = click.getCurrentItem();
            for (int i = 0; i < gui.getInventory().getSize(); i++) {
                if (i == 4) continue;
                gui.getInventory().setItem(i, new ItemBuilder(Material.LIME_DYE).setName("&a&lCLICK TO CONFIRM!").build());
            }
            click.getInventory().setItem(4, click.getCurrentItem());
        });

        gui.show(player);
    }

}