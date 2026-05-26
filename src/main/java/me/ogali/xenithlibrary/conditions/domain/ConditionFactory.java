package me.ogali.xenithlibrary.conditions.domain;

import me.ogali.xenithlibrary.conditions.evaluator.Evaluator;
import me.ogali.xenithlibrary.shared.DomainConfig;

import java.util.*;

public class ConditionFactory {
    private final Map<String, AbstractCondition> named = new HashMap<>();

    public void register(String key, AbstractCondition condition) {
        named.put(key, condition);
    }

    public Map<String, AbstractCondition> getAllNamed() {
        return Collections.unmodifiableMap(named);
    }

    public Condition getNamed(String id) {
        Condition condition = named.get(id);
        if (condition == null) {
            throw new IllegalArgumentException(
                    "No named condition with id '" + id + "'. " +
                            "Defined: " + named.keySet()
            );
        }
        return condition;
    }

    public void reset() {
        named.clear();
    }

    public AbstractCondition parse(String key, Map<String, Map<String, Object>> conditions) {
        DomainConfig config = new DomainConfig(conditions.get(key));

        String typeKey = config.getUppercaseString("type");
        ConditionType type = ConditionRegistry.getType(typeKey); // fixed

        AbstractCondition condition = type.getBuilder().build(config);
        condition.setId(config.getString("id"));
        condition.setTypeKey(typeKey); // set typeKey for serialization

        String evaluatorKey = config.getUppercaseString("evaluator");
        if (evaluatorKey != null && !evaluatorKey.isBlank()) {
            condition.setEvaluator(Evaluator.valueOf(evaluatorKey));
        }

        return condition;
    }

    /**
     * Parses a list of condition entries (from YAML) into a ConditionHolder.
     * <p>
     * Each entry is either:
     * { id: "myCondition" }            → reference to a named condition
     * { id: "condA and condB" }        → AND composite
     * { id: "condA or condB" }         → OR composite
     * { type: BLOCK_AGE, age: 7 }      → inline anonymous condition
     */
    public ConditionHolder parseHolder(List<Map<String, Object>> entries) {
        List<Condition> resolved = new ArrayList<>();

        for (Map<String, Object> entry : entries) {
            if (entry.containsKey("id") && entry.size() == 1) {
                resolved.add(resolveIdExpression((String) entry.get("id")));
            } else {
                String tempKey = UUID.randomUUID().toString();
                resolved.add(parse(tempKey, Map.of(tempKey, entry)));
            }
        }

        return new ConditionHolder(resolved);
    }

    private Condition resolveIdExpression(String expression) {
        String[] orParts = expression.split("\\s+or\\s+");
        return Arrays.stream(orParts)
                .map(this::resolveAndExpression)
                .reduce(Condition::or)
                .orElseThrow();
    }

    private Condition resolveAndExpression(String part) {
        String[] andParts = part.split("\\s+and\\s+");
        return Arrays.stream(andParts)
                .map(String::trim)
                .map(this::getNamed)
                .reduce(Condition::and)
                .orElseThrow();
    }
}