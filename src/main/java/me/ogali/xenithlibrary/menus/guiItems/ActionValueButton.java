package me.ogali.xenithlibrary.menus.guiItems;

import me.despical.inventoryframework.GuiItem;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.action.domain.impl.impl.StringValuePlayerAction;
import me.ogali.xenithlibrary.prompt.impl.impl.DoubleValueActionPrompt;
import me.ogali.xenithlibrary.prompt.impl.impl.StringValueActionPrompt;
import me.ogali.xenithlibrary.utilities.Chat;
import me.ogali.xenithlibrary.utilities.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ActionValueButton extends GuiItem {

    public ActionValueButton(AbstractAction<?, ?> action) {
        super(new ItemBuilder(Material.PAPER)
                        .setName("&dValue: " + action.getValue())
                        .addLoreLines("", "&aLeft-Click to change value")
                        .build(),
                click -> {
                    Player player = (Player) click.getWhoClicked();
                    if (action instanceof StringValuePlayerAction stringAction) {
                        new StringValueActionPrompt<>(stringAction).prompt(player);
                    } else if (action.getValue() instanceof Double) {
                        new DoubleValueActionPrompt<>((AbstractAction<?, Double>) action).prompt(player);
                    } else {
                        Chat.log("Error creating action: " + "Unknown value type! REPORT THIS TO THE DISCORD!");
                    }
                });
    }

}
