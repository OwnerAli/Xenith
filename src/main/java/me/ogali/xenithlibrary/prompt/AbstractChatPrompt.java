package me.ogali.xenithlibrary.prompt;

import lombok.Getter;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.manager.RegistryManager;
import me.ogali.xenithlibrary.registiry.impl.ChatPromptRegistry;
import me.ogali.xenithlibrary.registiry.impl.ConditionRegistry;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.Player;

@Getter
public abstract class AbstractChatPrompt<T extends AbstractCondition<?, ?>> implements ChatPrompt {

    private final T abstractCondition;

    public AbstractChatPrompt(T abstractCondition) {
        this.abstractCondition = abstractCondition;
    }

    public void prompt(Player player) {
        Chat.tell(player, "&aPlease input the desired value. To cancel, type '!cancel'.");
        XenithLibrary.getInstance().getRegistryManager()
                .getRegistry(ChatPromptRegistry.class)
                .register(player, this);
        player.closeInventory();
    }

    public void unPrompt(Player player) {
        RegistryManager registryManager = XenithLibrary.getInstance().getRegistryManager();
        registryManager.getRegistry(ChatPromptRegistry.class)
                .unRegister(player);
        registryManager.getRegistry(ConditionRegistry.class)
                .register(abstractCondition);
        Chat.tell(player, "&aCondition successfully created! &7(" + abstractCondition.getId() + ")");
    }

}
