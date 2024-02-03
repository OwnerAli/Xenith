package me.ogali.xenithlibrary.files.impl;

import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.condition.impl.BiomeCondition;
import me.ogali.xenithlibrary.condition.impl.ItemStackCondition;
import me.ogali.xenithlibrary.condition.impl.impl.*;
import me.ogali.xenithlibrary.registiry.impl.ConditionRegistry;
import me.ogali.xenithlibrary.utilities.Serialization;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ConditionsFile extends XenithJsonFile<AbstractCondition<?, ?>> {

    public ConditionsFile() {
        super("conditions");
    }

    public void save(AbstractCondition<?, ?> condition) {
        String id = condition.getId();

        set(id + ".type", condition.getType());
        set(id + ".priority", condition.getPriority());
        set(id + ".negate", condition.isNegate());
        if (condition instanceof ItemStackCondition<?> itemStackCondition) {
            if (itemStackCondition.getValue() instanceof ItemStack itemStack) {
                set(id + ".value", Serialization.serialize(itemStack));
                return;
            }
        }
        set(id + ".value", condition.getValue());
    }

    @Override
    public void load() {
        ConditionRegistry conditionRegistry = XenithLibrary.getInstance().getRegistryManager()
                .getRegistry(ConditionRegistry.class);
        singleLayerKeySet().forEach(key -> {
            AbstractCondition<?, ?> condition = getCondition(key);
            conditionRegistry.register(condition);
        });
    }

    public void delete(String conditionId) {
        set(conditionId, null);
    }

    public AbstractCondition<?, ?> getCondition(String key) {
        String type = getString(key + ".type");
        int priority = getInt(key + ".priority");
        boolean negate = getBoolean(key + ".negate");
        Object value = get(key + ".value");
        ItemStack itemStack = null;

        if (type.equalsIgnoreCase("itemStack")) {
            itemStack = Serialization.deserialize((String) value);
        }

        return switch (type) {
            case "itemStack" -> new ItemMatchCondition(key, priority, negate, itemStack);
            case "itemLoreContains" -> new ItemLoreContainsCondition(key, priority, negate, (String) value);
            case "itemLoreMatch" -> {
                value = getStringList(key + ".value");
                yield new ItemLoreMatchCondition(key, priority, negate, (List<String>) value);
            }
            case "stringMatch" -> new StringMatchItemCondition(key, priority, negate, (String) value);
            case "itemDurability" -> new ItemDurabilityCondition(key, priority, negate, (Integer) value);
            case "biome" -> new BiomeCondition(key, priority, negate, (String) value);
            default -> null;
        };
    }

}
