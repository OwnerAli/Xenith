package me.ogali.xenithlibrary.action.domain;

import lombok.Getter;
import org.bukkit.Material;

@Getter
public final class ActionType {
    private final String key;
    private final ActionBuilder builder;
    private final Material icon;

    public ActionType(String key, ActionBuilder builder) {
        this(key, builder, Material.PAPER);
    }

    public ActionType(String key, ActionBuilder builder, Material icon) {
        this.key = key.toUpperCase();
        this.builder = builder;
        this.icon = icon;
    }
}