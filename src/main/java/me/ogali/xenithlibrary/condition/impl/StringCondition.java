package me.ogali.xenithlibrary.condition.impl;

import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import org.bukkit.entity.LivingEntity;

public class StringCondition extends AbstractCondition<String, String> {

    protected StringCondition(String id, int priority, boolean negate) {
        super(id, priority, negate);
    }

    protected StringCondition(String id, int priority, boolean negate, String value) {
        super(id, priority, negate, value);
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public String getDisplayText() {
        return null;
    }

    @Override
    public boolean evaluate(String input, LivingEntity livingEntity) {
        return input.equalsIgnoreCase(getValue()) != isNegate();
    }

}
