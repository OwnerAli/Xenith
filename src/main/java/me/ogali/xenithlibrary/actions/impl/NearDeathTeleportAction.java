package me.ogali.xenithlibrary.actions.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.actions.domain.AbstractAction;
import me.ogali.xenithlibrary.actions.domain.ActionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public class NearDeathTeleportAction extends AbstractAction {

    private double teleportDistance;
    private double healAmountHearts; // Stored in hearts, converted to health points (2 per heart) for Bukkit
    private String message;

    public NearDeathTeleportAction(double teleportDistance, double healAmountHearts, String message) {
        this.teleportDistance = teleportDistance;
        this.healAmountHearts = healAmountHearts;
        this.message = message;
    }

    @Override
    public void execute(ActionContext context) {
        if (!rolledSuccessfully()) return;
        Player player = context.getPlayer();
        if (player == null) return;

        // Teleport player away
        Location currentLocation = player.getLocation();
        // Teleport player backwards relative to their facing direction and slightly up to prevent clipping
        Location newLocation = currentLocation.subtract(currentLocation.getDirection().multiply(teleportDistance)).add(0, 1, 0);

        // Ensure new location is safe (not in a wall, etc.) - simple check for now
        // For more robust teleportation, consider a utility to find the highest safe block
        if (newLocation.getWorld() != null && newLocation.getWorld().isChunkLoaded(newLocation.getChunk())) {
            player.teleport(newLocation);
        } else {
            // Fallback if the calculated location is not loaded or invalid, teleport directly up
            player.teleport(currentLocation.add(0, teleportDistance, 0));
        }

        // Heal player
        double currentHealth = player.getHealth();
        double maxHealth = player.getMaxHealth();
        double healthToAdd = healAmountHearts * 2; // Convert hearts to Bukkit health points
        player.setHealth(Math.min(maxHealth, currentHealth + healthToAdd));

        // Send message to player
        if (message != null && !message.isEmpty()) {
            String processedMessage = message;
            if (XenithLibrary.isPapiEnabled()) {
                processedMessage = PlaceholderAPI.setPlaceholders(player, processedMessage);
            }
            player.sendMessage(Chat.colorize(processedMessage));
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data =
                new LinkedHashMap<>(super.serialize());

        data.put("teleportDistance", teleportDistance);
        data.put("healAmountHearts", healAmountHearts);
        data.put("message", message);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "teleportDistance" -> this.teleportDistance = Double.parseDouble(value);
            case "healAmountHearts" -> this.healAmountHearts = Double.parseDouble(value);
            case "message" -> this.message = value;
            default -> super.applyEdit(field, value);
        }
    }

    public static AbstractAction fromConfig(DomainConfig config) {
        double teleportDistance = config.getDouble("teleportDistance", 10.0);
        double healAmountHearts = config.getDouble("healAmountHearts", 3.0);
        String message = config.getString("message", "&aYou were saved from death! Teleported &e%teleportDistance% blocks &aaway and healed &e%healAmountHearts% hearts!");

        NearDeathTeleportAction action = new NearDeathTeleportAction(teleportDistance, healAmountHearts, message);
        action.setChance(config.getDouble("chance", 100.0)); // Default 100% chance if not specified

        return action;
    }
}