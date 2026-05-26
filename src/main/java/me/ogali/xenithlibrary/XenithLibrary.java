package me.ogali.xenithlibrary;

import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import me.ogali.xenithlibrary.action.domain.ActionRegistry;
import me.ogali.xenithlibrary.commands.ActionCommands;
import me.ogali.xenithlibrary.conditions.domain.ConditionRegistry;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.logging.Level;

@Getter
public final class XenithLibrary extends JavaPlugin {

    @Getter
    private static XenithLibrary instance;
    private Random random;

    private boolean papiEnabled;

    public static boolean isPapiEnabled() {
        return instance.papiEnabled;
    }

    @Override
    public void onEnable() {
        instance = this;
        random = new Random();
        papiEnabled = checkPapi();
        loadPluginData();
        registerCommands();
    }

    @Override
    public void onDisable() {
        try {
            ConditionRegistry.saveAll();
            ActionRegistry.saveAll();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to save data on shutdown", e);
        }
    }

    private void loadPluginData() {
        ActionRegistry.loadFromFile();
        ConditionRegistry.loadFromFile();
    }

    private void registerCommands() {
        PaperCommandManager cm = new PaperCommandManager(this);
        cm.setFormat(MessageType.SYNTAX, ChatColor.RED, ChatColor.RED);
        cm.registerCommand(new ActionCommands());
        cm.getCommandCompletions().registerCompletion("actions", c ->
                ActionRegistry.allInstances().keySet().stream().toList());
    }

    private boolean checkPapi() {
        boolean found = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
        if (!found) {
            Chat.log("PlaceholderAPI not found — placeholder conditions will be unavailable.");
        }
        return found;
    }
}