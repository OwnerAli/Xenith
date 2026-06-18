package me.ogali.xenithlibrary.actions.impl;

import me.ogali.xenithlibrary.actions.domain.AbstractAction;
import me.ogali.xenithlibrary.actions.domain.ActionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.Sound;

public class PlayerSoundAction extends AbstractAction {
    private Sound sound;
    private float volume;
    private float pitch;

    public PlayerSoundAction(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void execute(ActionContext context) {
        if (!rolledSuccessfully()) return;
        if (context.getPlayer() == null) return;
        context.getPlayer().playSound(context.getPlayer().getLocation(), sound, volume, pitch);
    }

    @Override
    public java.util.Map<String, Object> serialize() {
        java.util.Map<String, Object> data = new java.util.LinkedHashMap<>(super.serialize());
        data.put("sound", sound.name());
        data.put("volume", volume);
        data.put("pitch", pitch);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "sound" -> this.sound = Sound.valueOf(value.toUpperCase());
            case "volume" -> this.volume = Float.parseFloat(value);
            case "pitch" -> this.pitch = Float.parseFloat(value);
            default -> super.applyEdit(field, value);
        }
    }

    public static AbstractAction fromConfig(DomainConfig config) {
        String soundStr = config.getUppercaseString("sound");
        Sound sound = (soundStr != null && !soundStr.isBlank())
                ? Sound.valueOf(soundStr)
                : Sound.UI_BUTTON_CLICK; // sensible default
        float volume = (float) config.getDouble("volume", 1.0);
        float pitch = (float) config.getDouble("pitch", 1.0);
        PlayerSoundAction action = new PlayerSoundAction(sound, volume, pitch);
        action.setChance(config.getDouble("chance", 100.0));
        return action;
    }
}