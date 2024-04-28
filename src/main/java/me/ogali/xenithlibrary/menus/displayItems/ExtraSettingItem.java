package me.ogali.xenithlibrary.menus.displayItems;

import me.ogali.xenithlibrary.settings.Setting;
import me.ogali.xenithlibrary.utilities.ItemBuilder;
import org.bukkit.Material;

public class ExtraSettingItem extends ItemBuilder {

    public ExtraSettingItem(Setting setting) {
        super(Material.PAPER);
        setName("&d" + setting.getName() + ": &f" + setting.getValue());
        addLoreLines("", "&aLeft-click to change value");
    }

}
