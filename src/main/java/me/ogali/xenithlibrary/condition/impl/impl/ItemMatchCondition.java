package me.ogali.xenithlibrary.condition.impl.impl;

import lombok.Getter;
import lombok.Setter;
import me.ogali.xenithlibrary.condition.impl.ItemStackCondition;
import me.ogali.xenithlibrary.utilities.Serialization;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class ItemMatchCondition extends ItemStackCondition<ItemStack> {

    private ItemStack value;

    public ItemMatchCondition(String id, int priority, boolean negate) {
        super(id, priority, negate);
    }

    public ItemMatchCondition(String id, int priority, boolean negate, ItemStack value) {
        super(id, priority, negate, value);
        this.value = value;
    }

    @Override
    public boolean evaluate(ItemStack input) {
        return input.equals(value) != isNegate();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + (isNegate() ? getPriority() + " != " + Serialization.serialize(getValue()) : getPriority()
                + " == " + Serialization.serialize(getValue()));
    }

}
