package me.ogali.xenithlibrary.conditions.domain;

import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.conditions.evaluator.Evaluator;
import me.ogali.xenithlibrary.conditions.impl.BlockAgeCondition;
import me.ogali.xenithlibrary.files.impl.ConditionsFile;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.Material;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ConditionRegistry {
    private static final Map<String, ConditionType> types = new HashMap<>();
    private static final Map<String, AbstractCondition> instances = new HashMap<>();

    private static final ConditionsFile file = new ConditionsFile();
    private static final ConditionFactory factory = new ConditionFactory();

    private ConditionRegistry() {
    }

    static {
        registerType(new ConditionType("BLOCK_AGE", BlockAgeCondition::fromConfig, Material.CLOCK));
    }

    public static void registerType(ConditionType type) {
        String key = type.getKey().toUpperCase();
        if (types.containsKey(key)) {
            throw new IllegalStateException("Condition type already registered: " + key);
        }
        types.put(key, type);
    }

    public static ConditionType getType(String key) {
        ConditionType type = types.get(key.toUpperCase());
        if (type == null) {
            throw new IllegalArgumentException(
                    "Unknown condition type: '" + key + "'. " +
                            "Registered types: " + types.keySet()
            );
        }
        return type;
    }

    public static boolean isTypeRegistered(String key) {
        return types.containsKey(key.toUpperCase());
    }

    public static void loadFromFile() {
        Map<String, Map<String, Object>> data = file.loadAll();
        data.forEach((key, config) -> {
            try {
                String typeKey = ((String) config.get("type")).toUpperCase();
                ConditionType type = getType(typeKey);
                AbstractCondition condition = type.getBuilder().build(new DomainConfig(config));
                condition.setId(key);
                condition.setTypeKey(typeKey);

                // Evaluator is optional
                Object evaluatorRaw = config.get("evaluator");
                if (evaluatorRaw instanceof String evaluatorStr) {
                    condition.setEvaluator(Evaluator.valueOf(evaluatorStr.toUpperCase()));
                }

                instances.put(key, condition);
                factory.register(key, condition); // keep factory in sync for parseHolder()
            } catch (Exception e) {
                log("Failed to load condition '" + key + "': " + e.getMessage());
            }
        });
    }

    public static void saveAll() {
        Map<String, Object> serialized = new HashMap<>();
        instances.forEach((key, condition) -> serialized.put(key, condition.serialize()));
        file.saveAll(serialized)
                .exceptionally(e -> {
                    log("Failed to save conditions: " + e.getMessage());
                    return null;
                });
    }

    /**
     * Registers a runtime-created condition and persists it.
     */
    public static void register(AbstractCondition condition) {
        instances.put(condition.getId(), condition);
        factory.register(condition.getId(), condition);
        file.save(condition.getId(), condition.serialize());
    }

    public static AbstractCondition get(String id) {
        AbstractCondition condition = instances.get(id);
        if (condition == null) {
            throw new IllegalArgumentException(
                    "No condition with id '" + id + "'. " +
                            "Registered: " + instances.keySet()
            );
        }
        return condition;
    }

    /**
     * Used when building ConditionHolders from config blocks at runtime.
     */
    public static ConditionFactory factory() {
        return factory;
    }

    public static Map<String, AbstractCondition> allInstances() {
        return Collections.unmodifiableMap(instances);
    }

    public static Map<String, ConditionType> allTypes() {
        return Collections.unmodifiableMap(types);
    }

    static void reset() {
        types.clear();
        instances.clear();
    }

    private static void log(String message) {
        XenithLibrary.getInstance().getLogger().warning(message);
    }
}