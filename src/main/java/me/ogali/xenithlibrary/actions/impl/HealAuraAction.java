package me.ogali.xenithlibrary.actions.impl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.actions.domain.AbstractAction;
import me.ogali.xenithlibrary.actions.domain.ActionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor // Required for DomainConfig and potential direct instantiation
public class HealAuraAction extends AbstractAction {

    private double range = 5.0;
    private double healAmount = 2.0;
    private String sound = "ENTITY_PLAYER_LEVELUP"; // Default sound
    private String particle = "HEART"; // Default particle
    private boolean healSelf = false; // If true, heal the player who triggered it. If false, skip.
    private String message = null; // Optional message to players when healed

    @Override
    public void execute(ActionContext context) {
        if (!rolledSuccessfully()) return;
        Player player = context.getPlayer();
        if (player == null || !player.isOnline()) return;

        Sound actualSound = null;
        try {
            actualSound = Sound.valueOf(this.sound.toUpperCase());
        } catch (IllegalArgumentException e) {
            XenithLibrary.getInstance().getLogger().warning("Invalid sound '" + this.sound + "' configured for HealAuraAction. Skipping sound effect.");
        }

        Particle actualParticle = null;
        try {
            actualParticle = Particle.valueOf(this.particle.toUpperCase());
        } catch (IllegalArgumentException e) {
            XenithLibrary.getInstance().getLogger().warning("Invalid particle '" + this.particle + "' configured for HealAuraAction. Skipping particle effect.");
        }

        for (Entity entity : player.getNearbyEntities(range, range, range)) {
            if (!(entity instanceof Player targetPlayer)) {
                continue;
            }

            if (!healSelf && targetPlayer.equals(player)) {
                continue; // Skip the triggering player if healSelf is false
            }

            // Heal the player
            double currentHealth = targetPlayer.getHealth();
            double maxHealth = targetPlayer.getMaxHealth();
            double newHealth = Math.min(maxHealth, currentHealth + healAmount);
            targetPlayer.setHealth(newHealth);

            // Play sound for the healed player
            if (actualSound != null) {
                targetPlayer.playSound(targetPlayer.getLocation(), actualSound, 1.0f, 1.0f);
            }

            // Spawn particles for the healed player
            if (actualParticle != null) {
                // Spawn particles slightly above the player
                targetPlayer.spawnParticle(actualParticle, targetPlayer.getLocation().add(0, 1, 0), 10, 0.5, 0.5, 0.5);
            }

            // Send message if configured
            if (message != null && !message.isEmpty()) {
                String resolvedMessage = message;
                if (XenithLibrary.isPapiEnabled()) {
                    resolvedMessage = PlaceholderAPI.setPlaceholders(targetPlayer, resolvedMessage);
                }
                targetPlayer.sendMessage(Chat.colorize(resolvedMessage));
            }
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("range", range);
        data.put("healAmount", healAmount);
        data.put("sound", sound);
        data.put("particle", particle);
        data.put("healSelf", healSelf);
        data.put("message", message);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "range" -> this.range = Double.parseDouble(value);
            case "healAmount" -> this.healAmount = Double.parseDouble(value);
            case "sound" -> this.sound = value;
            case "particle" -> this.particle = value;
            case "healSelf" -> this.healSelf = Boolean.parseBoolean(value);
            case "message" -> this.message = value.equalsIgnoreCase("null") || value.isEmpty() ? null : value;
            default -> super.applyEdit(field, value);
        }
    }

    public static AbstractAction fromConfig(DomainConfig config) {
        HealAuraAction action = new HealAuraAction();
        action.setRange(config.getDouble("range", 5.0));
        action.setHealAmount(config.getDouble("healAmount", 2.0));
        action.setSound(config.getString("sound", "ENTITY_PLAYER_LEVELUP"));
        action.setParticle(config.getString("particle", "HEART"));
        action.setHealSelf(config.getBoolean("healSelf", false));
        action.setMessage(config.getString("message", null));
        action.setChance(config.getDouble("chance", 100.0));
        return action;
    }
}