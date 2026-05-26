package me.ogali.xenithlibrary.action.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.action.domain.ActionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import me.ogali.xenithlibrary.utilities.Chat;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerActionBarAction extends AbstractAction {
    private String message;

    public PlayerActionBarAction(String message) {
        this.message = message;
    }

    @Override
    public void execute(ActionContext context) {
        if (!rolledSuccessfully()) return;
        if (context.getPlayer() == null) return;
        String resolved = message;
        if (XenithLibrary.isPapiEnabled()) {
            resolved = PlaceholderAPI.setPlaceholders(context.getPlayer(), resolved);
        }
        context.getPlayer().spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                new TextComponent(Chat.colorize(resolved))
        );
    }

    @Override
    public java.util.Map<String, Object> serialize() {
        java.util.Map<String, Object> data = new java.util.LinkedHashMap<>(super.serialize());
        data.put("message", message);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "message" -> this.message = value;
            default -> super.applyEdit(field, value);
        }
    }

    public static AbstractAction fromConfig(DomainConfig config) {
        String message = config.getString("message", "");
        PlayerActionBarAction action = new PlayerActionBarAction(message);
        action.setChance(config.getDouble("chance", 100.0));
        return action;
    }
}