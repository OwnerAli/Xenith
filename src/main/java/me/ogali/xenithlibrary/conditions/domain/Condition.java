package me.ogali.xenithlibrary.conditions.domain;

@FunctionalInterface
public interface Condition {
    boolean test(ConditionContext context);

    default Condition and(Condition other) {
        return context -> this.test(context) && other.test(context);
    }

    default Condition or(Condition other) {
        return context -> this.test(context) || other.test(context);
    }
}