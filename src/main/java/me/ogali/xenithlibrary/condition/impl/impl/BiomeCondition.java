package me.ogali.xenithlibrary.condition.impl.impl;

import me.ogali.xenithlibrary.condition.impl.LocationCondition;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.Location;
import org.bukkit.block.Biome;

public class BiomeCondition extends LocationCondition<String> {

    public BiomeCondition(String id, int priority, boolean negate, String value) {
        super(id, priority, negate, value);
    }

    public BiomeCondition(String id, int priority, boolean negate) {
        super(id, priority, negate);
    }

    @Override
    public boolean evaluate(Location input) {
        return input.getBlock().getBiome().equals(Biome.valueOf(getValue().toUpperCase())) != isNegate();
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