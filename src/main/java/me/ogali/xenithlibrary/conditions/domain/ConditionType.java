package me.ogali.xenithlibrary.conditions.domain;

import lombok.Getter;
import me.ogali.xenithlibrary.conditions.impl.block_age_condition.BlockAgeCondition;
import me.ogali.xenithlibrary.conditions.impl.entity_killer_condition.EntityKillerCondition;
import me.ogali.xenithlibrary.conditions.impl.placeholder_condition.PlaceholderCondition;
import me.ogali.xenithlibrary.shared.ConfigBuilder;
import org.bukkit.Material;

@Getter
public enum ConditionType {
    PLACEHOLDER(PlaceholderCondition.builder(), PlaceholderCondition.class, Material.ITEM_FRAME),
    ENTITY_KILLER(EntityKillerCondition.builder(), EntityKillerCondition.class, Material.IRON_SWORD),
    BLOCK_AGE(BlockAgeCondition.builder(), BlockAgeCondition.class, Material.CLOCK);

    private final ConfigBuilder<AbstractCondition> builder;
    private final Class<?> representingClass;
    private final Material creationMaterial;

    ConditionType(ConfigBuilder<AbstractCondition> builder, Class<?> representedClass, Material creationMaterial) {
        this.builder = builder;
        this.representingClass = representedClass;
        this.creationMaterial = creationMaterial;
    }
}
