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

    public ActionCreateListItem(AbstractAction<?, ?> abstractAction, AbstractCondition<?, ?> abstractCondition, boolean addToPassList) {
        super(Material.FLINT_AND_STEEL);
        setName("&c" + abstractAction.getId());

        if (addToPassList && abstractCondition.getPassActionHolder().contains(abstractAction)) {
            setName("&c" + abstractAction.getId());
            setMaterial(Material.RED_DYE);
            addLoreLines("", "&cClick to remove!");
            return;
        } else if (!addToPassList && abstractCondition.getFailActionHolder().contains(abstractAction)) {
            setName("&c" + abstractAction.getId());
            setMaterial(Material.RED_DYE);
            addLoreLines("", "&cClick to remove!");
            return;
        }

        addLoreLines("", "&aClick to add!");
    }

}
