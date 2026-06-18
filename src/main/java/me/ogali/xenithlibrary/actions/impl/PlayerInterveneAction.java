package me.ogali.xenithlibrary.actions.impl;

import lombok.Getter;
import lombok.Setter;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.actions.domain.AbstractAction;
import me.ogali.xenithlibrary.actions.domain.ActionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class PlayerInterveneAction extends AbstractAction {

    private String soundName;
    private float soundVolume;
    private float soundPitch;
    private String particleName;
    private int particleCount;
    private int glowingDurationTicks; // 0 for no glowing

    public PlayerInterveneAction(String soundName, float soundVolume, float soundPitch,
                                 String particleName, int particleCount, int glowingDurationTicks) {
        this.soundName = soundName;
        this.soundVolume = soundVolume;
        this.soundPitch = soundPitch;
        this.particleName = particleName;
        this.particleCount = particleCount;
        this.glowingDurationTicks = glowingDurationTicks;
    }

    @Override
    public void execute(ActionContext context) {
        if (!rolledSuccessfully()) return;
        Player player = context.getPlayer();
        if (player == null || !player.isOnline()) return;

        Location loc = player.getLocation();

        // Play sound
        try {
            Sound sound = Sound.valueOf(soundName.toUpperCase());
            player.playSound(loc, sound, soundVolume, soundPitch);
        } catch (IllegalArgumentException e) {
            XenithLibrary.getInstance().getLogger().warning("Invalid sound '" + soundName + "' for PlayerInterveneAction (ID: " + getId() + ")");
        }

        // Spawn particles
        try {
            Particle particle = Particle.valueOf(particleName.toUpperCase());
            // Particles are often best spawned slightly above the player's feet
            player.spawnParticle(particle, loc.add(0, 1.2, 0), particleCount, 0.3, 0.3, 0.3, 0.01);
        } catch (IllegalArgumentException e) {
            XenithLibrary.getInstance().getLogger().warning("Invalid particle '" + particleName + "' for PlayerInterveneAction (ID: " + getId() + ")");
        }

        // Apply glowing effect
        if (glowingDurationTicks > 0) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, glowingDurationTicks, 0, false, false, false));
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("soundName", soundName);
        data.put("soundVolume", soundVolume);
        data.put("soundPitch", soundPitch);
        data.put("particleName", particleName);
        data.put("particleCount", particleCount);
        data.put("glowingDurationTicks", glowingDurationTicks);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "soundName" -> this.soundName = value;
            case "soundVolume" -> this.soundVolume = Float.parseFloat(value);
            case "soundPitch" -> this.soundPitch = Float.parseFloat(value);
            case "particleName" -> this.particleName = value;
            case "particleCount" -> this.particleCount = Integer.parseInt(value);
            case "glowingDurationTicks" -> this.glowingDurationTicks = Integer.parseInt(value);
            default -> super.applyEdit(field, value);
        }
    }

    public static AbstractAction fromConfig(DomainConfig config) {
        String soundName = config.getString("soundName", Sound.ENTITY_GENERIC_EXTINGUISH_FIRE.name());
        float soundVolume = (float) config.getDouble("soundVolume", 0.4);
        float soundPitch = (float) config.getDouble("soundPitch", 1.2);
        String particleName = config.getString("particleName", Particle.ENCHANTMENT_TABLE.name());
        int particleCount = config.getInt("particleCount", 15);
        int glowingDurationTicks = config.getInt("glowingDurationTicks", 20); // 1 second glowing

        PlayerInterveneAction action = new PlayerInterveneAction(
                soundName, soundVolume, soundPitch, particleName, particleCount, glowingDurationTicks
        );
        action.setChance(config.getDouble("chance", 100.0));
        return action;
    }
}