package me.ogali.xenithlibrary.condition.impl;

import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.condition.domain.Condition;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ItemStackCondition<T> extends AbstractCondition<ItemStack, T> {

    private final boolean negate;
    private final T value;

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

}
