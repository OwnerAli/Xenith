package me.ogali.xenithlibrary.prompt.impl;

import me.ogali.xenithlibrary.condition.impl.StringCondition;
import me.ogali.xenithlibrary.prompt.AbstractChatPrompt;

public class StringConditionPrompt extends AbstractChatPrompt<StringCondition> {

    public StringConditionPrompt(StringCondition abstractCondition) {
        super(abstractCondition);
    }

    @Override
    public void setValue(String value) {
        getAbstractCondition().setValue(value);
    }

}
