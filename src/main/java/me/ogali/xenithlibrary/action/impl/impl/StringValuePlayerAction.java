package me.ogali.xenithlibrary.action.impl.impl;

import lombok.Getter;
import lombok.Setter;
import me.ogali.xenithlibrary.action.impl.AbstractPlayerAction;

@Setter
@Getter
public abstract class StringValuePlayerAction extends AbstractPlayerAction<String> {

    private String value;

    public StringValuePlayerAction(String id, String value, double chance, boolean hasExtraSettings) {
        super(id, value, chance, hasExtraSettings);
        this.value = value;
    }

    public StringValuePlayerAction(String id) {
        super(id);
    }

}
