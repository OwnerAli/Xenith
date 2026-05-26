package me.ogali.xenithlibrary.conditions.impl;

import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.ConditionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Objects;

public class BlockAgeCondition extends AbstractCondition {
    private final int age;

    public BlockAgeCondition(int age) {
        this.age = age;
    }

    @Override
    public boolean test(ConditionContext context) {
        if (!(context.getBukkitEvent() instanceof BlockBreakEvent event)) return false;
        if (!(event.getBlock().getBlockData() instanceof Ageable ageable)) return false;
        return evaluate(String.valueOf(ageable.getAge()), String.valueOf(age));
    }

    /**
     * Called by ConditionType registration — factory handles evaluator externally.
     */
    public static BlockAgeCondition fromConfig(DomainConfig config) {
        int age = Objects.requireNonNull(config.getInt("age"), "BlockAgeCondition requires 'age'");
        return new BlockAgeCondition(age);
    }
}