package me.ogali.xenithlibrary.actions.domain;

import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.actions.impl.*;
import me.ogali.xenithlibrary.files.impl.ActionsFile;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.Material;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ActionRegistry {
    private static final Map<String, ActionType> types = new HashMap<>();
    private static final Map<String, AbstractAction> instances = new HashMap<>();
    private static final ActionsFile file = new ActionsFile();

    static {
        registerType(new ActionType("COMPOSITE", CompositeAction::fromConfig, Material.COPPER_CHAIN));
        registerType(new ActionType("CANCEL_EVENT", CancelEventAction::fromConfig, Material.BARRIER));
        registerType(new ActionType("UNCANCEL_EVENT", UnCancelEventAction::fromConfig, Material.LIME_STAINED_GLASS));
        registerType(new ActionType("BROADCAST", BroadcastAction::fromConfig, Material.PAPER));
        registerType(new ActionType("CONSOLE_COMMAND", ConsoleCommandAction::fromConfig, Material.COMMAND_BLOCK));
        registerType(new ActionType("PLAYER_COMMAND", PlayerCommandAction::fromConfig, Material.COMMAND_BLOCK_MINECART));
        registerType(new ActionType("PLAYER_MESSAGE", PlayerMessageAction::fromConfig, Material.WRITABLE_BOOK));
        registerType(new ActionType("PLAYER_SOUND", PlayerSoundAction::fromConfig, Material.NOTE_BLOCK));
        registerType(new ActionType("GLOBAL_SOUND", GlobalSoundAction::fromConfig, Material.JUKEBOX));
        registerType(new ActionType("PLAYER_ACTION_BAR", PlayerActionBarAction::fromConfig, Material.NAME_TAG));
        registerType(new ActionType("PLAYER_TITLE", PlayerTitleAction::fromConfig, Material.BOOK));
        registerType(new ActionType("HEAL_AURA", HealAuraAction::fromConfig, Material.GOLDEN_APPLE)); // Register the new HealAuraAction
    }

    private ActionRegistry() {
    }

    public static void registerType(ActionType type, String namespace) {
        String key = "THIRD_PARTY::" + namespace + "::" + type.key().toUpperCase();
        if (types.containsKey(key)) {
            throw new IllegalStateException("Action type already registered: " + key);
        }
        types.put(key, type);
    }

    public static ActionType getType(String key) {
        ActionType type = types.get(key.toUpperCase());
        if (type == null) {
            throw new IllegalArgumentException(
                    "Unknown action type: '" + key + "'. " +
                            "Registered types: " + types.keySet()
            );
        }
        return type;
    }

    public static boolean isTypeRegistered(String key) {
        return types.containsKey(key.toUpperCase());
    }

    public static void loadFromFile() {
        Map<String, Map<String, Object>> data = file.loadAll();
        data.forEach((key, config) -> {
            try {
                String typeKey = ((String) config.get("type")).toUpperCase();
                if (typeKey.contains("THIRD_PARTY::")) return;

                ActionType type = getType(typeKey);
                AbstractAction action = type.builder().build(new DomainConfig(config));
                action.setId(key);
                action.setTypeKey(typeKey);
                instances.put(key, action);
            } catch (Exception e) {
                log("Failed to load action '" + key + "': " + e.getMessage());
            }
        });
    }

    public static void loadThirdParty() {
        Map<String, Map<String, Object>> data = file.loadAll();
        data.forEach((key, config) -> {
            try {
                String typeKey = ((String) config.get("type")).toUpperCase();
                if (!typeKey.contains("THIRD_PARTY::")) return;

                ActionType type = getType(typeKey);
                AbstractAction action = type.builder().build(new DomainConfig(config));
                action.setId(key);
                action.setTypeKey(typeKey);
                instances.put(key, action);
            } catch (Exception e) {
                log("Failed to load action '" + key + "': " + e.getMessage());
            }
        });
    }

    public static void saveAll() {
        Map<String, Object> serialized = new HashMap<>();
        instances.forEach((key, action) -> serialized.put(key, action.serialize()));
        file.saveAll(serialized)
                .exceptionally(e -> {
                    log("Failed to save actions: " + e.getMessage());
                    return null;
                });
    }

    /**
     * Registers a runtime-created action and persists it.
     */
    public static void register(AbstractAction action) {
        instances.put(action.getId(), action);
        file.save(action.getId(), action.serialize());
    }

    public static boolean isRegistered(String id) {
        return instances.containsKey(id);
    }

    public static void delete(String id) {
        instances.remove(id);
        file.delete(id);
    }

    public static AbstractAction get(String id) {
        if (id.contains("THIRD_PARTY::")) return null;
        AbstractAction action = instances.get(id);
        if (action == null) {
            throw new IllegalArgumentException(
                    "No action with id '" + id + "'. " +
                            "Registered: " + instances.keySet()
            );
        }
        return action;
    }

    public static Map<String, AbstractAction> allInstances() {
        return Collections.unmodifiableMap(instances);
    }

    public static Map<String, ActionType> allTypes() {
        return Collections.unmodifiableMap(types);
    }

    static void reset() {
        types.clear();
        instances.clear();
    }

    private static void registerType(ActionType type) {
        String key = type.key().toUpperCase();
        if (types.containsKey(key)) {
            throw new IllegalStateException("Action type already registered: " + key);
        }
        types.put(key, type);
    }

    private static void log(String message) {
        XenithLibrary.getInstance().getLogger().warning(message);
    }
}