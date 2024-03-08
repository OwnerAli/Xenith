package me.ogali.xenithlibrary.action.domain.impl.impl.impl;

import me.ogali.xenithlibrary.action.domain.impl.impl.StringValuePlayerAction;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.LivingEntity;

public class PlayerMessageAction extends StringValuePlayerAction {

    public PlayerMessageAction(String id, String value, double chance) {
        super(id, value, chance);
    }

    public PlayerMessageAction(String id) {
        super(id, "", 100.0);
    }

    @Override
    public void execute(LivingEntity livingEntity) {
        if (!isSuccessful(getChance())) return;
        Chat.tell(livingEntity, getValue());
    }

}
