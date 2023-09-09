package me.ogali.xenithlibrary.action.domain;

import lombok.Data;

@Data
public abstract class AbstractAction<T, V> implements Executable<T> {

    private final String id;
    private final V value;

}
