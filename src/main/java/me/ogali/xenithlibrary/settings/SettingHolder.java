package me.ogali.xenithlibrary.settings;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SettingHolder {

    private final List<Setting> settingsList;

    public SettingHolder() {
        this.settingsList = new ArrayList<>();
    }

    public void addSetting(Setting setting) {
        settingsList.add(setting);
    }

    public Setting getSetting(String name) {
        return settingsList.stream().filter(setting -> setting.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        settingsList.forEach(setting -> stringBuilder.append(setting.getValue()).append(","));
        return stringBuilder.toString();
    }
}
