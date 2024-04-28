package me.ogali.xenithlibrary.menus.guiItems;

import me.despical.inventoryframework.GuiItem;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.menus.displayItems.ExtraSettingItem;
import me.ogali.xenithlibrary.prompt.impl.impl.settingValuePrompt;
import me.ogali.xenithlibrary.settings.Setting;
import org.bukkit.entity.Player;

public class ExtraSettingGuiItem extends GuiItem {

    public ExtraSettingGuiItem(AbstractAction<?, ?> action, Setting setting) {
        super(new ExtraSettingItem(setting).build(), click -> {
            new settingValuePrompt(action, setting).prompt((Player) click.getWhoClicked());
            click.getWhoClicked().closeInventory();
        });
    }

}
