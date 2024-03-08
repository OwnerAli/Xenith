package me.ogali.xenithlibrary.menus.guiItems;

import me.despical.inventoryframework.GuiItem;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.menus.actions.ConditionActionAddMenu;
import me.ogali.xenithlibrary.menus.displayItems.ActionCreateListItem;
import org.bukkit.entity.Player;

import java.util.List;

public class ActionAddToConditionItem extends GuiItem {

    public ActionAddToConditionItem(Player player, AbstractCondition<?, ?> condition, AbstractAction<?, ?> action, boolean addToPassList) {
        super(new ActionCreateListItem(action, condition, addToPassList).build(),
                click -> {
                    click.setCancelled(true);

                    if (addToPassList) {
                        List<AbstractAction<?, ?>> conditionPassActionList = condition.getPassActionHolder().getActionList();

                        if (conditionPassActionList.contains(action)) {
                            conditionPassActionList.remove(action);
                        } else {
                            conditionPassActionList.add(action);
                        }

                        new ConditionActionAddMenu().show(player, condition, addToPassList);
                        return;
                    }

                    List<AbstractAction<?, ?>> conditionFailActionList = condition.getFailActionHolder().getActionList();

                    if (conditionFailActionList.contains(action)) {
                        conditionFailActionList.remove(action);
                    } else {
                        conditionFailActionList.add(action);
                    }

                    new ConditionActionAddMenu().show(player, condition, addToPassList);
                });
    }

}
