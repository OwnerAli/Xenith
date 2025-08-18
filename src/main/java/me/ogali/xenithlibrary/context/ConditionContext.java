package me.ogali.xenithlibrary.context;

import java.util.HashMap;
import java.util.Map;

public class ConditionContext {
    private final Map<Class<?>, Object> contextData;

    public ConditionContext() {
        this.contextData = new HashMap<>();
    }

    public <T> ConditionContext set(Class<T> key, T value) {
        contextData.put(key, value);
        return this;
    }

    public <T> T get(Class<T> key) {
        return key.cast(contextData.get(key));
    }

}
