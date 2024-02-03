package me.ogali.xenithlibrary.menus.guiItems;

import me.despical.inventoryframework.GuiItem;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.menus.displayItems.ConditionListItem;

public class ConditionListGuiItem extends GuiItem {

    public ConditionListGuiItem(AbstractCondition<?, ?> abstractCondition) {
        super(new ConditionListItem(abstractCondition).build());
    }

}
