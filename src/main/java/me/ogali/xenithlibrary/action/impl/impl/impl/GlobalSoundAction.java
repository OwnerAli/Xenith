package me.ogali.xenithlibrary.action.impl.impl.impl;

import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.files.impl.ActionsFile;
import me.ogali.xenithlibrary.settings.Setting;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class GlobalSoundAction extends PlayerSoundAction {

    public GlobalSoundAction(String id, String value, double chance) {
        super(id, value, chance);
        settingHolder.addSetting(new Setting("Volume", 1.0F));
        settingHolder.addSetting(new Setting("Pitch", 1.0F));
    }

    public GlobalSoundAction(String id) {
        super(id, "", 100.0);
        settingHolder.addSetting(new Setting("Volume", 1.0F));
        settingHolder.addSetting(new Setting("Pitch", 1.0F));
    }

    @Override
    public void execute(LivingEntity livingEntity) {
        if (!isSuccessful(getChance())) return;
        if (!(livingEntity instanceof Player player)) return;

        Setting volumeSetting = settingHolder.getSetting("Volume");
        Setting pitchSetting = settingHolder.getSetting("Pitch");

        player.getWorld().playSound(player.getLocation(),
                Sound.valueOf(getValue().toUpperCase()),
                (float) volumeSetting.getValue(),
                (float) pitchSetting.getValue());
    }

    @Override
    public void saveExtraSettings() {
        ActionsFile actionsFile = XenithLibrary.getInstance().getActionsFile();

        actionsFile.set(getId() + ".extraSettings", settingHolder);
    }

    @Override
    public void loadExtraSettings(String[] settings) {
        if (settings.length < 2) return;
        settingHolder.addSetting(new Setting("Volume", Float.parseFloat(settings[0])));
        settingHolder.addSetting(new Setting("Pitch", Float.parseFloat(settings[1])));
    }

}
