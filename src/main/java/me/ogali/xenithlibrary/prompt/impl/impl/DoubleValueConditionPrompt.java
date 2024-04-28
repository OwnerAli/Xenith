package me.ogali.xenithlibrary.prompt.impl.impl;

import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.prompt.impl.AbstractConditionChatPrompt;

public class DoubleValueConditionPrompt<T extends AbstractCondition<?, Double>> extends AbstractConditionChatPrompt<T> {

    public DoubleValueConditionPrompt(T abstractCondition) {
        super(abstractCondition);
    }

    @Override
    public boolean setValue(String value) {
        return false;
    }
}
