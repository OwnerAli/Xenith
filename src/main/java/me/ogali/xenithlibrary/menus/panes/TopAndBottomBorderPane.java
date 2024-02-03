package me.ogali.xenithlibrary.menus.panes;

import me.despical.inventoryframework.GuiItem;
import me.despical.inventoryframework.pane.StaticPane;
import me.ogali.xenithlibrary.utilities.ItemBuilder;
import org.bukkit.Material;

public abstract class TopAndBottomBorderPane extends StaticPane {

    public TopAndBottomBorderPane(int height, Material borderColor) {
        super(0, 0, 9, height);

        for (int i = 0; i < 9; i++) {
            if (height == 5) {
                addItem(new GuiItem(new ItemBuilder(borderColor).setCustomModelData(696916969).setName("&f").build(), click -> click.setCancelled(true)), i, 0);
                addItem(new GuiItem(new ItemBuilder(borderColor).setCustomModelData(696916969).setName("&f").build(), click -> click.setCancelled(true)), i, 4);
            } else {
                addItem(new GuiItem(new ItemBuilder(borderColor).setCustomModelData(696916969).setName("&f").build(), click -> click.setCancelled(true)), i, 0);
                addItem(new GuiItem(new ItemBuilder(borderColor).setCustomModelData(696916969).setName("&f").build(), click -> click.setCancelled(true)), i, 5);
            }
        }
    }

}
