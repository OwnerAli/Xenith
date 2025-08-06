package me.ogali.xenithlibrary.action.impl.impl.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.action.impl.impl.StringValuePlayerAction;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class BroadcastAction extends StringValuePlayerAction {

    public BroadcastAction(String id, String value, double chance) {
        super(id, value, chance);
    }

    public BroadcastAction(String id) {
        super(id, "", 100.0);
    }

    @Override
    public void execute(LivingEntity livingEntity) {
        if (!isSuccessful(getChance())) return;
        String value = getValue();
        if (XenithLibrary.PAPI && (livingEntity instanceof Player player)) {
            value = PlaceholderAPI.setPlaceholders(player, value);
        }
        Bukkit.broadcastMessage(Chat.colorize(value));
    }

}