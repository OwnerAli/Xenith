package me.ogali.xenithlibrary.shared;

import lombok.ToString;

import java.util.*;

@ToString
public class DomainConfig {
    private final Map<String, Object> config;

    public DomainConfig() {
        this.config = new HashMap<>();
    }

    public DomainConfig(Map<String, Object> data) {
        this.config = new HashMap<>(data);
    }

    public Optional<Object> get(String key) {
        return Optional.ofNullable(config.get(key));
    }

    public Map<String, Object> raw() {
        return Collections.unmodifiableMap(config);
    }

    public String getString(String key) {
        Object value = config.get(key);
        return value instanceof String s ? s : null;
    }

    public String getString(String key, String fallback) {
        String value = getString(key);
        return value != null ? value : fallback;
    }

    /**
     * Returns the string value uppercased — used for parsing enums from config.
     * Returns null if absent so callers can distinguish missing from empty.
     */
    public String getUppercaseString(String key) {
        String value = getString(key);
        return value != null ? value.toUpperCase() : null;
    }

    public Integer getInt(String key) {
        Object value = config.get(key);
        if (value instanceof Integer i) return i;
        if (value instanceof Number n) return n.intValue(); // handles YAML long literals
        return null;
    }

    public int getInt(String key, int fallback) {
        Integer value = getInt(key);
        return value != null ? value : fallback;
    }

    public Long getLong(String key) {
        Object value = config.get(key);
        if (value instanceof Long l) return l;
        if (value instanceof Number n) return n.longValue(); // YAML parses small ints as Integer
        return null;
    }

    public long getLong(String key, long fallback) {
        Long value = getLong(key);
        return value != null ? value : fallback;
    }

    public Double getDouble(String key) {
        Object value = config.get(key);
        if (value instanceof Double d) return d;
        if (value instanceof Number n) return n.doubleValue();
        return null;
    }

    public double getDouble(String key, double fallback) {
        Double value = getDouble(key);
        return value != null ? value : fallback;
    }

    public Boolean getBoolean(String key) {
        Object value = config.get(key);
        return value instanceof Boolean b ? b : null;
    }

    public boolean getBoolean(String key, boolean fallback) {
        Boolean value = getBoolean(key);
        return value != null ? value : fallback;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String key, Class<T> type) {
        Object value = config.get(key);
        if (!(value instanceof List<?> list)) return Collections.emptyList();
        if (list.isEmpty()) return Collections.emptyList();
        if (!type.isInstance(list.get(0))) return Collections.emptyList();
        return (List<T>) list;
    }

    public List<String> getStringList(String key) {
        return getList(key, String.class);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getMapList(String key) {
        return getList(key, (Class<Map<String, Object>>) (Class<?>) Map.class);
    }
}