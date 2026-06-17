package dev.xenith.plugin.actions;

import dev.xenith.core.action.AbstractAction;
import dev.xenith.core.action.ActionContext;
import dev.xenith.core.config.DomainConfig;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.LinkedHashMap;
import java.util.Map;

public class PushPlayerAction extends AbstractAction {

    private double pushAmount;
    private String pushDirection; // "up", "down", "forward", "backward", "left", "right"

    public PushPlayerAction(double pushAmount, String pushDirection) {
        this.pushAmount = pushAmount;
        this.pushDirection = pushDirection;
    }

    @Override
    public void execute(ActionContext context) {
        if (!rolledSuccessfully()) return;
        Player player = context.getPlayer();
        if (player == null) return;

        Vector directionVector = calculateDirectionVector(player);
        if (directionVector == null) {
            // Invalid pushDirection specified, or an error occurred during calculation.
            // Log this if necessary for debugging.
            return;
        }

        Vector currentVelocity = player.getVelocity();
        // Add the calculated push vector to the player's current velocity
        Vector newVelocity = currentVelocity.add(directionVector.multiply(pushAmount));

        player.setVelocity(newVelocity);
    }

    /**
     * Calculates the unit vector for the specified push direction relative to the player.
     * Horizontal directions (forward, backward, left, right) are flattened to the XZ plane.
     *
     * @param player The player whose direction is used as a reference.
     * @return A unit vector representing the push direction, or null if the direction is invalid.
     */
    private Vector calculateDirectionVector(Player player) {
        Vector playerDirection = player.getLocation().getDirection();

        return switch (pushDirection.toLowerCase()) {
            case "up" -> new Vector(0, 1, 0);
            case "down" -> new Vector(0, -1, 0);
            case "forward" -> {
                playerDirection.setY(0).normalize(); // Flatten and normalize for horizontal
                yield playerDirection;
            }
            case "backward" -> {
                playerDirection.setY(0).normalize(); // Flatten and normalize for horizontal
                yield playerDirection.multiply(-1);
            }
            case "left" -> {
                playerDirection.setY(0).normalize(); // Flatten and normalize for horizontal
                yield playerDirection.rotateAroundY(Math.toRadians(-90)); // Rotate -90 degrees around Y-axis
            }
            case "right" -> {
                playerDirection.setY(0).normalize(); // Flatten and normalize for horizontal
                yield playerDirection.rotateAroundY(Math.toRadians(90)); // Rotate 90 degrees around Y-axis
            }
            default -> null; // Invalid direction
        };
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("pushAmount", pushAmount);
        data.put("pushDirection", pushDirection);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "pushAmount" -> this.pushAmount = Double.parseDouble(value);
            case "pushDirection" -> this.pushDirection = value;
            default -> super.applyEdit(field, value);
        }
    }

    public static AbstractAction fromConfig(DomainConfig config) {
        double pushAmount = config.getDouble("pushAmount", 1.0);
        String pushDirection = config.getString("pushDirection", "up");

        PushPlayerAction action = new PushPlayerAction(pushAmount, pushDirection);
        action.setChance(config.getDouble("chance", 100.0));
        return action;
    }
}