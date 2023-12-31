package me.ogali.xenithlibrary.condition.impl.impl;

import me.ogali.xenithlibrary.condition.impl.ItemStackCondition;

import java.util.List;

public class ItemLoreMatchCondition extends ItemStackCondition<List<String>> {

    public ItemLoreMatchCondition(String id, int priority, boolean negate, List<String> value) {
        super(id, priority, negate, value);
    }

    @Override
    public String getType() {
        return "itemLoreMatch";
    }

//    @Override
//    public boolean evaluateCondition(ItemStack input, Player player) {
//        if (input.getItemMeta() == null) return false;
//        if (input.getItemMeta().getLore() == null) return false;
//        if (Objects.equals(input.getItemMeta().getLore(), getValue()) == isNegate()) return false;
//        executeActions(player);
//        return true;
//    }

}
