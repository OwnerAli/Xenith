package me.ogali.xenithlibrary.commands;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import me.ogali.xenithlibrary.manager.RegistryManager;
import me.ogali.xenithlibrary.menus.actions.ActionCreateMenu;
import me.ogali.xenithlibrary.menus.actions.ActionListMenu;
import me.ogali.xenithlibrary.registiry.impl.ActionRegistry;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.Player;

@CommandAlias("xenithcondition|xc")
@SuppressWarnings("unused")
public class ActionCommands {

    private final RegistryManager registryManager;

    public ActionCommands(RegistryManager registryManager) {
        this.registryManager = registryManager;
    }

    @Subcommand("action create")
    @CommandPermission("zenith.action.create")
    @Syntax("[id]")
    public void onConditionCreate(Player player, String id) {
        registryManager.getRegistry(ActionRegistry.class)
                .get(id)
                .ifPresentOrElse(abstractAction ->
                                Chat.tellFormatted(player, "&cAn action with id %s, already exists.", id),
                        () -> new ActionCreateMenu(registryManager).show(player, id));
    }

    @Subcommand("action list")
    @CommandPermission("zenith.condition.list")
    public void onActionList(Player player) {
        ActionListMenu.show(player);
    }

}
