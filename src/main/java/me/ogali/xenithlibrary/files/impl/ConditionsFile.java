package me.ogali.xenithlibrary.files.impl;

import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.condition.impl.ItemStackCondition;
import me.ogali.xenithlibrary.registiry.impl.ActionRegistry;
import me.ogali.xenithlibrary.registiry.impl.ConditionRegistry;
import me.ogali.xenithlibrary.utilities.Serialization;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

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
            if (!(key.contains(XenithLibrary.getInstance().getName()))) return;
            conditionRegistry.register(getCondition(key));
        });
    }

    public void delete(String conditionId) {
        set(conditionId, null);
    }

    public AbstractCondition<?, ?> getCondition(String key) {
        return getAbstractCondition(key);
    }

    @Nullable
    private AbstractCondition<?, ?> getAbstractCondition(String key) {
        String[] parts = getString(key + ".condition").split(" ");
        List<String> passActionIdList = getStringList(key + ".passActions");
        List<String> failActionIdList = getStringList(key + ".failActions");

        String type = parts[0];
        int priority = Integer.parseInt(parts[1]);
        boolean negate = parts[2].equals("!=");
        StringBuilder value = new StringBuilder(parts[3]);
        boolean offhand = false;

        for (int i = 4; i < parts.length; i++) {
            if (parts[i].equalsIgnoreCase("offhand")) {
                offhand = true;
                continue;
            } else if (parts[i].equalsIgnoreCase("mainhand")) {
                offhand = false;
                continue;
            }
            value.append(" ").append(parts[i]);
        }

        try {
            Class<?> clazz = Class.forName(type);
            Constructor<?> constructor;
            AbstractCondition<?, ?> condition;

            if (type.equals("ItemMatchCondition")) {
                constructor = clazz.getConstructor(String.class, int.class, boolean.class, ItemStack.class);
                condition = (AbstractCondition<?, ?>) constructor.newInstance(key, priority, negate, Serialization.deserialize(value.toString()));
                if (offhand) {
                    ((ItemStackCondition<?>) condition).setOffhand(true);
                }
            } else if (type.equals("itemStack")) {
                constructor = clazz.getConstructor(String.class, int.class, boolean.class, String.class);
                condition = (AbstractCondition<?, ?>) constructor.newInstance(key, priority, negate, value.toString());
                if (offhand) {
                    ((ItemStackCondition<?>) condition).setOffhand(true);
                }
            } else {
                constructor = clazz.getConstructor(String.class, int.class, boolean.class, String.class);
                condition = (AbstractCondition<?, ?>) constructor.newInstance(key, priority, negate, value.toString());
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
