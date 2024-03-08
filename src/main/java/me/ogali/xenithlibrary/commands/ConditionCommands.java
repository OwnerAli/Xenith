package me.ogali.xenithlibrary.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import me.ogali.xenithlibrary.manager.RegistryManager;
import me.ogali.xenithlibrary.menus.conditions.ConditionListMenu;
import me.ogali.xenithlibrary.menus.conditions.ConditionModeSelectionMenu;
import me.ogali.xenithlibrary.registiry.impl.ConditionRegistry;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.Player;

@CommandAlias("xenithcondition|xc")
@SuppressWarnings("unused")
public class ConditionCommands extends BaseCommand {

    private final RegistryManager registryManager;

    public ConditionCommands(RegistryManager registryManager) {
        this.registryManager = registryManager;
    }

    @Subcommand("create")
    @CommandPermission("zenith.condition.create")
    @Syntax("[id]")
    public void onConditionCreate(Player player, String id) {
        registryManager.getRegistry(ConditionRegistry.class)
                .get(id)
                .ifPresentOrElse(abstractCondition ->
                                Chat.tellFormatted(player, "&cA condition with id %s, already exists.", id),
                        () -> new ConditionModeSelectionMenu().show(player, id));
    }

    @Subcommand("list")
    @CommandPermission("zenith.condition.list")
    public void onConditionList(Player player) {
        ConditionListMenu.show(player);
    }

}
