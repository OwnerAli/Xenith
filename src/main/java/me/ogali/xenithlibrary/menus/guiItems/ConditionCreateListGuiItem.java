package me.ogali.xenithlibrary.menus.guiItems;

import me.despical.inventoryframework.GuiItem;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.condition.impl.impl.ItemDurabilityCondition;
import me.ogali.xenithlibrary.menus.displayItems.ConditionCreateListItem;

public class ConditionCreateListGuiItem extends GuiItem {

    public ConditionCreateListGuiItem(Class<? extends AbstractCondition<?, ?>> abstractConditionClass, String id, boolean inverted) {
        super(new ConditionCreateListItem(abstractConditionClass.getSimpleName(),
                inverted).build(), inventoryClickEvent -> {
            inventoryClickEvent.setCancelled(true);
            if (abstractConditionClass == ItemDurabilityCondition.class) {
                new ItemDurabilityCondition(id, -1, inverted, null);
            }



        });
    }

}
