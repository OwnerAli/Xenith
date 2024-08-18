package me.ogali.xenithlibrary.condition.impl.impl;

import me.ogali.xenithlibrary.condition.impl.ItemStackCondition;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ItemLoreMatchCondition extends ItemStackCondition<List<String>> {

    public ItemLoreMatchCondition(String id, int priority, boolean negate, String value) {
        super(id, priority, negate, new ArrayList<>(Arrays.stream(value.split("ꨀ")).toList()));
    }

    public ItemLoreMatchCondition(String id, int priority, boolean negate, List<String> value) {
        super(id, priority, negate, value);
    }

    public ItemLoreMatchCondition(String id, int priority, boolean negate) {
        super(id, priority, negate);
        List<String> lore = new ArrayList<>();
        setValue(lore);
    }

    @Override
    public String getType() {
        return "itemLoreMatch";
    }

    @Override
    public boolean evaluate(ItemStack input) {
        if (input.getItemMeta() == null) return false;
        if (input.getItemMeta().getLore() == null) return false;
        return Objects.equals(input.getItemMeta().getLore(), getValue()) != isNegate();
    }

    @Override
    public String getDisplayText() {
        return Chat.colorize("&fPlayer's item lore matches");
    }

    @Override
    public String toString() {
        return getClass().getName()
                + " " +
                (isNegate() ? getPriority() + " != " + Chat.listToString(getValue()) :
                        getPriority() + " == " + Chat.listToString(getValue()));
    }

}
