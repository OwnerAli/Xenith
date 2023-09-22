package me.ogali.xenithlibrary.menus;

import me.ogali.xenithlibrary.manager.RegistryManager;
import me.ogali.xenithlibrary.registiry.impl.ConditionRegistry;
import org.bukkit.entity.Player;

public class ConditionCreateMenu {

    private final RegistryManager registryManager;

    public ConditionCreateMenu(RegistryManager registryManager) {
        this.registryManager = registryManager;
    }

    public void show(Player player, String id, boolean inverted) {
        ConditionRegistry conditionRegistry = registryManager.getRegistry(ConditionRegistry.class);
        conditionRegistry.getConditionTypes();


    }

}
