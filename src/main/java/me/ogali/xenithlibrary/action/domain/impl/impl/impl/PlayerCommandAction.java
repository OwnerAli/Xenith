package me.ogali.xenithlibrary.action.domain.impl.impl.impl;

import me.ogali.xenithlibrary.action.domain.impl.impl.StringValuePlayerAction;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class PlayerCommandAction extends StringValuePlayerAction {

    public PlayerCommandAction(String id, String value, double chance) {
        super(id, value, chance);
    }

    public PlayerCommandAction(String id) {
        super(id, "", 100.0);
    }

    @Override
    public void execute(LivingEntity livingEntity) {
        if (!isSuccessful(getChance())) return;
        if (!(livingEntity instanceof Player player)) return;
        player.performCommand(getValue());
    }

}
