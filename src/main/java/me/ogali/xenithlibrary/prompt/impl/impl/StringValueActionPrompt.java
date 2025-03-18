package me.ogali.xenithlibrary.prompt.impl.impl;

import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.prompt.impl.AbstractActionChatPrompt;
import org.bukkit.entity.Player;

public class StringValueActionPrompt<T extends AbstractAction<?, String>> extends AbstractActionChatPrompt<T> {

    public StringValueActionPrompt(T abstractAction, Player player) {
        super(abstractAction, player);
    }

    @Override
    public boolean setValue(String value) {
        getType().setValue(value);
        callActionCreatedEvent();
        return true;
    }

}
