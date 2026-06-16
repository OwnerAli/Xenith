package me.ogali.xenithlibrary.conditions.impl;

import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.ConditionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.Location;
import org.bukkit.block.Biome;

import java.util.LinkedHashMap;
import java.util.Map;

public class BiomeCondition extends AbstractCondition {

    private String biome;

    public BiomeCondition(String biome) {
        this.biome = biome;
    }

    @Override
    public boolean test(ConditionContext context) {
        Location location = context.getLocation();
        if (location == null) return false;
        if (location.getWorld() == null) return false;

        return evaluate(location.getWorld().getBiome(location).name(), Biome.valueOf(biome).name());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("biome", biome);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "biome" -> this.biome = value.toUpperCase();
            default -> super.applyEdit(field, value);
        }
    }

    public static AbstractCondition fromConfig(DomainConfig config) {
        return new BiomeCondition(config.getString("biome", "PLAINS"));
    }
}