//package me.ogali.xenithlibrary.commands;
//
//import co.aikar.commands.BaseCommand;
//import co.aikar.commands.annotation.*;
//import me.ogali.xenithlibrary.manager.RegistryManager;
//import me.ogali.xenithlibrary.menus.conditions.ConditionListMenu;
//import me.ogali.xenithlibrary.menus.conditions.ConditionModeSelectionMenu;
//import me.ogali.xenithlibrary.menus.conditions.ConditionSettingsMenu;
//import me.ogali.xenithlibrary.registiry.impl.ConditionRegistry;
//import me.ogali.xenithlibrary.utilities.Chat;
//import org.bukkit.entity.Player;
//
//@CommandAlias("xenithcondition|xc")
//@SuppressWarnings("unused")
//public class ConditionCommands extends BaseCommand {
//
//    private final ConditionRegistry conditionRegistry;
//
//    public ConditionCommands(RegistryManager registryManager) {
//        this.conditionRegistry = registryManager.getRegistry(ConditionRegistry.class);
//    }
//
//    @Subcommand("create")
//    @CommandPermission("zenith.condition.create")
//    @Syntax("[id]")
//    public void onConditionCreate(Player player, String id) {
//        conditionRegistry.get(id)
//                .ifPresentOrElse(abstractCondition ->
//                                Chat.tellFormatted(player, "&cA condition with id %s, already exists.", id),
//                        () -> new ConditionModeSelectionMenu().show(player, id));
//    }
//
//    @Subcommand("condition edit")
//    @CommandPermission("zenith.condition.edit")
//    @CommandCompletion("@conditions")
//    @Syntax("[id]")
//    public void onConditionEdit(Player player, String id) {
//        conditionRegistry.get(id)
//                .ifPresentOrElse(condition -> new ConditionSettingsMenu().show(player, condition),
//                        () -> Chat.tellFormatted(player, "&cA condition with id %s, does not exist.", id));
//    }
//
//    @Subcommand("list")
//    @CommandPermission("zenith.condition.list")
//    public void onConditionList(Player player) {
//        ConditionListMenu.show(player);
//    }
//
//}
