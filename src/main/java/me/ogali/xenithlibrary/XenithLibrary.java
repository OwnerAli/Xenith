package me.ogali.xenithlibrary;

import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.action.domain.Executable;
import me.ogali.xenithlibrary.action.impl.impl.impl.CancelEventAction;
import me.ogali.xenithlibrary.action.impl.impl.impl.UnCancelEventAction;
import me.ogali.xenithlibrary.commands.ConditionCommands;
import me.ogali.xenithlibrary.files.impl.ActionsFile;
import me.ogali.xenithlibrary.files.impl.ConditionsFile;
import me.ogali.xenithlibrary.manager.RegistryManager;
import me.ogali.xenithlibrary.prompt.listeners.PlayerChatListener;
import me.ogali.xenithlibrary.registiry.domain.Registry;
import me.ogali.xenithlibrary.registiry.domain.impl.AbstractMapRegistry;
import me.ogali.xenithlibrary.registiry.impl.ActionRegistry;
import me.ogali.xenithlibrary.registiry.impl.ChatPromptRegistry;
import me.ogali.xenithlibrary.registiry.impl.ConditionRegistry;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.Random;

public final class XenithLibrary extends JavaPlugin {
    public static boolean PAPI = true;

    @Getter
    private static XenithLibrary instance;
    @Getter
    private RegistryManager registryManager;
    @Getter
    private ConditionsFile conditionsFile;
    @Getter
    private ActionsFile actionsFile;

    @Getter
    private Random random;

    @Override
    public void onEnable() {
        instance = this;
        random = new Random();
        loadPapi();
        initializeFiles();
        initializeRegistries(getClass().getPackageName());
        registerConditionTypes(getClass().getPackageName());
        registerActionTypes(getClass().getPackageName());
        registerActions();
        loadDataFromFiles();
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        registryManager.saveAllRegistries();
    }

    private void initializeFiles() {
        conditionsFile = new ConditionsFile();
        actionsFile = new ActionsFile();
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
            if (Modifier.isAbstract(abstractConditionClass.getModifiers())) continue;
            conditionRegistry.getConditionTypesList().add((Class<? extends AbstractCondition<?, ?>>) abstractConditionClass);
        }
    }

    private void registerActionTypes(String packageName) {
        ActionRegistry actionRegistry = registryManager.getRegistry(ActionRegistry.class);

        Reflections reflections = new Reflections(packageName);

        for (Class<?> abstractActionClass : reflections.getSubTypesOf(Executable.class)) {
            if (Modifier.isAbstract(abstractActionClass.getModifiers())) continue;
            if (abstractActionClass.getSimpleName().contains("Event")) continue;

            actionRegistry.registerActionType(((Class<? extends AbstractAction<?, ?>>) abstractActionClass));
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
        cm.getCommandCompletions().registerCompletion("actions", c -> registryManager.getRegistry(ActionRegistry.class)
                .getObjectMap().values().stream().map(AbstractAction::getId).toList());
    }

    private void registerActions() {
        ActionRegistry actionRegistry = registryManager.getRegistry(ActionRegistry.class);
        actionRegistry.register(new CancelEventAction());
        actionRegistry.register(new UnCancelEventAction());
    }

    private void loadDataFromFiles() {
        registryManager.getRegistry(ActionRegistry.class).loadFromFile(actionsFile);
        registryManager.getRegistry(ConditionRegistry.class).loadFromFile(conditionsFile);
    }

    private void loadPapi() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            PAPI = false;
            Chat.log("Could not find PlaceholderAPI! This plugin is required for action placeholders.");
        }
    }
}