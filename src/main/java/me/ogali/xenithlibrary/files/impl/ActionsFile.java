package me.ogali.xenithlibrary.files.impl;

import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.registiry.impl.ActionRegistry;
import org.jetbrains.annotations.Nullable;

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
            if (getAction(key) == null) return;

            actionRegistry.register(getAction(key));
        });
    }

    public AbstractAction<?, ?> getAction(String key) {
        return getAbstractAction(key);
    }

    @Nullable
    private AbstractAction<?, ?> getAbstractAction(String key) {
        String[] parts = getString(key + ".action").split(",");
        String className = parts[0]; // Fully qualified class name
        String value = parts[1];
        double chance = Double.parseDouble(parts[2]);

        try {
            // Try to load the class internally
            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor = findMatchingConstructor(clazz);

            if (constructor == null) {
                throw new NoSuchMethodException("No suitable constructor found for class " + className);
            }

            // Convert the value to the appropriate type
            Class<?> secondParamType = constructor.getParameterTypes()[1];
            Object convertedValue = convertValueToRequiredType(value, secondParamType);

            AbstractAction<?, ?> abstractAction = (AbstractAction<?, ?>) constructor.newInstance(key, convertedValue, chance);
            abstractAction.loadExtraSettings(getString(key + ".extraSettings").split(","));

            return abstractAction;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Constructor<?> findMatchingConstructor(Class<?> clazz) {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            Class<?>[] paramTypes = constructor.getParameterTypes();
            if (paramTypes.length == 3 && paramTypes[0] == String.class && paramTypes[2] == double.class) {
                return constructor;
            }
        }
        return null;
    }

    private Object convertValueToRequiredType(String value, Class<?> requiredType) {
        if (requiredType == int.class || requiredType == Integer.class) {
            return Integer.parseInt(value);
        } else if (requiredType == double.class || requiredType == Double.class) {
            return Double.parseDouble(value);
        } else if (requiredType == boolean.class || requiredType == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (requiredType == String.class) {
            return value;
        }
        // Add more types as needed
        throw new IllegalArgumentException("Unsupported type: " + requiredType);
    }

}
