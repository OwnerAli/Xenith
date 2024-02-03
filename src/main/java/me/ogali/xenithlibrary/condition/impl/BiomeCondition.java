package me.ogali.xenithlibrary.condition.impl;

import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.LivingEntity;

public class BiomeCondition extends StringCondition {

    public BiomeCondition(String id, int priority, boolean negate) {
        super(id, priority, negate);
    }

    public BiomeCondition(String id, int priority, boolean negate, String value) {
        super(id, priority, negate, value);
    }

    @Override
    public boolean evaluate(String input, LivingEntity livingEntity) {
        return livingEntity.getLocation().getBlock().getBiome().toString().equalsIgnoreCase(getValue()) != isNegate();
    }

    @Override
    public String getType() {
        return "biome";
    }

    @Override
    public String getDisplayText() {
        return Chat.colorize("&fEntities biome matches");
    }

}
