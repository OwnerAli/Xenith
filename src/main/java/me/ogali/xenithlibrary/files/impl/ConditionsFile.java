package me.ogali.xenithlibrary.files.impl;

import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.registiry.impl.ActionRegistry;
import me.ogali.xenithlibrary.registiry.impl.ConditionRegistry;
import me.ogali.xenithlibrary.utilities.Serialization;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ConditionsFile extends XenithJsonFile<AbstractCondition<?, ?>> {

    public ConditionsFile() {
        super("conditions");
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
        String[] parts = getString(key + ".condition").split(" ");
        List<String> passActionIdList = getStringList(key + ".passActions");
        List<String> failActionIdList = getStringList(key + ".failActions");

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
            populatePassActionHolderFromIdList(passActionIdList, condition);
            populateFailActionHolderFromIdList(failActionIdList, condition);

            return condition;
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void populatePassActionHolderFromIdList(List<String> idList, AbstractCondition<?, ?> condition) {
        ActionRegistry actionRegistry = XenithLibrary.getInstance().getRegistryManager()
                .getRegistry(ActionRegistry.class);

        idList.stream()
                .map(actionRegistry::get)
                .forEach(optionalAction ->
                        optionalAction.ifPresent(action -> condition.getPassActionHolder().getActionList().add(action)));
    }

    private void populateFailActionHolderFromIdList(List<String> idList, AbstractCondition<?, ?> condition) {
        ActionRegistry actionRegistry = XenithLibrary.getInstance().getRegistryManager()
                .getRegistry(ActionRegistry.class);

        idList.stream()
                .map(actionRegistry::get)
                .forEach(optionalAction ->
                        optionalAction.ifPresent(action -> condition.getFailActionHolder().getActionList().add(action)));
    }

}
