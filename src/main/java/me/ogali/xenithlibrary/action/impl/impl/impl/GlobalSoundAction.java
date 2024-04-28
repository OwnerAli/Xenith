package me.ogali.xenithlibrary.action.impl.impl.impl;

import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.files.impl.ActionsFile;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class GlobalSoundAction extends PlayerSoundAction {

    private float volume;
    private float pitch;

    public GlobalSoundAction(String id, String value, double chance) {
        super(id, value, chance);
    }

    public GlobalSoundAction(String id) {
        super(id, "", 100.0);
    }

    @Override
    public void execute(LivingEntity livingEntity) {
        if (!isSuccessful(getChance())) return;
        if (!(livingEntity instanceof Player player)) return;
        player.getWorld().playSound(player.getLocation(),
                Sound.valueOf(getValue().toUpperCase()),
                volume, pitch);
    }

    @Override
    public void saveExtraSettings() {
        ActionsFile actionsFile = XenithLibrary.getInstance().getActionsFile();
        actionsFile.set(getId() + ".extraSettings", volume + "," + pitch);
    }

    @Override
    public void loadExtraSettings(String[] settings) {
        if (settings.length < 2) return;
        volume = Float.parseFloat(settings[0]);
        pitch = Float.parseFloat(settings[1]);
    }

}
