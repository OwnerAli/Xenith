package me.ogali.xenithlibrary.conditions.domain;

import lombok.Getter;
import org.bukkit.Material;

/**
 * Describes a registered condition type.
 * Plugins create instances of this and register them via ConditionRegistry.
 */
@Getter
public final class ConditionType {
    private final String key;
    private final ConditionBuilder builder;
    private final Material icon; // optional, for GUIs

    public ConditionType(String key, ConditionBuilder builder) {
        this(key, builder, Material.PAPER);
    }

    public ConditionType(String key, ConditionBuilder builder, Material icon) {
        this.key = key.toUpperCase();
        this.builder = builder;
        this.icon = icon;
    }
}