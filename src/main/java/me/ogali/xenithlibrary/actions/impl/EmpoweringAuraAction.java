package me.ogali.xenithlibrary.actions.impl;

import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.actions.AbstractAction;
import me.ogali.xenithlibrary.actions.ActionContext;
import me.ogali.xenithlibrary.config.DomainConfig;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.Plugin; // Explicitly import Plugin for XenithLibrary.getPlugin()

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class EmpoweringAuraAction extends AbstractAction {

    private Particle particleType;
    private int particleCount; // Particles spawned per tick around the circle
    private double particleRadius;
    private long durationTicks;
    private double damageIncrease;

    // Unique ID for the attribute modifier to avoid conflicts and allow specific removal
    private static final UUID DAMAGE_MODIFIER_UUID = UUID.fromString("93c8d35d-0e42-4f36-9b8e-3a8c51e06f5b");
    private static final String DAMAGE_MODIFIER_NAME = "XenithEmpoweringAura";

    public EmpoweringAuraAction(Particle particleType, int particleCount, double particleRadius, long durationTicks, double damageIncrease) {
        this.particleType = particleType;
        this.particleCount = particleCount;
        this.particleRadius = particleRadius;
        this.durationTicks = durationTicks;
        this.damageIncrease = damageIncrease;
    }

    @Override
    public void execute(ActionContext context) {
        if (!rolledSuccessfully()) return;
        Player player = context.getPlayer();
        if (player == null) return;

        Plugin plugin = XenithLibrary.getPlugin();
        if (plugin == null) {
            // Log error or handle gracefully if plugin instance isn't available
            return;
        }

        // --- Apply damage increase ---
        AttributeInstance attackDamageAttribute = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (attackDamageAttribute != null) {
            // Remove any existing modifier with the same UUID to prevent stacking
            // We create a "dummy" modifier with the correct UUID and name for removal matching
            AttributeModifier existingModifierCheck = new AttributeModifier(DAMAGE_MODIFIER_UUID, DAMAGE_MODIFIER_NAME, 0, Operation.ADD_NUMBER);
            if (attackDamageAttribute.getModifiers().stream().anyMatch(m -> m.getUniqueId().equals(DAMAGE_MODIFIER_UUID))) {
                attackDamageAttribute.removeModifier(existingModifierCheck);
            }

            AttributeModifier modifier = new AttributeModifier(DAMAGE_MODIFIER_UUID, DAMAGE_MODIFIER_NAME, damageIncrease, Operation.ADD_NUMBER);
            attackDamageAttribute.addModifier(modifier);

            // Schedule removal of the modifier after durationTicks
            Bukkit.getScheduler().runLater(plugin, () -> {
                // Re-fetch attribute instance as player object might change or attribute might be re-initialized
                Player onlinePlayer = Bukkit.getPlayer(player.getUniqueId());
                if (onlinePlayer != null) {
                    AttributeInstance finalAttackDamageAttribute = onlinePlayer.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
                    if (finalAttackDamageAttribute != null) {
                        finalAttackDamageAttribute.removeModifier(modifier);
                    }
                }
            }, durationTicks);
        }

        // --- Schedule particle display ---
        new BukkitRunnable() {
            private long ticksElapsed = 0;

            @Override
            public void run() {
                // Ensure player is still online and the duration hasn't passed
                if (!player.isOnline() || ticksElapsed >= durationTicks) {
                    this.cancel();
                    return;
                }

                // Spawn particles in a circle around the player's current location
                Location playerLoc = player.getLocation();
                for (int i = 0; i < particleCount; i++) {
                    double angle = (double) i / particleCount * Math.PI * 2; // Calculate angle for each particle
                    double x = particleRadius * Math.cos(angle);
                    double z = particleRadius * Math.sin(angle);

                    // Offset particles slightly above the player's feet for better visibility
                    Location particleLoc = playerLoc.clone().add(x, 0.5, z);
                    player.getWorld().spawnParticle(particleType, particleLoc, 1, 0, 0, 0, 0); // Amount 1, offset 0 for precise placement
                }

                ticksElapsed++;
            }
        }.runTaskTimer(plugin, 0L, 1L); // Run immediately, repeat every tick
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("particleType", particleType.name());
        data.put("particleCount", particleCount);
        data.put("particleRadius", particleRadius);
        data.put("durationTicks", durationTicks);
        data.put("damageIncrease", damageIncrease);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "particleType" -> this.particleType = Particle.valueOf(value.toUpperCase());
            case "particleCount" -> this.particleCount = Integer.parseInt(value);
            case "particleRadius" -> this.particleRadius = Double.parseDouble(value);
            case "durationTicks" -> this.durationTicks = Long.parseLong(value);
            case "damageIncrease" -> this.damageIncrease = Double.parseDouble(value);
            default -> super.applyEdit(field, value);
        }
    }

    public static AbstractAction fromConfig(DomainConfig config) {
        // Default values for configuration
        Particle particleType = Particle.valueOf(config.getString("particleType", "FLAME").toUpperCase());
        int particleCount = config.getInt("particleCount", 16); // 16 particles per tick to form the circle
        double particleRadius = config.getDouble("particleRadius", 1.5); // Radius of the particle circle
        long durationTicks = config.getLong("durationTicks", 100L); // 100 ticks = 5 seconds
        double damageIncrease = config.getDouble("damageIncrease", 2.0); // +2.0 damage

        EmpoweringAuraAction action = new EmpoweringAuraAction(particleType, particleCount, particleRadius, durationTicks, damageIncrease);
        action.setChance(config.getDouble("chance", 100.0));
        return action;
    }
}