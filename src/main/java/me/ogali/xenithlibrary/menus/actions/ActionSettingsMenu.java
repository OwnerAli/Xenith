package me.ogali.xenithlibrary.menus.actions;

import me.despical.inventoryframework.Gui;
import me.despical.inventoryframework.GuiItem;
import me.despical.inventoryframework.pane.PaginatedPane;
import me.despical.inventoryframework.pane.StaticPane;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.menus.displayItems.BackButton;
import me.ogali.xenithlibrary.menus.guiItems.ActionChanceButton;
import me.ogali.xenithlibrary.menus.guiItems.ActionValueButton;
import me.ogali.xenithlibrary.menus.guiItems.ExtraSettingGuiItem;
import me.ogali.xenithlibrary.menus.panes.TopAndBottomFivePane;
import me.ogali.xenithlibrary.settings.Setting;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.Player;

import java.util.List;

public class ActionSettingsMenu {

    public void show(Player player, AbstractAction<?, ?> action) {
        if (action.getSettingHolder().getSettingsList().isEmpty()) {
            showGuiWithoutExtra(player, action);
        } else {
            showGuiWithExtraSettings(player, action);
        }
    }

    public void showGuiWithoutExtra(Player player, AbstractAction<?, ?> action) {
        Gui gui = new Gui(XenithLibrary.getInstance(), 5, Chat.colorize("&8Edit Action Settings"));
        StaticPane pane = new TopAndBottomFivePane();
        gui.setOnGlobalClick(click -> click.setCancelled(true));

        pane.addItem(new ActionValueButton(action), 3, 2);

        pane.addItem(new ActionChanceButton(action), 5, 2);

        pane.addItem(new GuiItem(new BackButton().build(), click -> ActionListMenu.show(player)), 4, 4);

        gui.addPane(pane);
        gui.show(player);
    }

    private void showGuiWithExtraSettings(Player player, AbstractAction<?, ?> action) {
        Gui gui = new Gui(XenithLibrary.getInstance(), 5, Chat.colorize("&8Edit Action Settings"));
        StaticPane pane = new TopAndBottomFivePane();
        PaginatedPane paginatedPane = new PaginatedPane(0, 1, 9, 5);
        gui.setOnGlobalClick(click -> click.setCancelled(true));

        pane.addItem(new ActionValueButton(action), 3, 2);

        pane.addItem(new ActionChanceButton(action), 5, 2);

        paginatedPane.populateWithGuiItems(getExtraSettingGuiItems(action.getSettingHolder().getSettingsList(), action));

        pane.addItem(new GuiItem(new BackButton().build(), click -> ActionListMenu.show(player)), 4, 4);

        gui.addPane(pane);
        gui.addPane(paginatedPane);
        gui.show(player);
    }

    private List<GuiItem> getExtraSettingGuiItems(List<Setting> settingsList, AbstractAction<?, ?> action) {
        return List.copyOf(settingsList.stream().map(setting -> new ExtraSettingGuiItem(action, setting)).toList());
    }

}
