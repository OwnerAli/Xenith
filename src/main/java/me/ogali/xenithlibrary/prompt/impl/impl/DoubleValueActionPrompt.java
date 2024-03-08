package me.ogali.xenithlibrary.prompt.impl.impl;

import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.prompt.impl.AbstractActionChatPrompt;

public class DoubleValueActionPrompt<T extends AbstractAction<?, Double>> extends AbstractActionChatPrompt<T> {

    public DoubleValueActionPrompt(T abstractAction) {
        super(abstractAction);
    }

    @Override
    public boolean setValue(String value) {
        try {
            double doubleValue = Double.parseDouble(value);
            getType().setValue(doubleValue);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

}