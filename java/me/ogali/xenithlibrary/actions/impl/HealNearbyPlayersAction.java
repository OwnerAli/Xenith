package me.ogali.xenithlibrary.actions.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.actions.AbstractAction;
import me.ogali.xenithlibrary.actions.ActionContext;
import me.ogali.xenithlibrary.actions.ActionRegistry;
import me.ogali.xenithlibrary.config.DomainConfig;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public class HealNearbyPlayersAction extends AbstractAction {

    private double radius;
    private double healAmount;

    static {
        ActionRegistry.registerType("heal-nearby-players", HealNearbyPlayersAction::fromConfig);
    }

    public HealNearbyPlayersAction() {
        // Default constructor
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getHealAmount() {
        return healAmount;
    }

    public void setHealAmount(double healAmount) {
        this.healAmount = healAmount;
    }

    @Override
    public void execute(ActionContext context) {
        Player player = context.getPlayer();
        if (player == null || !player.isOnline()) {
            return;
        }

        if (!rolledSuccessfully()) {
            sendFailMessage(player);
            return;
        }

        double finalRadius = this.radius;
        double finalHealAmount = this.healAmount;

        if (XenithLibrary.isPapiEnabled()) {
            try {
                String processedRadius = PlaceholderAPI.setPlaceholders(player, String.valueOf(this.radius));
                finalRadius = Double.parseDouble(processedRadius);
            } catch (NumberFormatException e) {
                XenithLibrary.getInstance().getLogger().warning("Failed to parse radius placeholder for " + player.getName() + ": " + this.radius);
            }

            try {
                String processedHealAmount = PlaceholderAPI.setPlaceholders(player, String.valueOf(this.healAmount));
                finalHealAmount = Double.parseDouble(processedHealAmount);
            } catch (NumberFormatException e) {
                XenithLibrary.getInstance().getLogger().warning("Failed to parse heal-amount placeholder for " + player.getName() + ": " + this.healAmount);
            }
        }

        int healedPlayersCount = 0;
        for (Player nearbyPlayer : player.getWorld().getPlayers()) {
            if (nearbyPlayer.isOnline() && nearbyPlayer.getLocation().distance(player.getLocation()) <= finalRadius) {
                double currentHealth = nearbyPlayer.getHealth();
                double maxHealth = nearbyPlayer.getMaxHealth();
                double newHealth = Math.min(currentHealth + finalHealAmount, maxHealth);
                nearbyPlayer.setHealth(newHealth);
                healedPlayersCount++;
            }
        }

        if (healedPlayersCount > 0) {
            sendSuccessMessage(player);
        } else if (!getFailMessage().isEmpty()) { // Only send fail message if no players were healed AND there's a fail message
            // Consider if sending a fail message here is appropriate or if it's only for the rolledSuccessfully() check.
            // For this action, if no players are found/healed, it's not strictly a "fail" due to chance.
            // Following existing patterns, success/fail messages are tied to the chance roll.
            // If the action successfully executed (passed the chance roll) but healed 0 players,
            // the success message might still be appropriate if it conveys "attempted to heal nearby players".
            // For now, only success message if players were healed, and rely on rolledSuccessfully() for fail message.
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>(super.serialize());
        map.put("radius", this.radius);
        map.put("heal-amount", this.healAmount);
        return map;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "radius":
                try {
                    this.radius = Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    XenithLibrary.getInstance().getLogger().warning("Invalid number format for radius: " + value);
                }
                break;
            case "heal-amount":
                try {
                    this.healAmount = Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    XenithLibrary.getInstance().getLogger().warning("Invalid number format for heal-amount: " + value);
                }
                break;
            default:
                super.applyEdit(field, value);
        }
    }

    public static HealNearbyPlayersAction fromConfig(DomainConfig config) {
        HealNearbyPlayersAction action = new HealNearbyPlayersAction();
        action.setRadius(config.getDouble("radius", 5.0)); // Default 5 block radius
        action.setHealAmount(config.getDouble("heal-amount", 4.0)); // Default 4 health (2 hearts)

        // Load common fields from AbstractAction
        loadCommonFields(config, action);

        return action;
    }
}