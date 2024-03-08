package me.ogali.xenithlibrary.prompt.impl;

import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.menus.conditions.ConditionSettingsMenu;
import me.ogali.xenithlibrary.prompt.AbstractChatPrompt;
import me.ogali.xenithlibrary.registiry.impl.ConditionRegistry;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.Player;

public abstract class AbstractConditionChatPrompt<T extends AbstractCondition<?, ?>> extends AbstractChatPrompt<T> {

    public AbstractConditionChatPrompt(T abstractCondition) {
        super(abstractCondition);
    }

    public void unPrompt(Player player) {
        super.unPrompt(player);
        XenithLibrary.getInstance().getRegistryManager()
                .getRegistry(ConditionRegistry.class)
                .register(getType());
        Chat.tell(player, "&aCondition successfully created! &7(" + getType().getId() + ")");
        new ConditionSettingsMenu().show(player, getType());
    }

}
