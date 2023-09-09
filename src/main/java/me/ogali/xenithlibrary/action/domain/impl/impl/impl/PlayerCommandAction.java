package me.ogali.xenithlibrary.action.domain.impl.impl.impl;

import me.ogali.xenithlibrary.action.domain.impl.impl.StringValuePlayerAction;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class PlayerCommandAction extends StringValuePlayerAction {

    private final String value;

    public PlayerCommandAction(String id, String value) {
        super(id, value);
        this.value = value;
    }

    @Override
    public void execute(LivingEntity livingEntity) {
        if (!(livingEntity instanceof Player player)) return;
        player.performCommand(value);
    }

}
