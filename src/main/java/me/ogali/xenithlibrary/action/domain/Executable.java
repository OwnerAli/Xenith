package me.ogali.xenithlibrary.action.domain;

public interface Executable<T> {

    default T executeAndReturn(T type) {
        return null;
    }

    default void execute(T type) {

    }

}
