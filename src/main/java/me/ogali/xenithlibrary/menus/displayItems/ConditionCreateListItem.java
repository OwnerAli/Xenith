package me.ogali.xenithlibrary.menus.displayItems;

import me.ogali.xenithlibrary.utilities.Chat;
import me.ogali.xenithlibrary.utilities.ItemBuilder;
import org.bukkit.Material;

public class ConditionCreateListItem extends ItemBuilder {

    public ConditionCreateListItem(String abstractConditionClassName, boolean inverted) {
        super(Material.COMPARATOR);
        setName(inverted ? Chat.colorize("&aIf " + abstractConditionClassName) :
                Chat.colorize("&cIf not" + abstractConditionClassName));
    }

}
