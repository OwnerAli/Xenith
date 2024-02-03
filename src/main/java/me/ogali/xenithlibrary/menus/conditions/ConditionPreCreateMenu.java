package me.ogali.xenithlibrary.menus.conditions;

import me.despical.inventoryframework.Gui;
import me.despical.inventoryframework.GuiItem;
import me.despical.inventoryframework.pane.StaticPane;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.utilities.Chat;
import me.ogali.xenithlibrary.utilities.ItemBuilder;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.entity.Player;

public class ConditionPreCreateMenu {

    public void show(Player player, String id) {
        Gui gui = new Gui(XenithLibrary.getInstance(), 1, Chat.colorize("&8Select An Option"));
        StaticPane staticPane = new StaticPane(0, 0, 9, 1);
        gui.setOnGlobalClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));

        staticPane.addItem(new GuiItem(new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("&a&lIF").build(),
                inventoryClickEvent -> {
                    player.playNote(player.getLocation(), Instrument.FLUTE,
                            Note.natural(0, Note.Tone.E));
                    new ConditionPreCreateSettingsMenu().show(player, id, true);
                }), 0, 0);

        staticPane.addItem(new GuiItem(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("&c&lIF NOT").build(),
                inventoryClickEvent -> {
                    player.playNote(player.getLocation(), Instrument.FLUTE,
                            Note.natural(0, Note.Tone.G));
                    new ConditionPreCreateSettingsMenu().show(player, id, false);
                }), 1, 0);

        gui.addPane(staticPane);
        gui.show(player);
    }

}
