package me.ogali.xenithlibrary.action.impl.impl.impl;

import me.ogali.xenithlibrary.action.impl.impl.StringValuePlayerAction;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class PlayerActionBarAction extends StringValuePlayerAction {

    public PlayerActionBarAction(String id, String value, double chance) {
        super(id, value, chance, false);
    }

    public PlayerActionBarAction(String id) {
        super(id, "", 100.0, false);
    }

    @Override
    public void execute(LivingEntity livingEntity) {
        if (!isSuccessful(getChance())) return;
        if (!(livingEntity instanceof Player player)) return;
        Chat.tellActionBar(player, getValue());
    }

}
