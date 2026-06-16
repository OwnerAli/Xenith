package me.ogali.xenithlibrary.conditions.impl;

import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.ConditionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerPermissionCondition extends AbstractCondition {

    private String permission;

    public PlayerPermissionCondition(String permission) {
        this.permission = permission;
    }

    @Override
    public boolean test(ConditionContext context) {
        Player player = context.getPlayer();
        if (player == null) return false;
        boolean has = player.hasPermission(permission);
        return evaluate(String.valueOf(has), "true");
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("permission", permission);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "permission" -> this.permission = value;
            default -> super.applyEdit(field, value);
        }
    }

    public static AbstractCondition fromConfig(DomainConfig config) {
        return new PlayerPermissionCondition(config.getString("permission", ""));
    }
}