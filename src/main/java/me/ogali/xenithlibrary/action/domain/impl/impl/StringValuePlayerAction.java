package me.ogali.xenithlibrary.action.domain.impl.impl;

import lombok.Getter;
import me.ogali.xenithlibrary.action.domain.impl.AbstractPlayerAction;

public abstract class StringValuePlayerAction extends AbstractPlayerAction<String> {

    @Getter
    private final String value;

    public StringValuePlayerAction(String id, String value) {
        super(id, value);
        this.value = value;
    }

}
