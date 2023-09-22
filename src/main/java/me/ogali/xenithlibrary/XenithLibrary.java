package me.ogali.xenithlibrary;

import lombok.Getter;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.manager.RegistryManager;
import me.ogali.xenithlibrary.registiry.domain.Registry;
import me.ogali.xenithlibrary.registiry.domain.impl.AbstractMapRegistry;
import me.ogali.xenithlibrary.registiry.impl.ConditionRegistry;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

public final class XenithLibrary extends JavaPlugin {

    @Getter
    private static XenithLibrary instance;
    @Getter
    private RegistryManager registryManager;

    @Override
    public void onEnable() {
        instance = this;
        initializeRegistries(getClass().getPackageName());
    }

    @Override
    public void onDisable() {
    }

    public void initializeRegistries(String packageName) {
        this.registryManager = new RegistryManager();

        Reflections reflections = new Reflections(packageName);

        for (Class<?> registryClass : reflections.getSubTypesOf(AbstractMapRegistry.class)) {
            registryManager.initializeRegistry((Class<? extends Registry<?,?>>) registryClass);
        }
    }

    public void registerConditionTypes(String packageName) {
        ConditionRegistry conditionRegistry = registryManager.getRegistry(ConditionRegistry.class);

        Reflections reflections = new Reflections(packageName);

        for (Class<?> abstractConditionClass : reflections.getSubTypesOf(AbstractCondition.class)) {
            conditionRegistry.getConditionTypes().add((Class<? extends AbstractCondition<?, ?>>) abstractConditionClass);
        }
    }

}
