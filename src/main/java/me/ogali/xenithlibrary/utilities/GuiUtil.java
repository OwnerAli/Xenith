package me.ogali.xenithlibrary.utilities;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

@UtilityClass
public final class GuiUtil {

    public static ItemStack item(Material material, String name, String... lore) {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return stack;
        meta.setDisplayName(Chat.colorize(name));
        if (lore.length > 0) {
            meta.setLore(Arrays.stream(lore).map(Chat::colorize).toList());
        }
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack filler() {
        return item(Material.GRAY_STAINED_GLASS_PANE, " ");
    }

    public static ItemStack back() {
        return item(Material.ARROW, "&7← &fBack");
    }
}