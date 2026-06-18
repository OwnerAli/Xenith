package me.ogali.xenithlibrary.conditions.impl;

import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.ConditionContext;
import me.ogali.xenithlibrary.conditions.domain.HandSlot;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedHashMap;
import java.util.Map;

public class ToolNameCondition extends AbstractCondition {
    private String name;
    private HandSlot hand;

    public ToolNameCondition(String name) {
        this.name = name;
    }

    @Override
    public boolean test(ConditionContext context) {
        Player player = context.getPlayer();
        if (player == null) return false;

        ItemStack tool = hand.getItem(player);
        ItemMeta meta = tool.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return false;
        return evaluate(meta.getDisplayName(), name);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("name", name);
        data.put("hand", hand);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "name" -> this.name = value;
            case "hand" -> this.hand = HandSlot.valueOf(value.toUpperCase());
            default -> super.applyEdit(field, value);
        }
    }

    public static AbstractCondition fromConfig(DomainConfig config) {
        return new ToolNameCondition(config.getString("name", ""));
    }
}