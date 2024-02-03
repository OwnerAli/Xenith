package me.ogali.xenithlibrary;

import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import me.ogali.xenithlibrary.commands.ConditionCommands;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.condition.domain.Condition;
import me.ogali.xenithlibrary.manager.RegistryManager;
import me.ogali.xenithlibrary.prompt.listeners.PlayerChatListener;
import me.ogali.xenithlibrary.registiry.domain.Registry;
import me.ogali.xenithlibrary.registiry.domain.impl.AbstractMapRegistry;
import me.ogali.xenithlibrary.registiry.impl.ChatPromptRegistry;
import me.ogali.xenithlibrary.registiry.impl.ConditionRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        registerConditionTypes(getClass().getPackageName());
        registerListeners();
        registerCommands();
    }

    private void initializeRegistries(String packageName) {
        this.registryManager = new RegistryManager();

        Reflections reflections = new Reflections(packageName);

        for (Class<?> registryClass : reflections.getSubTypesOf(AbstractMapRegistry.class)) {
            registryManager.initializeRegistry((Class<? extends Registry<?, ?>>) registryClass);
        }
    }

    private void registerConditionTypes(String packageName) {
        ConditionRegistry conditionRegistry = registryManager.getRegistry(ConditionRegistry.class);

        Reflections reflections = new Reflections(packageName);

        for (Class<?> abstractConditionClass : reflections.getSubTypesOf(Condition.class)) {
            conditionRegistry.getConditionTypes().add((Class<? extends AbstractCondition<?, ?>>) abstractConditionClass);
        }
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(registryManager
                .getRegistry(ChatPromptRegistry.class)), this);
    }

    private void registerCommands() {
        PaperCommandManager cm = new PaperCommandManager(this);

        cm.setFormat(MessageType.SYNTAX, ChatColor.RED, ChatColor.RED);
        cm.registerCommand(new ConditionCommands(registryManager));
    }

}
