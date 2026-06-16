package me.ogali.xenithlibrary.conditions.impl;

import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.ConditionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerGamemodeCondition extends AbstractCondition {

    private String gamemode;

    public PlayerGamemodeCondition(String gamemode) {
        this.gamemode = gamemode;
    }

    @Override
    public boolean test(ConditionContext context) {
        Player player = context.getPlayer();
        if (player == null) return false;
        return evaluate(player.getGameMode().name(), gamemode.toUpperCase());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("gamemode", gamemode);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "gamemode" -> this.gamemode = value.toUpperCase();
            default -> super.applyEdit(field, value);
        }
    }

    public static AbstractCondition fromConfig(DomainConfig config) {
        return new PlayerGamemodeCondition(config.getString("gamemode", GameMode.SURVIVAL.name()));
    }
}