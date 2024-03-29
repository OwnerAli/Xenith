package me.ogali.xenithlibrary.files.impl;

import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.condition.impl.ItemStackCondition;
import me.ogali.xenithlibrary.registiry.impl.ConditionRegistry;
import me.ogali.xenithlibrary.utilities.Serialization;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
        String[] parts = getString(key + ".").split(" ");

        String type = parts[0];
        int priority = Integer.parseInt(parts[1]);
        boolean negate = parts[2].equals("!=");
        String value = parts[3];

        try {
            Class<?> clazz = Class.forName("me.ogali.xenithlibrary.condition.impl.impl." + type);
            Constructor<?> constructor;
            AbstractCondition<?, ?> condition;

            if (type.equals("ItemMatchCondition")) {
                constructor = clazz.getConstructor(String.class, int.class, boolean.class, ItemStack.class);
                condition = (AbstractCondition<?, ?>) constructor.newInstance(key, priority, negate, Serialization.deserialize(value));
            } else {
                constructor = clazz.getConstructor(String.class, int.class, boolean.class, String.class);
                condition = (AbstractCondition<?, ?>) constructor.newInstance(key, priority, negate, value);
            }

            return condition;
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

}
