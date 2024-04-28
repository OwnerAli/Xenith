package me.ogali.xenithlibrary.action.impl.impl.impl;

import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.action.impl.impl.StringValuePlayerAction;
import me.ogali.xenithlibrary.files.impl.ActionsFile;
import me.ogali.xenithlibrary.settings.Setting;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class PlayerTitleAction extends StringValuePlayerAction {

    public PlayerTitleAction(String id, String value, double chance) {
        super(id, value, chance);
        settingHolder.addSetting(new Setting("SubTitle", "SubTitle of the title"));
        settingHolder.addSetting(new Setting("FadeIn", 0));
        settingHolder.addSetting(new Setting("Stay", 0));
        settingHolder.addSetting(new Setting("FadeOut", 0));
    }

    public PlayerTitleAction(String id) {
        super(id, "", 100.0);
        settingHolder.addSetting(new Setting("SubTitle", "SubTitle of the title"));
        settingHolder.addSetting(new Setting("FadeIn", 0));
        settingHolder.addSetting(new Setting("Stay", 0));
        settingHolder.addSetting(new Setting("FadeOut", 0));
    }

    @Override
    public void execute(LivingEntity livingEntity) {
        if (!isSuccessful(getChance())) return;
        if (!(livingEntity instanceof Player player)) return;

        String subTitle = (String) settingHolder.getSetting("SubTitle").getValue();
        int fadeIn = (int) settingHolder.getSetting("FadeIn").getValue();
        int stay = (int) settingHolder.getSetting("Stay").getValue();
        int fadeOut = (int) settingHolder.getSetting("FadeOut").getValue();

        player.sendTitle(getValue(), subTitle, fadeIn, stay, fadeOut);
    }

    @Override
    public void saveExtraSettings() {
        ActionsFile actionsFile = XenithLibrary.getInstance().getActionsFile();
        actionsFile.set(getId() + ".extraSettings", settingHolder);
    }

    @Override
    public void loadExtraSettings(String[] settings) {
        if (settings.length < 4) return;
        settingHolder.addSetting(new Setting("SubTitle", settings[0]));
        settingHolder.addSetting(new Setting("FadeIn", Integer.parseInt(settings[1])));
        settingHolder.addSetting(new Setting("Stay", Integer.parseInt(settings[2])));
        settingHolder.addSetting(new Setting("FadeOut", Integer.parseInt(settings[3])));
    }

}
