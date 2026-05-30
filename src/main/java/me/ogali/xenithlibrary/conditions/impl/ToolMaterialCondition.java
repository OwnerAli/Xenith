package me.ogali.xenithlibrary.conditions.impl;

import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.ConditionContext;
import me.ogali.xenithlibrary.conditions.domain.HandSlot;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.LinkedHashMap;
import java.util.Map;

public class ToolMaterialCondition extends AbstractCondition {
    private String material;
    private HandSlot hand;

    public ToolMaterialCondition(String material, HandSlot hand) {
        this.material = material;
        this.hand = hand;
    }

    @Override
    public boolean test(ConditionContext context) {
        if (!(context.getBukkitEvent() instanceof BlockBreakEvent event)) return false;

        Player player = event.getPlayer();
        Material held = hand.getItem(player).getType();

        return evaluate(held.name(), material.toUpperCase());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("material", material);
        data.put("hand", hand.name());
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "material" -> this.material = value.toUpperCase();
            case "hand" -> this.hand = HandSlot.valueOf(value.toUpperCase());
            default -> super.applyEdit(field, value);
        }
    }

    public static AbstractCondition fromConfig(DomainConfig config) {
        return new ToolMaterialCondition(
                config.getString("material", Material.AIR.name()),
                HandSlot.valueOf(
                        config.getString("hand", "MAIN_HAND").toUpperCase()
                )
        );
    }
}