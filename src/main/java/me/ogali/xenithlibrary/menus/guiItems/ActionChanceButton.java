package me.ogali.xenithlibrary.menus.guiItems;

import me.despical.inventoryframework.GuiItem;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.prompt.impl.impl.ActionChancePrompt;
import me.ogali.xenithlibrary.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ActionChanceButton extends GuiItem {

    public ActionChanceButton(AbstractAction<?, ?> action) {
        super(new ItemBuilder(Material.PAPER)
                        .setName("&cChance: " + action.getChance())
                        .addLoreLines("", "&aLeft-Click to change the chance", "of this action being executed.")
                        .build(),
                click -> new ActionChancePrompt(action).prompt((Player) click.getWhoClicked()));
    }

}
