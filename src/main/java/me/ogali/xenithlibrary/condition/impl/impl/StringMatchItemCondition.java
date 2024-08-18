package me.ogali.xenithlibrary.condition.impl.impl;

import me.ogali.xenithlibrary.condition.impl.ItemStackCondition;

public abstract class StringMatchItemCondition extends ItemStackCondition<String> {

    public StringMatchItemCondition(String id, int priority, boolean negate, String value) {
        super(id, priority, negate, value);
        if (!(value.isBlank() || value.isEmpty())) return;
        setValue("Item Match");
    }

    public StringMatchItemCondition(String id, int priority, boolean negate) {
        super(id, priority, negate);
    }

    @Override
    public String getType() {
        return "stringMatch";
    }

}
