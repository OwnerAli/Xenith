package me.ogali.xenithlibrary.menus.guiItems;

import me.despical.inventoryframework.GuiItem;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.holder.impl.ActionHolder;
import me.ogali.xenithlibrary.menus.actions.ActionListMenu;
import me.ogali.xenithlibrary.menus.actions.ActionSettingsMenu;
import me.ogali.xenithlibrary.menus.displayItems.ActionListItem;
import me.ogali.xenithlibrary.registiry.impl.ActionRegistry;
import org.bukkit.entity.Player;

public class ActionEditListItem extends GuiItem {

    public ActionEditListItem(AbstractAction<?, ?> action) {
        super(new ActionListItem(action).build(),
                click -> {
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
        super(new ActionListItem(action, actionHolder).build(),
                click -> {
                    if (actionHolder.contains(action)) {
                        actionHolder.getActionList().remove(action);
                    } else {
                        actionHolder.getActionList().add(action);
                    }
                    ActionListMenu.show((Player) click.getWhoClicked(), actionHolder);
                });
    }

}
