package me.ogali.xenithlibrary.action.domain.impl.impl;

import lombok.Getter;
import lombok.Setter;
import me.ogali.xenithlibrary.action.domain.impl.AbstractPlayerAction;

@Setter
@Getter
public abstract class StringValuePlayerAction extends AbstractPlayerAction<String> {

    private String value;

    public StringValuePlayerAction(String id, String value, double chance) {
        super(id, value, chance);
        this.value = value;
    }

    public StringValuePlayerAction(String id) {
        super(id);
    }

}
