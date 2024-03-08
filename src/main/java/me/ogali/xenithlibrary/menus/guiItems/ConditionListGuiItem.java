package me.ogali.xenithlibrary.menus.guiItems;

import me.despical.inventoryframework.GuiItem;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.menus.conditions.ConditionListMenu;
import me.ogali.xenithlibrary.menus.conditions.ConditionSettingsMenu;
import me.ogali.xenithlibrary.menus.displayItems.ConditionListItem;
import me.ogali.xenithlibrary.registiry.impl.ConditionRegistry;
import org.bukkit.entity.Player;

public class ConditionListGuiItem extends GuiItem {

    public ConditionListGuiItem(AbstractCondition<?, ?> abstractCondition) {
        super(new ConditionListItem(abstractCondition).build(),
                click -> {
                    if (click.getClick().isLeftClick()) {
                        new ConditionSettingsMenu().show((Player) click.getWhoClicked(), abstractCondition);
                    } else if (click.isRightClick()) {
                        XenithLibrary.getInstance().getRegistryManager()
                                .getRegistry(ConditionRegistry.class)
                                .getObjectMap()
                                .remove(abstractCondition.getId());
                        ConditionListMenu.show((Player) click.getWhoClicked());
                    }
                });
    }

}