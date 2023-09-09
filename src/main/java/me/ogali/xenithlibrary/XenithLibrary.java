package me.ogali.xenithlibrary;

import lombok.Getter;
import me.ogali.xenithlibrary.manager.RegistryManager;
import me.ogali.xenithlibrary.registiry.domain.Registry;
import me.ogali.xenithlibrary.registiry.domain.impl.AbstractMapRegistry;
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
        initializeRegistries();
    }

    @Override
    public void onDisable() {
    }

    private void initializeRegistries() {
        this.registryManager = new RegistryManager();

        Reflections reflections = new Reflections("me.ogali.xenithlibrary");

        for (Class<?> registryClass : reflections.getSubTypesOf(AbstractMapRegistry.class)) {
            registryManager.setRegistry((Class<? extends Registry<?,?>>) registryClass);
        }
    }

}
