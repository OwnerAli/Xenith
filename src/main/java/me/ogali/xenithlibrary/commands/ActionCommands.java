package me.ogali.xenithlibrary.commands;

import co.aikar.commands.annotation.*;
import me.ogali.xenithlibrary.manager.RegistryManager;
import me.ogali.xenithlibrary.menus.actions.ActionCreateMenu;
import me.ogali.xenithlibrary.menus.actions.ActionListMenu;
import me.ogali.xenithlibrary.menus.actions.ActionSettingsMenu;
import me.ogali.xenithlibrary.registiry.impl.ActionRegistry;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.Player;

@CommandAlias("xenithcondition|xc")
@SuppressWarnings("unused")
public class ActionCommands {

    private final RegistryManager registryManager;
    private final ActionRegistry actionRegistry;

    public ActionCommands(RegistryManager registryManager) {
        this.registryManager = registryManager;
        this.actionRegistry = registryManager.getRegistry(ActionRegistry.class);
    }

    @Subcommand("action create")
    @CommandPermission("zenith.action.create")
    @Syntax("[id]")
    public void onActionCreate(Player player, String id) {
        actionRegistry.get(id)
                .ifPresentOrElse(abstractAction ->
                                Chat.tellFormatted(player, "&cAn action with id %s, already exists.", id),
                        () -> new ActionCreateMenu(registryManager).show(player, id));
    }

    @Subcommand("action edit")
    @CommandPermission("zenith.action.edit")
    @CommandCompletion("@actions")
    @Syntax("[id]")
    public void onActionEdit(Player player, String id) {
        actionRegistry.get(id)
                .ifPresentOrElse(action -> new ActionSettingsMenu().show(player, action),
                        () -> Chat.tellFormatted(player, "&cAn action with id %s, does not exist.", id));
    }

    @Subcommand("action list")
    @CommandPermission("zenith.condition.list")
    public void onActionList(Player player) {
        ActionListMenu.show(player);
    }

}
