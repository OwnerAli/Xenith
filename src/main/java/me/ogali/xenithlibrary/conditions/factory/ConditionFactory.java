package me.ogali.xenithlibrary.conditions.factory;

import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.Condition;
import me.ogali.xenithlibrary.conditions.domain.ConditionHolder;
import me.ogali.xenithlibrary.conditions.domain.ConditionType;
import me.ogali.xenithlibrary.conditions.evaluator.Evaluator;
import me.ogali.xenithlibrary.files.YmlFileManager;
import me.ogali.xenithlibrary.files.impl.ConditionFile;
import me.ogali.xenithlibrary.shared.ConfigBuilder;
import me.ogali.xenithlibrary.shared.DomainConfig;

import java.util.*;

public class ConditionFactory {
    private final HashMap<String, AbstractCondition> registry;
    private final ConditionFile conditionFile;

    public ConditionFactory() {
        this.registry = new HashMap<>();
        this.conditionFile = new ConditionFile();
        registerAllConditionTypes();
    }

    public void registerAll(Map<String, Map<String, Object>> conditions) {
        for (String key : conditions.keySet()) {
            AbstractCondition condition = create(key, conditions);
            registry.put(key, condition);
        }
    }

    public void saveAll() {
        YmlFileManager ymlFileManager = new YmlFileManager(XenithLibrary.getInstance().getDataFolder());
        ymlFileManager.createNewFile("conditions");
        registry.forEach((key, value) -> {
            ymlFileManager.save("conditions", key, value);
        });
    }

    public AbstractCondition create(String key, Map<String, Map<String, Object>> conditions) {
        DomainConfig config = ConfigBuilder.resolveConfig(key, conditions);
        ConditionType type = ConditionType.valueOf(config.getUppercaseString("type"));
        ConfigBuilder<AbstractCondition> builder = type.getBuilder();

        if (builder == null) {
            throw new IllegalArgumentException("Unknown condition type: " + type);
        }

        // Get id from config
        String id = config.getString("id");

        // Get evaluator from config
        String evaluatorFromConfig = config.getUppercaseString("evaluator");
        Evaluator evaluator = null;
        if (evaluatorFromConfig != null && !evaluatorFromConfig.isEmpty()) {
            evaluator = Evaluator.valueOf(evaluatorFromConfig);
        }
        // Set id for built condition
        AbstractCondition condition = builder.build(config);
        condition.setId(id);

        if (evaluator != null) {
            condition.setEvaluator(evaluator);
        }

        return condition;
    }

    public void registerAllConditionTypes() {
    }

    public ConditionHolder createConditionHolder(List<Object> conditions) {
        List<Condition> resolvedConditions = new ArrayList<>();

        for (Object item : conditions) {
            if (item instanceof Map<?, ?> map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> conditionData = (Map<String, Object>) map;

                // Composite condition by ID string
                if (conditionData.containsKey("id") && conditionData.size() == 1) {
                    String id = (String) conditionData.get("id");

                    Condition resolved = parseCompositeId(id);
                    resolvedConditions.add(resolved);
                } else {
                    // Inline condition (not by id)
                    String tempKey = UUID.randomUUID().toString();
                    Map<String, Map<String, Object>> wrapper = Map.of(tempKey, conditionData);
                    Condition inlineCondition = create(tempKey, wrapper);
                    resolvedConditions.add(inlineCondition);
                }
            } else {
                throw new IllegalArgumentException("Condition entry must be a map: " + item);
            }
        }

        return new ConditionHolder(resolvedConditions);
    }

    private Condition parseCompositeId(String id) {
        String[] andParts = id.split("\\s+and\\s+");

        if (andParts.length > 1) {
            return Arrays.stream(andParts)
                    .map(this::parseOrComposite)
                    .reduce(Condition::and)
                    .orElseThrow();
        }

        return parseOrComposite(id);
    }

    private Condition parseOrComposite(String part) {
        String[] orParts = part.split("\\s+or\\s+");

        if (orParts.length > 1) {
            return Arrays.stream(orParts)
                    .map(String::trim)
                    .map(this::resolveSingleId)
                    .reduce(Condition::or)
                    .orElseThrow();
        }

        return resolveSingleId(part.trim());
    }

    private Condition resolveSingleId(String id) {
        Condition reusable = registry.get(id);
        if (reusable == null) {
            throw new IllegalArgumentException("Unknown reusable condition: " + id);
        }
        return reusable;
    }
}
