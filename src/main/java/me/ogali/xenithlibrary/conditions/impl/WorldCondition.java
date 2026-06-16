package me.ogali.xenithlibrary.conditions.impl;

import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.ConditionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.World;

import java.util.LinkedHashMap;
import java.util.Map;

public class WorldCondition extends AbstractCondition {

    private String world;

    public WorldCondition(String world) {
        this.world = world;
    }

    @Override
    public boolean test(ConditionContext context) {
        World contextWorld = context.getWorld();
        if (contextWorld == null) return false;
        return evaluate(contextWorld.getName(), world);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("world", world);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "world" -> this.world = value;
            default -> super.applyEdit(field, value);
        }
    }

    public static AbstractCondition fromConfig(DomainConfig config) {
        return new WorldCondition(config.getString("world", "world"));
    }
}