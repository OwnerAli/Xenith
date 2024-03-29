package me.ogali.xenithlibrary.files.impl;

import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.registiry.impl.ActionRegistry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ActionsFile extends XenithJsonFile<AbstractAction<?, ?>> {

    public ActionsFile() {
        super("actions");
    }

    @Override
    public void load() {
        ActionRegistry actionRegistry = XenithLibrary.getInstance().getRegistryManager()
                .getRegistry(ActionRegistry.class);

        singleLayerKeySet().forEach(key -> {
            AbstractAction<?, ?> action = getAction(key);
            actionRegistry.register(action);
        });
    }

    public AbstractAction<?, ?> getAction(String key) {
        String[] parts = getString(key + ".").split(" ");
        String type = parts[0];
        String value = parts[1];
        double chance = Double.parseDouble(parts[2]);

        try {
            Class<?> clazz = Class.forName("me.ogali.xenithlibrary.action.impl.impl.impl." + type);
            Constructor<?> constructor = clazz.getConstructor(String.class, String.class, double.class);

            return (AbstractAction<?, ?>) constructor.newInstance(key, value, chance);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

}
