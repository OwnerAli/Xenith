package me.ogali.xenithlibrary.actions.domain;

import org.bukkit.Material;

public record ActionType(String key, ActionBuilder builder, Material icon) {

    public ActionType(String key, ActionBuilder builder, Material icon) {
        this.key = key.toUpperCase();
        this.builder = builder;
        this.icon = icon;
    }
}