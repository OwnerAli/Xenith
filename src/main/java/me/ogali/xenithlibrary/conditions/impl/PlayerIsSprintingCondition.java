package me.ogali.xenithlibrary.conditions.impl;

import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.ConditionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerIsSprintingCondition extends AbstractCondition {

    private boolean sprinting;

    public PlayerIsSprintingCondition(boolean sprinting) {
        this.sprinting = sprinting;
    }

    @Override
    public boolean test(ConditionContext context) {
        Player player = context.getPlayer();
        if (player == null) return false;
        return evaluate(String.valueOf(player.isSprinting()), String.valueOf(sprinting));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("sprinting", sprinting);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "sprinting" -> this.sprinting = Boolean.parseBoolean(value);
            default -> super.applyEdit(field, value);
        }
    }

    public static AbstractCondition fromConfig(DomainConfig config) {
        return new PlayerIsSprintingCondition(config.getBoolean("sprinting", true));
    }
}