package me.ogali.xenithlibrary.conditions.impl;

import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.ConditionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.LinkedHashMap;
import java.util.Map;

public class BlockBiomeCondition extends AbstractCondition {

    private String biome;

    public BlockBiomeCondition(String biome) {
        this.biome = biome;
    }

    @Override
    public boolean test(ConditionContext context) {
        if (!(context.getBukkitEvent() instanceof BlockBreakEvent event)) return false;
        return evaluate(event.getBlock().getBiome().name(), biome.toUpperCase());
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
            default      -> super.applyEdit(field, value);
        }
    }

    public static AbstractCondition fromConfig(DomainConfig config) {
        return new BlockBiomeCondition(config.getString("biome", "PLAINS"));
    }
}