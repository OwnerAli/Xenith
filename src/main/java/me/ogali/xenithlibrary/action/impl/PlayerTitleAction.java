package me.ogali.xenithlibrary.action.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.action.domain.ActionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import me.ogali.xenithlibrary.utilities.Chat;

public class PlayerTitleAction extends AbstractAction {
    private String title;
    private String subtitle;
    private int fadeIn;
    private int stay;
    private int fadeOut;

    public PlayerTitleAction(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    @Override
    public void execute(ActionContext context) {
        if (!rolledSuccessfully()) return;
        if (context.getPlayer() == null) return;
        String resolvedTitle = title;
        String resolvedSubtitle = subtitle;
        if (XenithLibrary.isPapiEnabled()) {
            resolvedTitle = PlaceholderAPI.setPlaceholders(context.getPlayer(), resolvedTitle);
            resolvedSubtitle = PlaceholderAPI.setPlaceholders(context.getPlayer(), resolvedSubtitle);
        }
        context.getPlayer().sendTitle(
                Chat.colorize(resolvedTitle),
                Chat.colorize(resolvedSubtitle),
                fadeIn, stay, fadeOut
        );
    }

    // PlayerTitleAction.java
    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "title" -> this.title = value;
            case "subtitle" -> this.subtitle = value;
            case "fade-in" -> this.fadeIn = Integer.parseInt(value);
            case "stay" -> this.stay = Integer.parseInt(value);
            case "fade-out" -> this.fadeOut = Integer.parseInt(value);
            default -> super.applyEdit(field, value);
        }
    }

    @Override
    public java.util.Map<String, Object> serialize() {
        java.util.Map<String, Object> data = new java.util.LinkedHashMap<>(super.serialize());
        data.put("title", title);
        data.put("subtitle", subtitle);
        data.put("fade-in", fadeIn);
        data.put("stay", stay);
        data.put("fade-out", fadeOut);
        return data;
    }

    public static AbstractAction fromConfig(DomainConfig config) {
        String title = config.getString("title", "");
        String subtitle = config.getString("subtitle", "");
        int fadeIn = config.getInt("fade-in", 10);
        int stay = config.getInt("stay", 70);
        int fadeOut = config.getInt("fade-out", 20);
        PlayerTitleAction action = new PlayerTitleAction(title, subtitle, fadeIn, stay, fadeOut);
        action.setChance(config.getDouble("chance", 100.0));
        return action;
    }
}