package me.ogali.xenithlibrary.condition.impl.impl;

import me.ogali.xenithlibrary.condition.impl.ItemStackCondition;

public class ItemDurabilityCondition extends ItemStackCondition<Integer> {

    public ItemDurabilityCondition(String id, int priority, boolean negate, Integer value) {
        super(id, priority, negate, value);
    }

    public String getType() {
        return "itemDurability";
    }

}