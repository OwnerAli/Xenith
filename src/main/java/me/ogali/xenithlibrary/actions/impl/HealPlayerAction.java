package me.ogali.xenithlibrary.actions.impl;

import me.ogali.xenithlibrary.actions.domain.AbstractAction;
import me.ogali.xenithlibrary.actions.domain.ActionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public class HealPlayerAction extends AbstractAction {
    private double healAmount;

    public HealPlayerAction(double healAmount) {
        this.healAmount = healAmount;
    }

    @Override
    public void execute(ActionContext context) {
        if (!rolledSuccessfully()) return;
        Player player = context.getPlayer();
        if (player == null) return;

        double currentHealth = player.getHealth();
        double maxHealth = player.getMaxHealth();
        double newHealth = Math.min(currentHealth + healAmount, maxHealth);

        player.setHealth(newHealth);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("healAmount", healAmount);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        if (field.equals("healAmount")) {
            this.healAmount = Double.parseDouble(value);
        } else {
            super.applyEdit(field, value);
        }
    }

    public static AbstractAction fromConfig(DomainConfig config) {
        double healAmount = config.getDouble("healAmount", 0.0);
        HealPlayerAction action = new HealPlayerAction(healAmount);
        action.setChance(config.getDouble("chance", 100.0));
        return action;
    }
}