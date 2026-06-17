package com.xenith.plugin.action.impl;

import com.xenith.library.action.AbstractAction;
import com.xenith.library.action.ActionContext;
import com.xenith.library.config.DomainConfig;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.LinkedHashMap;
import java.util.Map;

public class ReduceDamageAction extends AbstractAction {

    private double percentage; // The percentage to reduce damage by (e.g., 25.0 for 25%)

    public ReduceDamageAction(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public void execute(ActionContext context) {
        if (!rolledSuccessfully()) return;

        // Assume ActionContext provides access to the raw triggering event.
        // This is necessary for actions that modify event properties.
        Event event = context.getEvent(); 

        if (event instanceof EntityDamageEvent damageEvent) {
            if (damageEvent.isCancelled() || percentage < 0 || percentage > 100) {
                return;
            }

            double currentDamage = damageEvent.getDamage();
            double reductionFactor = percentage / 100.0;
            double reducedDamage = currentDamage * (1.0 - reductionFactor);

            if (reducedDamage < 0) {
                reducedDamage = 0;
            }
            
            damageEvent.setDamage(reducedDamage);
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("percentage", percentage);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "percentage" -> {
                try {
                    this.percentage = Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    // Log or handle the error appropriately in a real plugin
                    System.err.println("Xenith: Invalid number format for 'percentage' in ReduceDamageAction: " + value);
                }
            }
            default -> super.applyEdit(field, value);
        }
    }

    public static AbstractAction fromConfig(DomainConfig config) {
        double percentage = config.getDouble("percentage", 0.0); // Default to 0% reduction
        
        ReduceDamageAction action = new ReduceDamageAction(percentage);
        action.setChance(config.getDouble("chance", 100.0)); // Default to 100% chance to apply
        
        return action;
    }
}