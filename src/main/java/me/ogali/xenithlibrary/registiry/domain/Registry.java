package me.ogali.xenithlibrary.registiry.domain;

import java.util.Optional;

public interface Registry<T, V> {

    void register(T object);
    void unRegister(V object);
    Optional<T> get(V object);

}
