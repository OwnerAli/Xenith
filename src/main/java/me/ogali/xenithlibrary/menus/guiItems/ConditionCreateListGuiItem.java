package me.ogali.xenithlibrary.menus.guiItems;

import me.despical.inventoryframework.GuiItem;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.condition.impl.ItemStackCondition;
import me.ogali.xenithlibrary.menus.conditions.ItemInputConditionMenu;
import me.ogali.xenithlibrary.menus.displayItems.ConditionCreateListItem;
import me.ogali.xenithlibrary.prompt.impl.impl.ConditionValuePrompt;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ConditionCreateListGuiItem extends GuiItem {

    public ConditionCreateListGuiItem(Class<? extends AbstractCondition<?, ?>> abstractConditionClass, String id, boolean inverted) {
        super(new ConditionCreateListItem(abstractConditionClass.getSimpleName(),
                inverted).build(), inventoryClickEvent -> {
            inventoryClickEvent.setCancelled(true);
            Player player = (Player) inventoryClickEvent.getWhoClicked();

            try {
                Constructor<? extends AbstractCondition<?, ?>> constructor = abstractConditionClass.getConstructor(String.class, int.class, boolean.class);
                AbstractCondition<?, ?> abstractCondition = constructor.newInstance(id, 0, false);
                abstractCondition.setId(id);

                if (abstractCondition.getValue() instanceof ItemStack
                        || abstractCondition.getValue() instanceof List<?>) {
                    new ItemInputConditionMenu().show(player, (ItemStackCondition<ItemStack>) abstractCondition);
                } else {
                    new ConditionValuePrompt(player, abstractCondition).prompt(player);
                }

//                if (abstractCondition.getValue() instanceof String) {
//                    new StringValueConditionPrompt<>((AbstractCondition<?, String>) abstractCondition).prompt(player);
//                } else if (abstractCondition.getValue() instanceof Double) {
//                    new DoubleValueConditionPrompt<>((AbstractCondition<?, Double>) abstractCondition).prompt(player);
//                } else if (abstractCondition.getKey() instanceof Location && abstractCondition.getValue() instanceof Integer) {
//                    new ConditionValuePrompt(abstractCondition).prompt(player);
//                } else if (abstractCondition.getValue() instanceof ItemStack || abstractCondition.getValue() instanceof Integer
//                        || abstractCondition.getValue() instanceof List<?>) {
//                    new ItemInputConditionMenu().show(player, (ItemStackCondition<ItemStack>) abstractCondition);
//                } else {
//                    Chat.log("Error creating condition: " + "Unknown value type");
//                }
            } catch (InstantiationException | IllegalAccessException |
                     NoSuchMethodException | InvocationTargetException e) {
                Chat.log("&dError creating condition: " + e);
            }
        });
    }

}