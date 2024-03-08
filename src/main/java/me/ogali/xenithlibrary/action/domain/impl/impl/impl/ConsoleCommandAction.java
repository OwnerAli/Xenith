package me.ogali.xenithlibrary.action.domain.impl.impl.impl;

import me.ogali.xenithlibrary.action.domain.impl.impl.StringValuePlayerAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

public class ConsoleCommandAction extends StringValuePlayerAction {

    public ConsoleCommandAction(String id, String value, double chance) {
        super(id, value, chance);
    }

    public ConsoleCommandAction(String id) {
        super(id, "", 100.0);
    }

    @Override
    public void execute(LivingEntity livingEntity) {
        if (!isSuccessful(getChance())) return;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), getValue().replace("%player%", livingEntity.getName()));
    }

}
