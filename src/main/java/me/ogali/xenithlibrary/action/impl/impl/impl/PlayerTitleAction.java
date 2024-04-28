package me.ogali.xenithlibrary.action.impl.impl.impl;

import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.action.impl.impl.StringValuePlayerAction;
import me.ogali.xenithlibrary.files.impl.ActionsFile;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class PlayerTitleAction extends StringValuePlayerAction {

    private String subTitle;
    private int fadeIn;
    private int stay;
    private int fadeOut;

    public PlayerTitleAction(String id, String value, double chance) {
        super(id, value, chance, true);
    }

    public PlayerTitleAction(String id) {
        super(id, "", 100.0, true);
    }

    @Override
    public void execute(LivingEntity livingEntity) {
        if (!isSuccessful(getChance())) return;
        if (!(livingEntity instanceof Player player)) return;
        player.sendTitle(getValue(), subTitle, fadeIn, stay, fadeOut);
    }

    @Override
    public void saveExtraSettings() {
        ActionsFile actionsFile = XenithLibrary.getInstance().getActionsFile();
        actionsFile.set(getId() + ".extraSettings", subTitle + "," + fadeIn + "," + stay + "," + fadeOut);
    }

    @Override
    public void loadExtraSettings(String[] settings) {
        if (settings.length < 4) return;
        subTitle = settings[0];
        fadeIn = Integer.parseInt(settings[1]);
        stay = Integer.parseInt(settings[2]);
        fadeOut = Integer.parseInt(settings[3]);
    }

}
