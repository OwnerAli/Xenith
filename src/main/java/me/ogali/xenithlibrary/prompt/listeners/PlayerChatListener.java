package me.ogali.xenithlibrary.prompt.listeners;

import me.ogali.xenithlibrary.registiry.impl.ChatPromptRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class PlayerChatListener implements Listener {

    private final ChatPromptRegistry chatPromptRegistry;

    public PlayerChatListener(ChatPromptRegistry chatPromptRegistry) {
        this.chatPromptRegistry = chatPromptRegistry;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(PlayerChatEvent event) {
        chatPromptRegistry.get(event.getPlayer())
                .ifPresent(chatPrompt -> {
                    event.setCancelled(true);
                    if (event.getMessage().equalsIgnoreCase("!cancel")) {
                        chatPrompt.unPrompt(event.getPlayer());
                        return;
                    }
                    if (!chatPrompt.setValue(event.getMessage())) return;
                    chatPrompt.unPrompt(event.getPlayer());
                });
    }

}
