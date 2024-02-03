package me.ogali.xenithlibrary.utilities;

import me.ogali.xenithlibrary.XenithLibrary;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

public class ItemMetaUtils {

    public static ItemStack removeIfNbtData(ItemStack itemStack) {
        if (itemStack.getItemMeta() == null) return null;
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(XenithLibrary.getInstance(), "if-uuid");
        container.remove(key);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static boolean isNavigationItem(ItemStack itemStack) {
        if (itemStack == null) return false;
        if (itemStack.getItemMeta() == null) return false;
        if (!itemStack.getItemMeta().hasCustomModelData()) return false;
        return itemStack.getItemMeta().getCustomModelData() == 696916969;
    }

}
