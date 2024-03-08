package me.ogali.xenithlibrary.prompt.impl.impl;

import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.prompt.impl.AbstractActionChatPrompt;

public class ActionChancePrompt extends AbstractActionChatPrompt<AbstractAction<?, ?>> {

    public ActionChancePrompt(AbstractAction<?, ?> value) {
        super(value);
    }

    @Override
    public boolean setValue(String value) {
        try {
            double doubleValue = Double.parseDouble(value);
            getType().setChance(doubleValue);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

}
