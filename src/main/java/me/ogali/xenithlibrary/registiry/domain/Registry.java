package me.ogali.xenithlibrary.registiry.domain;

import java.util.Optional;

/**
 * A generic interface for a registry that manages key-value pairs.
 *
 * @param <K> The type of the key.
 * @param <V> The type of the value.
 */
public interface Registry<K, V> {

    /**
     * Registers a value in the registry.
     *
     * @param object The value to be registered.
     */
    default void register(V object) {}

    /**
     * Registers a value in the registry with a specified key.
     *
     * @param key    The key associated with the value.
     * @param object The value to be registered.
     */
    default void register(K key, V object) {}

    /**
     * Unregisters a value from the registry using its key.
     *
     * @param key The key of the value to be unregistered.
     */
    void unRegister(K key);

    /**
     * Retrieves an optional value from the registry based on its key.
     *
     * @param key The key of the value to be retrieved.
     * @return An Optional containing the value if present, otherwise an empty Optional.
     */
    Optional<V> get(K key);

}