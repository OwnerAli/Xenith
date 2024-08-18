package me.ogali.xenithlibrary.prompt.impl.impl;

import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.prompt.impl.AbstractConditionChatPrompt;

public class ConditionValuePrompt extends AbstractConditionChatPrompt<AbstractCondition<?, ?>> {

    private final AbstractCondition<?, ?> abstractCondition;

    public ConditionValuePrompt(AbstractCondition<?, ?> abstractCondition) {
        super(abstractCondition);
        this.abstractCondition = abstractCondition;
    }

    @Override
    public boolean setValue(String value) {
        Object settingValue = abstractCondition.getValue();

        if (settingValue instanceof Integer) {
            ((AbstractCondition<?, Integer>) abstractCondition).setValue(Integer.parseInt(value));
            return true;
        } else if (settingValue instanceof Double) {
            ((AbstractCondition<?, Double>) abstractCondition).setValue(Double.parseDouble(value));
            return true;
        } else if (settingValue instanceof Float) {
            ((AbstractCondition<?, Float>) abstractCondition).setValue(Float.parseFloat(value));
            return true;
        } else if (settingValue instanceof Long) {
            ((AbstractCondition<?, Long>) abstractCondition).setValue(Long.parseLong(value));
            return true;
        } else if (settingValue instanceof String) {
            ((AbstractCondition<?, String>) abstractCondition).setValue(value);
            return true;
        } else if (settingValue instanceof Boolean) {
            ((AbstractCondition<?, Boolean>) abstractCondition).setValue(Boolean.parseBoolean(value));
            return true;
        }

        return false;
    }

}
