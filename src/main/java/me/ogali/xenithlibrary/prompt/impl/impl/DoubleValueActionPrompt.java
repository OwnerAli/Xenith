package me.ogali.xenithlibrary.prompt.impl.impl;

import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.prompt.impl.AbstractActionChatPrompt;
import org.bukkit.entity.Player;

public class DoubleValueActionPrompt<T extends AbstractAction<?, Double>> extends AbstractActionChatPrompt<T> {

    public DoubleValueActionPrompt(T abstractAction, Player player) {
        super(abstractAction, player);
    }

    @Override
    public boolean setValue(String value) {
        try {
            double doubleValue = Double.parseDouble(value);
            getType().setValue(doubleValue);
            callActionCreatedEvent();
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

}