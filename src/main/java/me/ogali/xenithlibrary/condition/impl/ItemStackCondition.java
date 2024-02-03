package me.ogali.xenithlibrary.condition.impl;

import lombok.Getter;
import lombok.Setter;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public abstract class ItemStackCondition<T> extends AbstractCondition<ItemStack, T> {

    private final boolean negate;

    @Setter @Getter
    private T value;

    public ItemStackCondition(String id, int priority, boolean negate) {
        super(id, priority, negate);
        this.negate = negate;
    }

    public ItemStackCondition(String id, int priority, boolean negate, T value) {
        super(id, priority, negate, value);
        this.negate = negate;
        this.value = value;
    }

    @Override
    public boolean evaluate(ItemStack input, LivingEntity livingEntity) {
        return input.equals(value) != negate;
    }

    @Override
    public String getType() {
        return "itemStack";
    }

    @Override
    public String getDisplayText() {
        return Chat.colorize("&fPlayer's item matches");
    }

}
