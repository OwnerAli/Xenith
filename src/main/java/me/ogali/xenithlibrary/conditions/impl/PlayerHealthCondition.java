package me.ogali.xenithlibrary.conditions.impl;

import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.ConditionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerHealthCondition extends AbstractCondition {

    private double health;

    public PlayerHealthCondition(double health) {
        this.health = health;
    }

    @Override
    public boolean test(ConditionContext context) {
        Player player = context.getPlayer();
        if (player == null) return false;
        return evaluate(String.valueOf(player.getHealth()), String.valueOf(health));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("health", health);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "health" -> this.health = Double.parseDouble(value);
            default -> super.applyEdit(field, value);
        }
    }

    public static AbstractCondition fromConfig(DomainConfig config) {
        return new PlayerHealthCondition(config.getDouble("health", 20.0));
    }
}