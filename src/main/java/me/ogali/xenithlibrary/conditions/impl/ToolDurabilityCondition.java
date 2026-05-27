package me.ogali.xenithlibrary.conditions.impl;

import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.ConditionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.LinkedHashMap;
import java.util.Map;

public class ToolDurabilityCondition extends AbstractCondition {

    private int durability;

    public ToolDurabilityCondition(int durability) {
        this.durability = durability;
    }

    @Override
    public boolean test(ConditionContext context) {
        if (!(context.getBukkitEvent() instanceof BlockBreakEvent event)) return false;
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        if (!(tool.getItemMeta() instanceof Damageable damageable)) return false;
        int remaining = tool.getType().getMaxDurability() - damageable.getDamage();
        return evaluate(String.valueOf(remaining), String.valueOf(durability));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("durability", durability);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "durability" -> this.durability = Integer.parseInt(value);
            default           -> super.applyEdit(field, value);
        }
    }

    public static AbstractCondition fromConfig(DomainConfig config) {
        return new ToolDurabilityCondition(config.getInt("durability", 1));
    }
}