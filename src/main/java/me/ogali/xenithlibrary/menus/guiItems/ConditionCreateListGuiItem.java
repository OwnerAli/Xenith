package me.ogali.xenithlibrary.menus.guiItems;

import me.despical.inventoryframework.GuiItem;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.condition.impl.BiomeCondition;
import me.ogali.xenithlibrary.condition.impl.impl.*;
import me.ogali.xenithlibrary.menus.conditions.ItemInputConditionMenu;
import me.ogali.xenithlibrary.menus.displayItems.ConditionCreateListItem;
import me.ogali.xenithlibrary.prompt.impl.StringConditionPrompt;
import me.ogali.xenithlibrary.prompt.impl.StringValueItemStackConditionPrompt;
import org.bukkit.entity.Player;

public class ConditionCreateListGuiItem extends GuiItem {

    public ConditionCreateListGuiItem(Class<? extends AbstractCondition<?, ?>> abstractConditionClass, String id, int priority, boolean inverted) {
        super(new ConditionCreateListItem(abstractConditionClass.getSimpleName(),
                inverted).build(), inventoryClickEvent -> {
            inventoryClickEvent.setCancelled(true);
            Player player = (Player) inventoryClickEvent.getWhoClicked();

            if (abstractConditionClass == ItemMatchCondition.class) {
                new ItemInputConditionMenu().show((Player) inventoryClickEvent.getWhoClicked(), new ItemMatchCondition(id, priority, inverted));
            } else if (abstractConditionClass == ItemDurabilityCondition.class) {
                new ItemInputConditionMenu().show(player, new ItemDurabilityCondition(id, priority, inverted));
            } else if (abstractConditionClass == ItemLoreMatchCondition.class) {
                new ItemInputConditionMenu().show(player, new ItemLoreMatchCondition(id, priority, inverted));
            } else if (abstractConditionClass == ItemLoreContainsCondition.class) {
                new StringValueItemStackConditionPrompt(new ItemLoreContainsCondition(id, priority, inverted)).prompt(player);
            } else if (abstractConditionClass == ItemNameCondition.class) {
                new StringValueItemStackConditionPrompt(new ItemNameCondition(id, priority, inverted)).prompt(player);
            } else if (abstractConditionClass == BiomeCondition.class) {
                new StringConditionPrompt(new BiomeCondition(id, priority, inverted)).prompt(player);
            }
        });
    }

}
