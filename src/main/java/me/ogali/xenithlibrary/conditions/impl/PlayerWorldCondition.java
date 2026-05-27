package me.ogali.xenithlibrary.conditions.impl;

import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.ConditionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerWorldCondition extends AbstractCondition {

    private String world;

    public PlayerWorldCondition(String world) {
        this.world = world;
    }

    @Override
    public boolean test(ConditionContext context) {
        if (!(context.getBukkitEvent() instanceof BlockBreakEvent event)) return false;
        return evaluate(event.getPlayer().getWorld().getName(), world);
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
            default      -> super.applyEdit(field, value);
        }
    }

    public static AbstractCondition fromConfig(DomainConfig config) {
        return new PlayerWorldCondition(config.getString("world", "world"));
    }
}