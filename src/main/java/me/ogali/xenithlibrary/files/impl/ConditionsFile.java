package me.ogali.xenithlibrary.files.impl;

import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.files.YmlFileManager;
import me.ogali.xenithlibrary.files.domain.PluginFile;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ConditionsFile implements PluginFile {
    private static final String FILE_NAME = "conditions";
    private final YmlFileManager fileManager;

    public ConditionsFile() {
        this.fileManager = new YmlFileManager(XenithLibrary.getInstance().getDataFolder());
        fileManager.initFile(FILE_NAME);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Map<String, Object>> loadAll() {
        Map<String, Object> raw = fileManager.loadAll(FILE_NAME);
        if (raw.isEmpty()) return Collections.emptyMap();

        // Every top-level value is a condition config block (Map<String, Object>)
        return (Map<String, Map<String, Object>>) (Map<?, ?>) raw;
    }

    @Override
    public CompletableFuture<Void> save(String key, Object value) {
        return fileManager.save(FILE_NAME, key, value);
    }

    @Override
    public CompletableFuture<Void> saveAll(Map<String, Object> data) {
        return fileManager.saveAll(FILE_NAME, data);
    }

    @Override
    public CompletableFuture<Void> delete(String key) {
        return fileManager.delete(FILE_NAME, key);
    }
}