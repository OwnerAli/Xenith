package me.ogali.xenithlibrary.conditions.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds a set of conditions and evaluates them together.
 * All conditions must pass for checkAll() to return true.
 */
public final class ConditionHolder {
    private final List<AbstractCondition> conditions;

    public ConditionHolder(List<AbstractCondition> conditions) {
        this.conditions = new ArrayList<>(conditions);
    }

    public ConditionHolder(AbstractCondition... conditions) {
        this(List.of(conditions));
    }

    public static ConditionHolder empty() {
        return new ConditionHolder();
    }

    /**
     * Adds all conditions from another holder into this one.
     */
    public void merge(ConditionHolder other) {
        conditions.addAll(other.conditions);
    }

    /**
     * Returns true only if every condition passes.
     */
    public boolean checkAll(ConditionContext context) {
        return conditions.stream().allMatch(c -> c.test(context));
    }

    /**
     * Returns true if any condition passes.
     */
    public boolean checkAny(ConditionContext context) {
        return conditions.stream().anyMatch(c -> c.test(context));
    }

    public boolean isEmpty() {
        return conditions.isEmpty();
    }

    public List<String> toIdList() {
        return conditions.stream().map(AbstractCondition::getId).toList();
    }

    public ConditionHolder fromIdList(List<String> ids) {
        return new ConditionHolder(
                ids.stream()
                        .map(ConditionRegistry::get)
                        .toList()
        );
    }
}