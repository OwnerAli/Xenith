package me.ogali.xenithlibrary.condition.impl.impl;

import me.ogali.xenithlibrary.condition.impl.ItemStackCondition;

public class StringMatchItemCondition extends ItemStackCondition<String> {

    public StringMatchItemCondition(String id, int priority, boolean negate) {
        super(id, priority, negate);
    }

    public StringMatchItemCondition(String id, int priority, boolean negate, String value) {
        super(id, priority, negate, value);
    }

    @Override
    public String getType() {
        return "stringMatch";
    }

}
