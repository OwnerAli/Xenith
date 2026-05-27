package me.ogali.xenithlibrary.conditions.impl;

import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.ConditionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;

public class ToolEnchantmentCondition extends AbstractCondition {

    private String enchantment;

    public ToolEnchantmentCondition(String enchantment) {
        this.enchantment = enchantment;
    }

    @Override
    public boolean test(ConditionContext context) {
        if (!(context.getBukkitEvent() instanceof BlockBreakEvent event)) return false;
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        if (!tool.hasItemMeta()) return false;
        boolean hasEnchant = tool.getEnchantments().keySet().stream()
                .anyMatch(e -> e.getKey().getKey().equalsIgnoreCase(enchantment));
        return evaluate(String.valueOf(hasEnchant), "true");
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("enchantment", enchantment);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "enchantment" -> this.enchantment = value.toLowerCase();
            default            -> super.applyEdit(field, value);
        }
    }

    public static AbstractCondition fromConfig(DomainConfig config) {
        return new ToolEnchantmentCondition(config.getString("enchantment", "fortune"));
    }
}