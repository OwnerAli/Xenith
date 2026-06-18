package me.ogali.xenithlibrary.settings;

import lombok.Data;

@Data
public class Setting {

    private Object value;
    private String name;

    public Setting(String name, Object value) {
        this.name = name;
        this.value = value;
    }

}
