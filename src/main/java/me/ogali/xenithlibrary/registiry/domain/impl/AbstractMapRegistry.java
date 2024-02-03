package me.ogali.xenithlibrary.registiry.domain.impl;

import lombok.Getter;
import me.ogali.xenithlibrary.registiry.domain.Registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public abstract class AbstractMapRegistry<K, V> implements Registry<K, V> {

    private final Map<K, V> objectMap = new HashMap<>();

    @Override
    public void register(K key, V object) {
        objectMap.put(key, object);
    }

    @Override
    public void unRegister(K key) {
        objectMap.remove(key);
    }

    @Override
    public Optional<V> get(K key) {
        return Optional.ofNullable(objectMap.get(key));
    }

}