package me.ogali.xenithlibrary.action.domain.impl.impl.impl;

import me.ogali.xenithlibrary.action.domain.impl.impl.StringValuePlayerAction;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.LivingEntity;

public class PlayerMessageAction extends StringValuePlayerAction {

    public PlayerMessageAction(String id, String value) {
        super(id, value);
    }

    @Override
    public void execute(LivingEntity livingEntity) {
        Chat.tell(livingEntity, getValue());
    }

}
