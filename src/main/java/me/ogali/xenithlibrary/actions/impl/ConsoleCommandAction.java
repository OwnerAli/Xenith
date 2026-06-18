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

public class ConsoleCommandAction extends AbstractAction {
    private String command;

    public ConsoleCommandAction(String command) {
        this.command = command;
    }

    @Override
    public void execute(ActionContext context) {
        if (!rolledSuccessfully()) return;
        String resolved = command;
        if (XenithLibrary.isPapiEnabled() && context.getPlayer() != null) {
            resolved = PlaceholderAPI.setPlaceholders(context.getPlayer(), resolved);
        }
        final String command = Chat.strip(resolved);
        Bukkit.getScheduler().runTask(XenithLibrary.getInstance(),
                () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("command", command);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "command" -> this.command = value;
            default -> super.applyEdit(field, value);
        }
    }

    public static AbstractAction fromConfig(DomainConfig config) {
        String command = config.getString("command", "");
        ConsoleCommandAction action = new ConsoleCommandAction(command);
        action.setChance(config.getDouble("chance", 100.0));
        return action;
    }
}