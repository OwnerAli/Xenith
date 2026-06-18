package me.ogali.xenithlibrary.conditions.impl;

import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.ConditionContext;
import me.ogali.xenithlibrary.conditions.domain.HandSlot;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ToolLoreCondition extends AbstractCondition {
    private String lore;
    private HandSlot hand;

    public ToolLoreCondition(String lore) {
        this.lore = lore;
    }

    @Override
    public boolean test(ConditionContext context) {
        Player player = context.getPlayer();
        if (player == null) return false;

        ItemStack tool = hand.getItem(player);
        ItemMeta meta = tool.getItemMeta();
        if (meta == null || !meta.hasLore()) return false;
        List<String> loreLines = meta.getLore();
        boolean contains = loreLines != null && loreLines.stream()
                .anyMatch(line -> line.equalsIgnoreCase(lore));
        return evaluate(String.valueOf(contains), "true");
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("lore", lore);
        data.put("hand", hand);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "lore" -> this.lore = value;
            case "hand" -> this.hand = HandSlot.valueOf(value.toUpperCase());
            default -> super.applyEdit(field, value);
        }
    }

    public static AbstractCondition fromConfig(DomainConfig config) {
        return new ToolLoreCondition(config.getString("lore", ""));
    }
}