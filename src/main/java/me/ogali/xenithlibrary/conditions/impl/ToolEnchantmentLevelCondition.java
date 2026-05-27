package me.ogali.xenithlibrary.conditions.impl;

import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.ConditionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;

public class ToolEnchantmentLevelCondition extends AbstractCondition {

    private String enchantment;
    private int level;

    public ToolEnchantmentLevelCondition(String enchantment, int level) {
        this.enchantment = enchantment;
        this.level = level;
    }

    @Override
    public boolean test(ConditionContext context) {
        if (!(context.getBukkitEvent() instanceof BlockBreakEvent event)) return false;
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        int actualLevel = tool.getEnchantments().entrySet().stream()
                .filter(e -> e.getKey().getKey().getKey().equalsIgnoreCase(enchantment))
                .mapToInt(Map.Entry::getValue)
                .findFirst()
                .orElse(0);
        return evaluate(String.valueOf(actualLevel), String.valueOf(level));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("enchantment", enchantment);
        data.put("level", level);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "enchantment" -> this.enchantment = value.toLowerCase();
            case "level"       -> this.level       = Integer.parseInt(value);
            default            -> super.applyEdit(field, value);
        }
    }

    public static AbstractCondition fromConfig(DomainConfig config) {
        return new ToolEnchantmentLevelCondition(
                config.getString("enchantment", "fortune"),
                config.getInt("level", 1)
        );
    }
}