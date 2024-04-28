package me.ogali.xenithlibrary.prompt.listeners;

import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.registiry.impl.ChatPromptRegistry;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    private final ChatPromptRegistry chatPromptRegistry;

    public PlayerChatListener(ChatPromptRegistry chatPromptRegistry) {
        this.chatPromptRegistry = chatPromptRegistry;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        chatPromptRegistry.get(event.getPlayer())
                .ifPresent(chatPrompt -> {
                    event.setCancelled(true);

                    Player player = event.getPlayer();

                    if (event.getMessage().equalsIgnoreCase("!cancel")) {
                        Chat.tell(player, "&cPrompt cancelled.");
                        XenithLibrary.getInstance().getRegistryManager()
                                .getRegistry(ChatPromptRegistry.class)
                                .unRegister(player);
                        return;
                    }
                    if (!chatPrompt.setValue(event.getMessage())) return;
                    chatPrompt.unPrompt(player);
                });
    }

}
