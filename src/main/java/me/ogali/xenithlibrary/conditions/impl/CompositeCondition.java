package me.ogali.xenithlibrary.conditions.impl;

import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.ConditionContext;
import me.ogali.xenithlibrary.conditions.domain.ConditionRegistry;
import me.ogali.xenithlibrary.shared.DomainConfig;
import me.ogali.xenithlibrary.shared.Operator;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CompositeCondition extends AbstractCondition {
    private final List<String> conditionIds;
    private Operator operator;

    public CompositeCondition(List<String> conditionIds, Operator operator) {
        this.conditionIds = conditionIds;
        this.operator = operator;
    }

    public static AbstractCondition fromConfig(DomainConfig config) {
        String operatorStr = config.getString("operator", "AND");
        Operator operator = Operator.valueOf(operatorStr.toUpperCase());
        List<String> ids = config.getStringList("conditions");
        return new CompositeCondition(ids, operator);
    }

    @Override
    public boolean test(ConditionContext context) {
        List<AbstractCondition> resolved = conditionIds.stream()
                .map(ConditionRegistry::get)
                .toList();

        return switch (operator) {
            case AND -> resolved.stream().allMatch(c -> c.test(context));
            case OR -> resolved.stream().anyMatch(c -> c.test(context));
        };
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("operator", operator.name());
        data.put("conditions", conditionIds);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "operator" -> this.operator = Operator.valueOf(value.toUpperCase());
            case "conditions" -> this.conditionIds.add(value);
            default -> super.applyEdit(field, value);
        }
    }
}