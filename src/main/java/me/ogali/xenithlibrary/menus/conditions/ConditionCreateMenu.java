package me.ogali.xenithlibrary.menus.conditions;

import me.despical.inventoryframework.Gui;
import me.despical.inventoryframework.GuiItem;
import me.despical.inventoryframework.pane.PaginatedPane;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.manager.RegistryManager;
import me.ogali.xenithlibrary.menus.guiItems.ConditionCreateListGuiItem;
import me.ogali.xenithlibrary.registiry.impl.ConditionRegistry;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ConditionCreateMenu {

    private final RegistryManager registryManager;

    public ConditionCreateMenu(RegistryManager registryManager) {
        this.registryManager = registryManager;
    }

    public void show(Player player, String id, int priority, boolean negate) {
        Gui gui = new Gui(XenithLibrary.getInstance(), 4, Chat.colorize("&8Pick a Condition Type"));
        PaginatedPane pgPane = new PaginatedPane(0, 0, 9, 4);

        ConditionRegistry conditionRegistry = registryManager.getRegistry(ConditionRegistry.class);
        List<Class<? extends AbstractCondition<?, ?>>> conditionTypes = conditionRegistry.getConditionTypes();
        List<GuiItem> guiItems = new ArrayList<>();

        conditionTypes.forEach(abstractConditionClass -> guiItems.add(new ConditionCreateListGuiItem(abstractConditionClass, id, priority, negate)));

        pgPane.populateWithGuiItems(guiItems);
        gui.addPane(pgPane);
        gui.show(player);
    }

}
