package me.ogali.xenithlibrary.menus.guiItems;

import me.despical.inventoryframework.GuiItem;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.holder.impl.ActionHolder;
import me.ogali.xenithlibrary.menus.actions.ActionListMenu;
import me.ogali.xenithlibrary.menus.actions.ActionSettingsMenu;
import me.ogali.xenithlibrary.registiry.impl.ActionRegistry;
import me.ogali.xenithlibrary.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ActionEditListItem extends GuiItem {

    public ActionEditListItem(AbstractAction<?, ?> action) {
        super(new ItemBuilder(Material.HEART_OF_THE_SEA)
                .setName("&b" + action.getId())
                .build(), click -> {
            if (click.getClick().isLeftClick()) {
                new ActionSettingsMenu().show((Player) click.getWhoClicked(), action);
            } else if (click.isRightClick()) {
                XenithLibrary.getInstance().getRegistryManager()
                        .getRegistry(ActionRegistry.class)
                        .getObjectMap()
                        .remove(action.getId());
                ActionListMenu.show((Player) click.getWhoClicked());
            }
        });
    }

    public ActionEditListItem(AbstractAction<?, ?> action, ActionHolder actionHolder) {
        super(new ItemBuilder(actionHolder.contains(action) ? Material.NAUTILUS_SHELL : Material.HEART_OF_THE_SEA)
                .setName(actionHolder.contains(action) ? "&c" + action.getId() : "&b" + action.getId())
                .addLoreLines("", actionHolder.contains(action) ? "&cClick to remove action." : "&aClick to add action.")
                .build(), click -> {
            if (actionHolder.contains(action)) {
                actionHolder.getActionList().remove(action);
            } else {
                actionHolder.getActionList().add(action);
            }
            ActionListMenu.show((Player) click.getWhoClicked(), actionHolder);
        });
    }

}
