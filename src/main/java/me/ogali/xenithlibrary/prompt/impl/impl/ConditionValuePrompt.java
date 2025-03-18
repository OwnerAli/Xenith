package me.ogali.xenithlibrary.prompt.impl.impl;

import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.events.ConditionCreatedEvent;
import me.ogali.xenithlibrary.prompt.impl.AbstractConditionChatPrompt;
import org.bukkit.entity.Player;

public class ConditionValuePrompt extends AbstractConditionChatPrompt<AbstractCondition<?, ?>> {

    private final Player player;
    private final AbstractCondition<?, ?> abstractCondition;

    public ConditionValuePrompt(Player player, AbstractCondition<?, ?> abstractCondition) {
        super(abstractCondition);
        this.player = player;
        this.abstractCondition = abstractCondition;
    }

    @Override
    public boolean setValue(String value) {
        Object settingValue = abstractCondition.getValue();

        if (settingValue instanceof Integer) {
            ((AbstractCondition<?, Integer>) abstractCondition).setValue(Integer.parseInt(value));
            callConditionCreatedEvent();
            return true;
        } else if (settingValue instanceof Double) {
            ((AbstractCondition<?, Double>) abstractCondition).setValue(Double.parseDouble(value));
            callConditionCreatedEvent();
            return true;
        } else if (settingValue instanceof Float) {
            ((AbstractCondition<?, Float>) abstractCondition).setValue(Float.parseFloat(value));
            callConditionCreatedEvent();
            return true;
        } else if (settingValue instanceof Long) {
            ((AbstractCondition<?, Long>) abstractCondition).setValue(Long.parseLong(value));
            callConditionCreatedEvent();
            return true;
        } else if (settingValue instanceof String) {
            ((AbstractCondition<?, String>) abstractCondition).setValue(value);
            callConditionCreatedEvent();
            return true;
        } else if (settingValue instanceof Boolean) {
            ((AbstractCondition<?, Boolean>) abstractCondition).setValue(Boolean.parseBoolean(value));
            callConditionCreatedEvent();
            return true;
        }

        return false;
    }

    private void callConditionCreatedEvent() {
        new ConditionCreatedEvent(player, abstractCondition).call();
    }

}
