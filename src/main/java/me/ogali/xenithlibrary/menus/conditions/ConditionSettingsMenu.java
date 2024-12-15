package me.ogali.xenithlibrary.menus.conditions;

import me.despical.inventoryframework.Gui;
import me.despical.inventoryframework.GuiItem;
import me.despical.inventoryframework.pane.StaticPane;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.condition.impl.ItemStackCondition;
import me.ogali.xenithlibrary.menus.actions.ConditionActionAddMenu;
import me.ogali.xenithlibrary.menus.displayItems.DoneItem;
import me.ogali.xenithlibrary.menus.panes.TopAndBottomSixPane;
import me.ogali.xenithlibrary.utilities.Chat;
import me.ogali.xenithlibrary.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ConditionSettingsMenu {

    private int priority = 0;

    public void show(Player player, AbstractCondition<?, ?> condition) {
        Gui gui = new Gui(XenithLibrary.getInstance(), 6, Chat.colorize("&8Edit Condition Settings"));
        StaticPane staticPane = new TopAndBottomSixPane();
        gui.setOnTopClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));

        staticPane.addItem(new GuiItem(new ItemBuilder(Material.GUNPOWDER).setName("&aPass Actions: ")
                .addLoreLines("&f" + Chat.actionListToString(condition.getPassActionHolder().getActionList())).build(),
                click -> new ConditionActionAddMenu().show(player, condition, true)), 2, 2);

        staticPane.addItem(new GuiItem(new ItemBuilder((condition.isNegate() ?
                Material.REPEATER : Material.COMPARATOR)).setName("&fCondition Mode: " + (condition.isNegate() ?
                "&c&lIF NOT" : "&a&lIF")).build(),
                click -> {
                    condition.setNegate(!condition.isNegate());
                    show(player, condition);
                }), 4, 2);

        staticPane.addItem(new GuiItem(new ItemBuilder(Material.GUNPOWDER).setName("&cFail Actions: ")
                .addLoreLines("&f" + Chat.actionListToString(condition.getFailActionHolder().getActionList())).build(),
                click -> new ConditionActionAddMenu().show(player, condition, false)), 6, 2);

        if (condition instanceof ItemStackCondition itemStackCondition) {
            staticPane.addItem(new GuiItem(new ItemBuilder(Material.SHIELD).setName("&a&lOffhand: " + itemStackCondition.isOffhand())
                    .addLoreLines("&fClick to toggle offhand check.").build(),
                    inventoryClickEvent -> {
                        itemStackCondition.setOffhand(!itemStackCondition.isOffhand());
                        show(player, condition);
                    }), 5, 3);
            staticPane.addItem(new GuiItem(new ItemBuilder(Material.CLOCK).setName("&a&lPriority:&f " + condition.getPriority())
                    .addLoreLines("&fClick to change condition priority.", "&f&nLower Priority = Evaluated First!", "",
                            "&aLeft-Click +1", "&aShift-Left-Click +10", "&cRight-Click -1",
                            "&cShift-Right-Click -10").build(),
                    inventoryClickEvent -> {
                        if (inventoryClickEvent.isShiftClick()) {
                            if (inventoryClickEvent.isLeftClick()) {
                                priority += 10;
                            } else if (inventoryClickEvent.isRightClick()) {
                                priority -= 10;
                            }
                            condition.setPriority(priority);
                            show(player, condition);
                            return;
                        }
                        if (inventoryClickEvent.isLeftClick()) {
                            priority++;
                        } else if (inventoryClickEvent.isRightClick()) {
                            priority--;
                        }
                        condition.setPriority(priority);
                        show(player, condition);
                    }), 3, 3);
        } else {
            staticPane.addItem(new GuiItem(new ItemBuilder(Material.CLOCK).setName("&a&lPriority:&f " + condition.getPriority())
                    .addLoreLines("&fClick to change condition priority.", "&f&nLower Priority = Evaluated First!", "",
                            "&aLeft-Click +1", "&aShift-Left-Click +10", "&cRight-Click -1",
                            "&cShift-Right-Click -10").build(),
                    inventoryClickEvent -> {
                        if (inventoryClickEvent.isShiftClick()) {
                            if (inventoryClickEvent.isLeftClick()) {
                                priority += 10;
                            } else if (inventoryClickEvent.isRightClick()) {
                                priority -= 10;
                            }
                            condition.setPriority(priority);
                            show(player, condition);
                            return;
                        }
                        if (inventoryClickEvent.isLeftClick()) {
                            priority++;
                        } else if (inventoryClickEvent.isRightClick()) {
                            priority--;
                        }
                        condition.setPriority(priority);
                        show(player, condition);
                    }), 4, 3);
        }

        staticPane.addItem(new GuiItem(new DoneItem().build(), click -> click.getWhoClicked().closeInventory()), 4, 5);

        gui.addPane(staticPane);
        gui.show(player);
    }

}