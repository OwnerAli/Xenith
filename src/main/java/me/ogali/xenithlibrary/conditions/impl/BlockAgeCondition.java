package me.ogali.xenithlibrary.conditions.impl;

import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.ConditionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.LinkedHashMap;
import java.util.Map;

public class BlockAgeCondition extends AbstractCondition {

    private int age;

    public BlockAgeCondition(int age) {
        this.age = age;
    }

    @Override
    public boolean test(ConditionContext context) {
        if (!(context.getBukkitEvent() instanceof BlockBreakEvent event)) return false;
        if (!(event.getBlock().getBlockData() instanceof Ageable ageable)) return false;
        return evaluate(String.valueOf(ageable.getAge()), String.valueOf(age));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("age", age);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "age" -> this.age = Integer.parseInt(value);
            default -> super.applyEdit(field, value);
        }
    }

    public static AbstractCondition fromConfig(DomainConfig config) {
        int age = config.getInt("age", 0);
        return new BlockAgeCondition(age);
    }
}