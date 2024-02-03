package me.ogali.xenithlibrary.menus.displayItems;

import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.utilities.ItemBuilder;
import org.bukkit.Material;

public class ConditionListItem extends ItemBuilder {

    public ConditionListItem(AbstractCondition<?, ?> abstractCondition) {
        super(Material.COMPARATOR);
        setName("&f" + abstractCondition.getId());
        addLoreLine("");
        addLoreLine(abstractCondition.isNegate() ? "&c&lIF NOT" : "&a&lIF");
        addLoreLine("&7" + abstractCondition.getClass().getSimpleName() + " " + abstractCondition.getValue());
    }

}
