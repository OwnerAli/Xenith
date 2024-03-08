package me.ogali.xenithlibrary.condition.impl.impl;

import org.bukkit.inventory.ItemStack;

public class ItemNameCondition extends StringMatchItemCondition {

    public ItemNameCondition(String id, int priority, boolean negate) {
        super(id, priority, negate);
    }

    public ItemNameCondition(String id, int priority, boolean negate, String value) {
        super(id, priority, negate, value);
    }

    @Override
    public boolean evaluate(ItemStack input) {
        if (input.getItemMeta() == null) return false;
        if (!input.getItemMeta().hasDisplayName()) return false;
        return input.getItemMeta().getDisplayName().equalsIgnoreCase(getValue()) != isNegate();
    }
}
