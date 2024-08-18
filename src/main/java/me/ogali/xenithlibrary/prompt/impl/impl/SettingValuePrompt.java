package me.ogali.xenithlibrary.prompt.impl.impl;

import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.prompt.AbstractChatPrompt;
import me.ogali.xenithlibrary.settings.Setting;

public class SettingValuePrompt extends AbstractChatPrompt<AbstractAction<?, ?>> {

    private final Setting setting;

    public SettingValuePrompt(AbstractAction<?, ?> action, Setting setting) {
        super(action);
        this.setting = setting;
    }

    @Override
    public boolean setValue(String value) {
        Object settingValue = setting.getValue();

        if (settingValue instanceof Integer) {
            setting.setValue(Integer.parseInt(value));
            return true;
        } else if (settingValue instanceof Double) {
            setting.setValue(Double.parseDouble(value));
            return true;
        } else if (settingValue instanceof Float) {
            setting.setValue(Float.parseFloat(value));
            return true;
        } else if (settingValue instanceof Long) {
            setting.setValue(Long.parseLong(value));
            return true;
        } else if (settingValue instanceof String) {
            setting.setValue(value);
            return true;
        } else if (settingValue instanceof Boolean) {
            setting.setValue(Boolean.parseBoolean(value));
            return true;
        }

        return false;
    }

}
