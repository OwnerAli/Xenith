package me.ogali.xenithlibrary.conditions.impl;

import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.ConditionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerLevelCondition extends AbstractCondition {

    private int level;

    public PlayerLevelCondition(int level) {
        this.level = level;
    }

    @Override
    public boolean test(ConditionContext context) {
        if (!(context.getBukkitEvent() instanceof BlockBreakEvent event)) return false;
        return evaluate(String.valueOf(event.getPlayer().getLevel()), String.valueOf(level));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("level", level);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "level" -> this.level = Integer.parseInt(value);
            default      -> super.applyEdit(field, value);
        }
    }

    public static AbstractCondition fromConfig(DomainConfig config) {
        return new PlayerLevelCondition(config.getInt("level", 0));
    }
}