package me.ogali.xenithlibrary.registiry.domain.impl;

import lombok.Getter;
import me.ogali.xenithlibrary.registiry.domain.Registry;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class AbstractMapRegistry<T, V> implements Registry<T, V> {

    private final Map<V, T> objectMap = new HashMap<>();

}
