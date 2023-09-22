package me.ogali.xenithlibrary.manager;

import me.ogali.xenithlibrary.registiry.domain.Registry;

import java.util.HashMap;
import java.util.Map;

public class RegistryManager {

    private final Map<Class<? extends Registry<?, ?>>, Registry<?, ?>> registryMap = new HashMap<>();

    public void initializeRegistry(Class<? extends Registry<?, ?>> registryClass) {
        try {
            Registry<?, ?> registryInstance = registryClass.getDeclaredConstructor().newInstance();
            registryMap.put(registryClass, registryInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T extends Registry<?, ?>> T getRegistry(Class<T> registryClass) {
        return (T) registryMap.get(registryClass);
    }

}
