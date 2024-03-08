package me.ogali.xenithlibrary.menus.guiItems;

import me.despical.inventoryframework.GuiItem;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.condition.impl.ItemStackCondition;
import me.ogali.xenithlibrary.condition.impl.LocationCondition;
import me.ogali.xenithlibrary.condition.impl.impl.BiomeCondition;
import me.ogali.xenithlibrary.condition.impl.impl.*;
import me.ogali.xenithlibrary.menus.conditions.ItemInputConditionMenu;
import me.ogali.xenithlibrary.menus.displayItems.ConditionCreateListItem;
import me.ogali.xenithlibrary.prompt.impl.impl.StringValueConditionPrompt;
import org.bukkit.entity.Player;

public class ConditionCreateListGuiItem extends GuiItem {

    public ConditionCreateListGuiItem(Class<? extends AbstractCondition<?, ?>> abstractConditionClass, String id, boolean inverted) {
        super(new ConditionCreateListItem(abstractConditionClass.getSimpleName(),
                inverted).build(), inventoryClickEvent -> {
            inventoryClickEvent.setCancelled(true);
            Player player = (Player) inventoryClickEvent.getWhoClicked();

            if (abstractConditionClass == ItemMatchCondition.class) {
                new ItemInputConditionMenu().show((Player) inventoryClickEvent.getWhoClicked(), new ItemMatchCondition(id, 0, inverted));
            } else if (abstractConditionClass == ItemDurabilityCondition.class) {
                new ItemInputConditionMenu().show(player, new ItemDurabilityCondition(id, 0, inverted));
            } else if (abstractConditionClass == ItemLoreMatchCondition.class) {
                new ItemInputConditionMenu().show(player, new ItemLoreMatchCondition(id, 0, inverted));
            } else if (abstractConditionClass == ItemLoreContainsCondition.class) {
                new StringValueConditionPrompt<ItemStackCondition<String>>(new ItemLoreContainsCondition(id, 0, inverted)).prompt(player);
            } else if (abstractConditionClass == ItemNameCondition.class) {
                new StringValueConditionPrompt<ItemStackCondition<String>>(new ItemNameCondition(id, 0, inverted)).prompt(player);
            } else if (abstractConditionClass == ItemMaterialCondition.class) {
                new StringValueConditionPrompt<ItemStackCondition<String>>(new ItemMaterialCondition(id, 0, inverted)).prompt(player);
            } else if (abstractConditionClass == BiomeCondition.class) {
                new StringValueConditionPrompt<LocationCondition<String>>(new BiomeCondition(id, 0, inverted)).prompt(player);
            }
        });
    }

}
