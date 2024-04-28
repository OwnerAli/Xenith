package me.ogali.xenithlibrary.action.impl.impl.impl;

import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.action.impl.impl.StringValuePlayerAction;
import me.ogali.xenithlibrary.files.impl.ActionsFile;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class PlayerSoundAction extends StringValuePlayerAction {

    private float volume;
    private float pitch;

    public PlayerSoundAction(String id, String value, double chance) {
        super(id, value, chance, true);
    }

    public PlayerSoundAction(String id) {
        super(id, "", 100.0, true);
    }

    @Override
    public void execute(LivingEntity livingEntity) {
        if (!isSuccessful(getChance())) return;
        if (!(livingEntity instanceof Player player)) return;
        player.playSound(player.getLocation(),
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
