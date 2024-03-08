package me.ogali.xenithlibrary.prompt;

import lombok.Getter;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.registiry.impl.ChatPromptRegistry;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.Player;

@Getter
public abstract class AbstractChatPrompt<T> implements ChatPrompt {

    private final T type;

    public AbstractChatPrompt(T type) {
        this.type = type;
    }

    @Override
    public void prompt(Player player) {
        Chat.tell(player, "&aPlease input the desired value. To cancel, type '!cancel'.");
        XenithLibrary.getInstance().getRegistryManager()
                .getRegistry(ChatPromptRegistry.class)
                .register(player, this);
        player.closeInventory();
    }

    public void unPrompt(Player player) {
        XenithLibrary.getInstance().getRegistryManager()
                .getRegistry(ChatPromptRegistry.class)
                .unRegister(player);
        Chat.tell(player, "&cPrompt cancelled.");
    }

}
