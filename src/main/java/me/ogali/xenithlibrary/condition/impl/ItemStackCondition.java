package me.ogali.xenithlibrary.condition.impl;

import lombok.Getter;
import lombok.Setter;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.inventory.ItemStack;

@Setter
@Getter
public abstract class ItemStackCondition<T> extends AbstractCondition<ItemStack, T> {

    private T value;

    public ItemStackCondition(String id, int priority, boolean negate) {
        super(id, priority, negate);
    }

    public ItemStackCondition(String id, int priority, boolean negate, T value) {
        super(id, priority, negate, value);
        this.value = value;
    }

    @Override
    public String getType() {
        return "itemStack";
    }

    @Override
    public String getDisplayText() {
        return Chat.colorize("&fPlayer's item matches");
    }

    @Override
    public boolean evaluate(ItemStack input) {
        return false;
    }

}
