package me.ogali.xenithlibrary.conditions.domain;

import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.conditions.evaluator.Evaluator;
import me.ogali.xenithlibrary.conditions.impl.*;
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

    static {
        registerType(new ConditionType("COMPOSITE", CompositeCondition::fromConfig, Material.IRON_CHAIN));
        registerType(new ConditionType("BLOCK_TYPE", BlockTypeCondition::fromConfig, Material.GRASS_BLOCK));
        registerType(new ConditionType("BLOCK_AGE", BlockAgeCondition::fromConfig, Material.CLOCK));
        registerType(new ConditionType("BLOCK_BIOME", BlockBiomeCondition::fromConfig, Material.FERN));
        registerType(new ConditionType("BLOCK_WORLD", BlockWorldCondition::fromConfig, Material.ENDER_PEARL));
        registerType(new ConditionType("TOOL_MATERIAL", ToolMaterialCondition::fromConfig, Material.IRON_PICKAXE));
        registerType(new ConditionType("TOOL_ENCHANTMENT", ToolEnchantmentCondition::fromConfig, Material.ENCHANTING_TABLE));
        registerType(new ConditionType("TOOL_ENCHANTMENT_LEVEL", ToolEnchantmentLevelCondition::fromConfig, Material.EXPERIENCE_BOTTLE));
        registerType(new ConditionType("TOOL_DURABILITY", ToolDurabilityCondition::fromConfig, Material.ANVIL));
        registerType(new ConditionType("TOOL_NAME", ToolNameCondition::fromConfig, Material.NAME_TAG));
        registerType(new ConditionType("TOOL_LORE", ToolLoreCondition::fromConfig, Material.WRITABLE_BOOK));
        registerType(new ConditionType("PLAYER_GAMEMODE", PlayerGamemodeCondition::fromConfig, Material.COMPASS));
        registerType(new ConditionType("PLAYER_WORLD", PlayerWorldCondition::fromConfig, Material.GRASS_BLOCK));
        registerType(new ConditionType("PLAYER_HEALTH", PlayerHealthCondition::fromConfig, Material.RED_DYE));
        registerType(new ConditionType("PLAYER_LEVEL", PlayerLevelCondition::fromConfig, Material.EXPERIENCE_BOTTLE));
        registerType(new ConditionType("PLAYER_PERMISSION", PlayerPermissionCondition::fromConfig, Material.GOLDEN_APPLE));
        registerType(new ConditionType("PLAYER_IS_SNEAKING", PlayerIsSneakingCondition::fromConfig, Material.LEATHER_BOOTS));
        registerType(new ConditionType("PLAYER_IS_SPRINTING", PlayerIsSprintingCondition::fromConfig, Material.FEATHER));
        registerType(new ConditionType("PLAYER_BIOME", PlayerBiomeCondition::fromConfig, Material.SUNFLOWER));
        registerType(new ConditionType("PLACEHOLDER", PlaceholderCondition::fromConfig, Material.ITEM_FRAME));
    }

    private ConditionRegistry() {
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
                instances.put(key, parse(key, config));
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

    public static void register(AbstractCondition condition) {
        instances.put(condition.getId(), condition);
        file.save(condition.getId(), condition.serialize());
    }

    public static void delete(String id) {
        instances.remove(id);
        file.delete(id);
    }

    public static AbstractCondition get(String id) {
        AbstractCondition condition = instances.get(id);
        if (condition == null) {
            throw new IllegalArgumentException(
                    "No condition with id '" + id + "'. Registered: " + instances.keySet()
            );
        }
        return condition;
    }

    public static boolean isRegistered(String id) {
        return instances.containsKey(id);
    }

    /**
     * Parses a single condition config map into an AbstractCondition.
     * Used internally during loadFromFile and by external plugins
     * that want to build conditions programmatically.
     */
    public static AbstractCondition parse(String id, Map<String, Object> config) {
        DomainConfig domainConfig = new DomainConfig(config);

        String typeKey = domainConfig.getUppercaseString("type");
        ConditionType type = getType(typeKey);

        AbstractCondition condition = type.getBuilder().build(domainConfig);
        condition.setId(id);
        condition.setTypeKey(typeKey);

        String evaluatorKey = domainConfig.getUppercaseString("evaluator");
        if (evaluatorKey != null && !evaluatorKey.isBlank()) {
            condition.setEvaluator(Evaluator.valueOf(evaluatorKey));
        }

        return condition;
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