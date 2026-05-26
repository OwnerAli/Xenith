package me.ogali.xenithlibrary.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.ogali.xenithlibrary.action.domain.ActionContext;
import me.ogali.xenithlibrary.action.domain.ActionRegistry;
import me.ogali.xenithlibrary.menus.actions.ActionCreateMenu;
import me.ogali.xenithlibrary.menus.actions.ActionListMenu;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.Player;

@CommandAlias("xenithaction|xa")
@SuppressWarnings("unused")
public class ActionCommands extends BaseCommand {

    @Subcommand("create")
    @CommandPermission("xenith.action.create")
    @Syntax("<id>")
    @Description("Create a new action")
    public void onCreate(Player player, String id) {
        if (ActionRegistry.isRegistered(id)) {
            Chat.tellFormatted(player, "&cAn action with id &e%s &calready exists.", id);
            return;
        }
        Chat.tellFormatted(player, "&aCreating action: &e%s", id);
        ActionCreateMenu.show(player, id);
    }

    @Subcommand("edit")
    @CommandPermission("xenith.action.edit")
    @CommandCompletion("@actions")
    @Syntax("<id>")
    @Description("Edit an existing action")
    public void onEdit(Player player, String id) {
        if (!ActionRegistry.isRegistered(id)) {
            Chat.tellFormatted(player, "&cNo action with id &e%s &cexists.", id);
            return;
        }
        ActionListMenu.show(player);
        Chat.tellFormatted(player, "&aEditing action: &e%s", id);
    }

    @Subcommand("list")
    @CommandPermission("xenith.action.list")
    @Description("List all registered actions")
    public void onList(Player player) {
        if (ActionRegistry.allInstances().isEmpty()) {
            Chat.tell(player, "&cNo actions registered.");
            return;
        }
        ActionListMenu.show(player);
    }

    @Subcommand("delete")
    @CommandPermission("xenith.action.delete")
    @CommandCompletion("@actions")
    @Syntax("<id>")
    @Description("Delete an existing action")
    public void onDelete(Player player, String id) {
        if (!ActionRegistry.isRegistered(id)) {
            Chat.tellFormatted(player, "&cNo action with id &e%s &cexists.", id);
            return;
        }
        ActionRegistry.delete(id);
        Chat.tellFormatted(player, "&aAction &e%s &adeleted.", id);
    }

    @Subcommand("test")
    @CommandPermission("xenith.action.test")
    @CommandCompletion("@actions")
    @Syntax("<id>")
    @Description("Execute an action against yourself for testing")
    public void onTest(Player player, String id) {
        if (!ActionRegistry.isRegistered(id)) {
            Chat.tellFormatted(player, "&cNo action with id &e%s &cexists.", id);
            return;
        }
        ActionRegistry.get(id).execute(ActionContext.of(player));
        Chat.tellFormatted(player, "&aExecuted action: &e%s", id);
    }
}