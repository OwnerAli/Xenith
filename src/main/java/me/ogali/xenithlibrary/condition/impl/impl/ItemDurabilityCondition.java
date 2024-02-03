package me.ogali.xenithlibrary.condition.impl.impl;

import me.ogali.xenithlibrary.condition.impl.ItemStackCondition;
import me.ogali.xenithlibrary.utilities.Chat;

public class ItemDurabilityCondition extends ItemStackCondition<Integer> {

    public ItemDurabilityCondition(String id, int priority, boolean negate) {
        super(id, priority, negate);
    }

    public ItemDurabilityCondition(String id, int priority, boolean negate, Integer value) {
        super(id, priority, negate, value);
    }

    public String getType() {
        return "itemDurability";
    }

    @Override
    public String getDisplayText() {
        return Chat.colorize("&fPlayer's item durability equals");
    }

}