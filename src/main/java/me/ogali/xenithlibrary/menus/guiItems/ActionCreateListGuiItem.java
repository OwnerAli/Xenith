package me.ogali.xenithlibrary.menus.guiItems;

import me.despical.inventoryframework.GuiItem;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.menus.displayItems.ActionCreateListItem;
import me.ogali.xenithlibrary.prompt.impl.impl.DoubleValueActionPrompt;
import me.ogali.xenithlibrary.prompt.impl.impl.StringValueActionPrompt;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ActionCreateListGuiItem extends GuiItem {

    public ActionCreateListGuiItem(Class<? extends AbstractAction<?, ?>> abstractActionClass, String id) {
        super(new ActionCreateListItem(abstractActionClass.getSimpleName()).build(), inventoryClickEvent -> {
            inventoryClickEvent.setCancelled(true);
            Player player = (Player) inventoryClickEvent.getWhoClicked();

            try {
                Constructor<? extends AbstractAction<?, ?>> constructor = abstractActionClass.getConstructor(String.class);
                AbstractAction<?, ?> abstractAction = constructor.newInstance(id);
                abstractAction.setId(id);

                if (abstractAction.getValue() instanceof String) {
                    new StringValueActionPrompt<>((AbstractAction<?, String>) abstractAction).prompt(player);
                } else if (abstractAction.getValue() instanceof Double) {
                    new DoubleValueActionPrompt<>((AbstractAction<?, Double>) abstractAction).prompt(player);
                } else {
                    Chat.log("Error creating action: " + "Unknown value type");
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                Chat.log("&dError creating action: " + e);
            }
        });
    }

}