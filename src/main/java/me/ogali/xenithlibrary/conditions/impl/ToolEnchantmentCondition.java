package me.ogali.xenithlibrary.conditions.impl;

import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.ConditionContext;
import me.ogali.xenithlibrary.conditions.domain.HandSlot;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;

public class ToolEnchantmentCondition extends AbstractCondition {

    private String enchantment;
    private HandSlot hand;

    public ToolEnchantmentCondition(String enchantment) {
        this.enchantment = enchantment;
    }

    @Override
    public boolean test(ConditionContext context) {
        Player player = context.getPlayer();
        if (player == null) return false;

        ItemStack tool = hand.getItem(player);
        if (!tool.hasItemMeta()) return false;

        boolean hasEnchant = tool.getEnchantments().keySet().stream()
                .anyMatch(e -> e.getKeyOrThrow().getKey().equalsIgnoreCase(enchantment));
        return evaluate(String.valueOf(hasEnchant), "true");
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("enchantment", enchantment);
        data.put("hand", hand);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "enchantment" -> this.enchantment = value.toLowerCase();
            case "hand" -> this.hand = HandSlot.valueOf(value.toUpperCase());
            default -> super.applyEdit(field, value);
        }
    }

    public static AbstractCondition fromConfig(DomainConfig config) {
        return new ToolEnchantmentCondition(config.getString("enchantment", "fortune"));
    }
}