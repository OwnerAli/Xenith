package me.ogali.xenithlibrary.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.ogali.xenithlibrary.conditions.domain.ConditionRegistry;
import me.ogali.xenithlibrary.menus.conditions.ConditionCreateMenu;
import me.ogali.xenithlibrary.menus.conditions.ConditionListMenu;
import me.ogali.xenithlibrary.menus.conditions.ConditionSettingsMenu;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.Player;

@CommandAlias("xenithcondition|xc")
@SuppressWarnings("unused")
public class ConditionCommands extends BaseCommand {

    @Subcommand("create")
    @CommandPermission("xenith.condition.create")
    @Syntax("<id>")
    @Description("Create a new condition")
    public void onCreate(Player player, String id) {
        if (ConditionRegistry.isRegistered(id)) {
            Chat.tellFormatted(player, "&cA condition with id &e%s &calready exists.", id);
            return;
        }
        ConditionCreateMenu.show(player, id);
    }

    @Subcommand("edit")
    @CommandPermission("xenith.condition.edit")
    @CommandCompletion("@conditions")
    @Syntax("<id>")
    @Description("Edit an existing condition")
    public void onEdit(Player player, String id) {
        if (!ConditionRegistry.isRegistered(id)) {
            Chat.tellFormatted(player, "&cNo condition with id &e%s &cexists.", id);
            return;
        }
        ConditionSettingsMenu.show(player, ConditionRegistry.get(id), null);
    }

    @Subcommand("list")
    @CommandPermission("xenith.condition.list")
    @Description("List all registered conditions")
    public void onList(Player player) {
        ConditionListMenu.show(player);
    }

    @Subcommand("delete")
    @CommandPermission("xenith.condition.delete")
    @CommandCompletion("@conditions")
    @Syntax("<id>")
    @Description("Delete a condition")
    public void onDelete(Player player, String id) {
        if (!ConditionRegistry.isRegistered(id)) {
            Chat.tellFormatted(player, "&cNo condition with id &e%s &cexists.", id);
            return;
        }
        ConditionRegistry.delete(id);
        Chat.tellFormatted(player, "&cCondition &e%s &cdeleted.", id);
    }
}
