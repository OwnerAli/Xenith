package me.ogali.xenithlibrary.prompt.impl.impl;

import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.prompt.impl.AbstractActionChatPrompt;

public class StringValueActionPrompt<T extends AbstractAction<?, String>> extends AbstractActionChatPrompt<T> {

    public StringValueActionPrompt(T abstractAction) {
        super(abstractAction);
    }

    @Override
    public boolean setValue(String value) {
        getType().setValue(value);
        return true;
    }

}
