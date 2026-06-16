package me.ogali.xenithlibrary.conditions.impl;

import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.ConditionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.LinkedHashMap;
import java.util.Map;

public class BlockTypeCondition extends AbstractCondition {

    private String material;

    public BlockTypeCondition(String material) {
        this.material = material;
    }

    @Override
    public boolean test(ConditionContext context) {
        Block block = context.getBlock();
        if (block == null) return false;
        return evaluate(block.getType().name(), material.toUpperCase());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("material", material);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "material" -> this.material = value.toUpperCase();
            default -> super.applyEdit(field, value);
        }
    }

    public static AbstractCondition fromConfig(DomainConfig config) {
        return new BlockTypeCondition(config.getString("material", Material.STONE.name()));
    }
}