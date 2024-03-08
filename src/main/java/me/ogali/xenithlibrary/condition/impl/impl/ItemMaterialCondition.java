package me.ogali.xenithlibrary.condition.impl.impl;

import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemMaterialCondition extends StringMatchItemCondition {

    public ItemMaterialCondition(String id, int priority, boolean negate) {
        super(id, priority, negate);
    }
    
    public ItemMaterialCondition(String id, int priority, boolean negate, String value) {
        super(id, priority, negate, value);
    }

    @Override
    public boolean evaluate(ItemStack input) {
        try {
            return input.getType().equals(Material.valueOf(getValue())) != isNegate();
        } catch (IllegalArgumentException ignored) {
            Chat.log("&cInvalid material name configured for this condition.");
            return false;
        }
    }

}
