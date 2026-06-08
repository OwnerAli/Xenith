package me.ogali.xenithlibrary.actions.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.actions.domain.AbstractAction;
import me.ogali.xenithlibrary.actions.domain.ActionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.Bukkit;

import java.util.LinkedHashMap;
import java.util.Map;

public class BroadcastAction extends AbstractAction {
    private String message;

    public BroadcastAction(String message) {
        this.message = message;
    }

    @Override
    public void execute(ActionContext context) {
        if (!rolledSuccessfully()) return;

        String resolved = message;
        if (XenithLibrary.isPapiEnabled() && context.getPlayer() != null) {
            resolved = PlaceholderAPI.setPlaceholders(context.getPlayer(), resolved);
        }

        Bukkit.broadcastMessage(Chat.colorize(resolved));
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "message" -> this.message = value;
            default -> super.applyEdit(field, value);
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("message", message);
        return data;
    }

    public static AbstractAction fromConfig(DomainConfig config) {
        String message = config.getString("message", "");
        BroadcastAction action = new BroadcastAction(message);
        action.setChance(config.getDouble("chance", 100.0));
        return action;
    }
}