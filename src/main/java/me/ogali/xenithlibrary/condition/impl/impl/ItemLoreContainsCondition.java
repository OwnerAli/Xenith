package me.ogali.xenithlibrary.condition.impl.impl;

import me.ogali.xenithlibrary.condition.impl.ItemStackCondition;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class ItemLoreContainsCondition extends ItemStackCondition<String> {

    public ItemLoreContainsCondition(String id, int priority, boolean negate) {
        super(id, priority, negate);
    }

    public ItemLoreContainsCondition(String id, int priority, boolean negate, String value) {
        super(id, priority, negate, value);
    }

    @Override
    public String getType() {
        return "itemLoreContains";
    }

    @Override
    public boolean evaluate(ItemStack input, LivingEntity livingEntity) {
        if (input.getItemMeta() == null) return false;
        if (input.getItemMeta().getLore() == null) return false;
        for (String string : input.getItemMeta().getLore()) {
            return string.contains(getValue()) != isNegate();
        }
        return false;
    }

    @Override
    public String getDisplayText() {
        return Chat.colorize("&fPlayer's item lore contains");
    }

}
