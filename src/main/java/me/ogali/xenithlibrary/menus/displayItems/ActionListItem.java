package me.ogali.xenithlibrary.menus.displayItems;

import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.holder.impl.ActionHolder;
import me.ogali.xenithlibrary.utilities.ItemBuilder;
import org.bukkit.Material;

public class ActionListItem extends ItemBuilder {

    public ActionListItem(AbstractAction<?, ?> action) {
        super(Material.LIME_DYE);
        addLoreLines("Type: " + action.getClass().getSimpleName(),
                "Value: " + action.getValue(), "Chance: " + action.getChance(),
                "", "&aLeft-Click to edit", "&cRight-Click to delete");
    }

    public ActionListItem(AbstractAction<?, ?> action, ActionHolder actionHolder) {
        super(Material.LIME_DYE);
        addLoreLines("Type: " + action.getClass().getSimpleName(),
                "Value: " + action.getValue(), "Chance: " + action.getChance(), "");

        if (actionHolder.getActionList().contains(action)) {
            setName("&c" + action.getId());
            setMaterial(Material.RED_DYE);
            addLoreLine("&cClick to remove from actions.");
            return;
        }

        setName("&c" + action.getId());
        addLoreLine("&aClick to add to actions.");
    }

}
