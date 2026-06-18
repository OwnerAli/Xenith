package me.ogali.xenithlibrary.conditions.domain;

import me.ogali.xenithlibrary.shared.DomainConfig;

/**
 * Builds a condition from a parsed config section.
 * Implement this as a static factory method on each condition class.
 * <p>
 * Example:
 * new ConditionType("BLOCK_AGE", BlockAgeCondition::fromConfig)
 */
@FunctionalInterface
public interface ConditionBuilder {
    AbstractCondition build(DomainConfig config);
}