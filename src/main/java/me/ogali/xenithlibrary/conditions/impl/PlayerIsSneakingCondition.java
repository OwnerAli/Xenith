package me.ogali.xenithlibrary.conditions.impl;

import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.ConditionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerIsSneakingCondition extends AbstractCondition {

    private boolean sneaking;

    public PlayerIsSneakingCondition(boolean sneaking) {
        this.sneaking = sneaking;
    }

    @Override
    public boolean test(ConditionContext context) {
        Player player = context.getPlayer();
        if (player == null) return false;
        return evaluate(String.valueOf(player.isSneaking()), String.valueOf(sneaking));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("sneaking", sneaking);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "sneaking" -> this.sneaking = Boolean.parseBoolean(value);
            default -> super.applyEdit(field, value);
        }
    }

    public static AbstractCondition fromConfig(DomainConfig config) {
        return new PlayerIsSneakingCondition(config.getBoolean("sneaking", true));
    }
}