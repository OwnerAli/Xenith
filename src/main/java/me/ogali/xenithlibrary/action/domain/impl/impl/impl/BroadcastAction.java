package me.ogali.xenithlibrary.action.domain.impl.impl.impl;

import me.ogali.xenithlibrary.action.domain.impl.impl.StringValuePlayerAction;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

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
        Bukkit.broadcastMessage(Chat.colorize(getValue()));
    }

}
