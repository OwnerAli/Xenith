package me.ogali.xenithlibrary.menus.displayItems;

import me.ogali.xenithlibrary.utilities.ItemBuilder;
import org.bukkit.Material;

import java.util.List;

public class NavigationButton extends ItemBuilder {

    public NavigationButton(String buttonText) {
        super(Material.ARROW);
        setName(buttonText);
        setCustomModelData(696916969);
    }

    public NavigationButton(String buttonText, String... lore) {
        super(Material.ARROW);
        setName(buttonText);
        setCustomModelData(696916969);
        setLore(List.of(lore));
    }

}