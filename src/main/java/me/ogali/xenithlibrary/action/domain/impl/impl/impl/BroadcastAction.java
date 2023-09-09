package me.ogali.xenithlibrary.action.domain.impl.impl.impl;

import me.ogali.xenithlibrary.action.domain.impl.impl.StringValuePlayerAction;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

public class BroadcastAction extends StringValuePlayerAction {

    public BroadcastAction(String id, String value) {
        super(id, value);
    }

    @Override
    public void execute(LivingEntity livingEntity) {
        Bukkit.broadcastMessage(Chat.colorize(getValue()));
    }

}
