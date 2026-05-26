package me.ogali.xenithlibrary.action.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.action.domain.ActionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import me.ogali.xenithlibrary.utilities.Chat;

public class PlayerMessageAction extends AbstractAction {
    private String message;

    public PlayerMessageAction(String message) {
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
        context.getPlayer().sendMessage(Chat.colorize(resolved));
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
        PlayerMessageAction action = new PlayerMessageAction(message);
        action.setChance(config.getDouble("chance", 100.0));
        return action;
    }
}