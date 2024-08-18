package me.ogali.xenithlibrary.prompt.impl.impl;

import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.prompt.impl.AbstractConditionChatPrompt;

public class IntegerValueConditionPrompt<T extends AbstractCondition<?, Integer>> extends AbstractConditionChatPrompt<T> {

    public IntegerValueConditionPrompt(T abstractCondition) {
        super(abstractCondition);
    }

    @Override
    public boolean setValue(String value) {
        try {
            getType().setValue(Integer.parseInt(value));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
