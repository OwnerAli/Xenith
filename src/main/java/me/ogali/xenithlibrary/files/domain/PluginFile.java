package me.ogali.xenithlibrary.files.domain;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Contract for a named yml file owned by a specific domain (conditions, actions, etc).
 * Each implementation knows its own file name and data shape.
 */
public interface PluginFile {
    Map<String, Map<String, Object>> loadAll();

    CompletableFuture<Void> save(String key, Object value);

    CompletableFuture<Void> saveAll(Map<String, Object> data);

    CompletableFuture<Void> delete(String key);
}