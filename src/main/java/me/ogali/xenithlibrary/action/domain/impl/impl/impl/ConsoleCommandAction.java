package me.ogali.xenithlibrary.action.domain.impl.impl.impl;

import me.ogali.xenithlibrary.action.domain.impl.impl.StringValuePlayerAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

public class ConsoleCommandAction extends StringValuePlayerAction {

    public ConsoleCommandAction(String id, String value) {
        super(id, value);
    }

    @Override
    public void execute(LivingEntity livingEntity) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), getValue().replace("%player%", livingEntity.getName()));
    }

}
