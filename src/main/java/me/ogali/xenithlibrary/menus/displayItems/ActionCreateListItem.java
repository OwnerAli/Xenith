package me.ogali.xenithlibrary.menus.displayItems;

import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.utilities.Chat;
import me.ogali.xenithlibrary.utilities.ItemBuilder;
import org.bukkit.Material;

public class ActionCreateListItem extends ItemBuilder {

    public ActionCreateListItem(String abstractActionClassName) {
        super(Material.FLINT_AND_STEEL);
        setName(Chat.colorize("&d" + abstractActionClassName));
    }

    public ActionCreateListItem(AbstractAction<?, ?> action, AbstractCondition<?, ?> abstractCondition, boolean addToPassList) {
        super(Material.LIME_DYE);
        addLoreLines("Type: " + action.getClass().getSimpleName(),
                "Value: " + action.getValue(), "Chance: " + action.getChance(),
                "");

        if (addToPassList && abstractCondition.getPassActionHolder().contains(action)) {
            setName("&c" + action.getId());
            setMaterial(Material.RED_DYE);
            addLoreLine("&cClick to remove!");
            return;
        } else if (!addToPassList && abstractCondition.getFailActionHolder().contains(action)) {
            setName("&c" + action.getId());
            setMaterial(Material.RED_DYE);
            addLoreLine("&cClick to remove!");
            return;
        }

        setName("&a" + action.getId());
        addLoreLine("&aClick to add!");
    }

}
