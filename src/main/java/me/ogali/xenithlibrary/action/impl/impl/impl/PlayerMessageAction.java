package me.ogali.xenithlibrary.action.impl.impl.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.action.impl.impl.StringValuePlayerAction;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class PlayerMessageAction extends StringValuePlayerAction {

    public PlayerMessageAction(String id, String value, double chance) {
        super(id, value, chance);
    }

    public PlayerMessageAction(String id) {
        super(id, "", 100.0);
    }

    @Override
    public void execute(LivingEntity livingEntity) {
        if (!isSuccessful(getChance())) return;
        String value = getValue();
        if (XenithLibrary.PAPI && (livingEntity instanceof Player player)) {
            value = PlaceholderAPI.setPlaceholders(player, value);
        }
        Chat.tell(livingEntity, value);
    }

}
