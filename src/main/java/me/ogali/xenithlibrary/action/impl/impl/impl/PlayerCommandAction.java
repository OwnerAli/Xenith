package me.ogali.xenithlibrary.action.impl.impl.impl;

import me.ogali.xenithlibrary.action.impl.impl.StringValuePlayerAction;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class PlayerCommandAction extends StringValuePlayerAction {

    public PlayerCommandAction(String id, String value, double chance) {
        super(id, value, chance, false);
    }

    public PlayerCommandAction(String id) {
        super(id, "", 100.0, false);
    }

    @Override
    public void execute(LivingEntity livingEntity) {
        if (!isSuccessful(getChance())) return;
        if (!(livingEntity instanceof Player player)) return;
        player.performCommand(getValue());
    }

}
