package me.ogali.xenithlibrary.prompt.impl;

import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.prompt.AbstractChatPrompt;
import me.ogali.xenithlibrary.registiry.impl.ActionRegistry;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.Player;

public abstract class AbstractActionChatPrompt<T extends AbstractAction<?, ?>> extends AbstractChatPrompt<T> {

    public AbstractActionChatPrompt(T value) {
        super(value);
    }

    public void unPrompt(Player player) {
        super.unPrompt(player);
        XenithLibrary.getInstance().getRegistryManager()
                .getRegistry(ActionRegistry.class)
                .register(getType());
        Chat.tell(player, "&aAction successfully created! &7(" + getType().getId() + ")");
    }

}
