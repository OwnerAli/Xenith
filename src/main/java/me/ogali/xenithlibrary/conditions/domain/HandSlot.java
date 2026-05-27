package me.ogali.xenithlibrary.conditions.domain;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public enum HandSlot {
    MAIN_HAND,
    OFF_HAND;

    public ItemStack getItem(Player player) {
        return switch (this) {
            case MAIN_HAND -> player.getInventory().getItemInMainHand();
            case OFF_HAND -> player.getInventory().getItemInOffHand();
        };
    }
}