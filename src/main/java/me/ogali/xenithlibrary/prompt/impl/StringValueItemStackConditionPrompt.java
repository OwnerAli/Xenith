package me.ogali.xenithlibrary.prompt.impl;

import me.ogali.xenithlibrary.condition.impl.ItemStackCondition;
import me.ogali.xenithlibrary.prompt.AbstractChatPrompt;

public class StringValueItemStackConditionPrompt extends AbstractChatPrompt<ItemStackCondition<String>> {

    public StringValueItemStackConditionPrompt(ItemStackCondition<String> abstractCondition) {
        super(abstractCondition);
    }

    @Override
    public void setValue(String value) {
        getAbstractCondition().setValue(value);
    }

}
