package me.ogali.xenithlibrary.action.impl.impl.impl;

import me.ogali.xenithlibrary.action.impl.impl.StringValuePlayerAction;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.LivingEntity;

public class PlayerMessageAction extends StringValuePlayerAction {

    public PlayerMessageAction(String id, String value, double chance) {
        super(id, value, chance, false);
    }

    public PlayerMessageAction(String id) {
        super(id, "", 100.0, false);
    }

    @Override
    public void execute(LivingEntity livingEntity) {
        if (!isSuccessful(getChance())) return;
        Chat.tell(livingEntity, getValue());
    }

}
