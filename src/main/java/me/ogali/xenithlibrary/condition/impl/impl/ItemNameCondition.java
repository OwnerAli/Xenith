package me.ogali.xenithlibrary.condition.impl.impl;

public class ItemNameCondition extends StringMatchItemCondition {

    public ItemNameCondition(String id, int priority, boolean negate) {
        super(id, priority, negate);
    }

    public ItemNameCondition(String id, int priority, boolean negate, String value) {
        super(id, priority, negate, value);
    }

}
