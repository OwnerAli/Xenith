package me.ogali.xenithlibrary.prompt.impl.impl;

import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.prompt.AbstractChatPrompt;
import me.ogali.xenithlibrary.settings.Setting;

public class settingValuePrompt extends AbstractChatPrompt<AbstractAction<?, ?>> {

    private final Setting setting;

    public settingValuePrompt(AbstractAction<?, ?> action, Setting setting) {
        super(action);
        this.setting = setting;
    }

    @Override
    public boolean setValue(String value) {
        Object settingValue = setting.getValue();

        if (settingValue instanceof Integer) {
            setting.setValue(Integer.parseInt(value));
            return true;
        } else if (settingValue instanceof Double doubleValue) {
            setting.setValue(doubleValue);
            return true;
        } else if (settingValue instanceof Float floatValue) {
            setting.setValue(floatValue);
            return true;
        } else if (settingValue instanceof Long longValue) {
            setting.setValue(longValue);
            return true;
        } else if (settingValue instanceof String) {
            setting.setValue(value);
            return true;
        } else if (settingValue instanceof Boolean booleanValue) {
            setting.setValue(booleanValue);
            return true;
        }

        return false;
    }

}
