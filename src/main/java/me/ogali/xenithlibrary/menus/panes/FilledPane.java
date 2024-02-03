package me.ogali.xenithlibrary.menus.panes;

import me.despical.inventoryframework.GuiItem;
import me.ogali.xenithlibrary.utilities.ItemBuilder;
import org.bukkit.Material;

public class FilledPane extends TopAndBottomBorderPane {

    public FilledPane(int height, Material borderColor, Material fillColor) {
        super(height, borderColor);

        for (int i = 0; i < 9; i++) {
            addItem(new GuiItem(new ItemBuilder(fillColor).setName("&f").build(), click -> click.setCancelled(true)), i, 1);
            addItem(new GuiItem(new ItemBuilder(fillColor).setName("&f").build(), click -> click.setCancelled(true)), i, 2);
            addItem(new GuiItem(new ItemBuilder(fillColor).setName("&f").build(), click -> click.setCancelled(true)), i, 3);
        }
    }

}
