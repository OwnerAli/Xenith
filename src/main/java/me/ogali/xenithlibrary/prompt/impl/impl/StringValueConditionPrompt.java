package me.ogali.xenithlibrary.prompt.impl.impl;

import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.prompt.impl.AbstractConditionChatPrompt;

public class StringValueConditionPrompt<T extends AbstractCondition<?, String>> extends AbstractConditionChatPrompt<T> {

    public StringValueConditionPrompt(T abstractCondition) {
        super(abstractCondition);
    }

    @Override
    public boolean setValue(String value) {
        getType().setValue(value);
        return true;
    }

}