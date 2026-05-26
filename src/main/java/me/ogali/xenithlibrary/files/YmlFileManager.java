package me.ogali.xenithlibrary.files;

import me.ogali.xenithlibrary.XenithLibrary;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class YmlFileManager {
    private final File directory;
    private final Yaml yaml;

    public YmlFileManager(File directory) {
        this.directory = directory;
        this.yaml = buildYaml();
        directory.mkdirs();
    }

    /**
     * Loads all top-level entries from a yml file as a map.
     * Returns empty map if the file doesn't exist or is empty.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> loadAll(String fileName) {
        File file = resolve(fileName);
        if (!file.exists()) return Collections.emptyMap();

        try (InputStream in = new FileInputStream(file)) {
            Object data = yaml.load(in);
            return data instanceof Map<?, ?> map ? (Map<String, Object>) map : Collections.emptyMap();
        } catch (IOException e) {
            log("Failed to load file: " + fileName, e);
            return Collections.emptyMap();
        }
    }

    public Optional<Object> get(String fileName, String key) {
        return Optional.ofNullable(loadAll(fileName).get(key));
    }

    /**
     * Saves the entire map to a yml file asynchronously, replacing its contents.
     */
    public CompletableFuture<Void> saveAll(String fileName, Map<String, Object> data) {
        return CompletableFuture.runAsync(() -> {
            File file = resolve(fileName);
            ensureExists(file);

            try (FileWriter writer = new FileWriter(file, false)) {
                yaml.dump(data, writer);
            } catch (IOException e) {
                log("Failed to save file: " + fileName, e);
            }
        });
    }

    /**
     * Merges a single key into an existing yml file asynchronously.
     * Existing keys are preserved; only the given key is updated.
     */
    public CompletableFuture<Void> save(String fileName, String key, Object value) {
        return CompletableFuture.runAsync(() -> {
            Map<String, Object> existing = new LinkedHashMap<>(loadAll(fileName));
            existing.put(key, value);

            File file = resolve(fileName);
            ensureExists(file);

            try (FileWriter writer = new FileWriter(file, false)) {
                yaml.dump(existing, writer);
            } catch (IOException e) {
                log("Failed to save key '" + key + "' to file: " + fileName, e);
            }
        });
    }

    /**
     * Removes a key from a yml file asynchronously.
     */
    public CompletableFuture<Void> delete(String fileName, String key) {
        return CompletableFuture.runAsync(() -> {
            Map<String, Object> existing = new LinkedHashMap<>(loadAll(fileName));
            existing.remove(key);

            File file = resolve(fileName);
            try (FileWriter writer = new FileWriter(file, false)) {
                yaml.dump(existing, writer);
            } catch (IOException e) {
                log("Failed to delete key '" + key + "' from file: " + fileName, e);
            }
        });
    }

    public void initFile(String fileName) {
        directory.mkdirs();
        File file = resolve(fileName);
        XenithLibrary.getInstance().getLogger().info("Initializing file: " + fileName);
        ensureExists(file);
    }

    private File resolve(String fileName) {
        String name = fileName.endsWith(".yml") ? fileName : fileName + ".yml";
        return new File(directory, name);
    }

    private void ensureExists(File file) {
        try {
            file.getParentFile().mkdirs(); // belt and braces
            if (!file.exists()) {
                boolean created = file.createNewFile();
                if (!created) {
                    log("File already exists or could not be created: " + file.getName(), null);
                }
            }
        } catch (IOException e) {
            log("Failed to create file: " + file.getName(), e);
        }
    }

    private void log(String message, Exception e) {
        XenithLibrary.getInstance().getLogger().log(Level.SEVERE, message, e);
    }

    private Yaml buildYaml() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        options.setIndent(2);
        return new Yaml(options);
    }
}