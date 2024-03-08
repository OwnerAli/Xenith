package me.ogali.xenithlibrary.holder;

import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.condition.impl.ItemStackCondition;
import me.ogali.xenithlibrary.condition.impl.impl.BiomeCondition;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public interface IConditionHolder {

    Set<AbstractCondition<?, ?>> conditionSet = new HashSet<>();

    default void addCondition(AbstractCondition<?, ?> condition) {
        conditionSet.add(condition);
    }

    default void removeCondition(AbstractCondition<?, ?> condition) {
        conditionSet.remove(condition);
    }

    /**
     * @param player the player
     * @param values any value required for the condition
     * @return evaluates all conditions in the list are evaluated as true, otherwise false
     */
    default boolean evaluateConditionsAndExecuteActions(Player player, List<AbstractAction<?, ?>> executedActionsList, Object... values) {
        ItemStack itemStack = null;
        Location location = null;

        for (Object value : values) {
            if (value instanceof ItemStack item) {
                itemStack = item;
            } else if (value instanceof Location loc) {
                location = loc;
            }
        }

        // Sort conditions from the lowest priority to highest
        List<AbstractCondition<?, ?>> sortedConditionList = conditionSet.stream().sorted(Comparator.reverseOrder()).toList();
        sortedConditionList.forEach(condition -> Chat.tell(player, "PRIORITY: " + condition.getPriority()));

        // Save all failed conditions to execute their failed actions after all conditions are evaluated to be false
        List<AbstractCondition<?,?>> failedConditionsList = new ArrayList<>();

        for (AbstractCondition<?, ?> abstractCondition : sortedConditionList) {
            if (abstractCondition instanceof ItemStackCondition<?> itemStackCondition) {
                if (itemStack == null) continue;
                if (itemStackCondition.evaluate(itemStack)) {
                    itemStackCondition.getPassActionHolder().execute(player, values);
                    executedActionsList.addAll(itemStackCondition.getPassActionHolder().getActionList());
                    return true;
                }
                failedConditionsList.add(abstractCondition);
            } else if (abstractCondition instanceof BiomeCondition locationCondition) {
                if (location == null) continue;
                if (locationCondition.evaluate(location)) {
                    locationCondition.getPassActionHolder().execute(player, values);
                    executedActionsList.addAll(locationCondition.getPassActionHolder().getActionList());
                    return true;
                }
                failedConditionsList.add(abstractCondition);
            }
            failedConditionsList.forEach(condition -> {
                executedActionsList.addAll(condition.getFailActionHolder().getActionList());
                condition.getFailActionHolder().execute(player, values);
            });
        }

        return sortedConditionList.isEmpty();
    }

    /**
     * @param player the Player
     * @param values any value required for the condition
     * @return true if all conditions in the list are evaluated as true, otherwise false
     */
    default boolean evaluateConditionsChainedAndExecuteActions(Player player, Object... values) {
        ItemStack itemStack = null;
        Location location = null;

        for (Object value : values) {
            if (value instanceof ItemStack item) {
                itemStack = item;
            } else if (value instanceof Location loc) {
                location = loc;
            }
        }

        // Sort conditions from the lowest priority to highest
        List<AbstractCondition<?, ?>> sortedConditionList = conditionSet.stream()
                .sorted(Comparator.reverseOrder())
                .toList();

        for (AbstractCondition<?, ?> abstractCondition : sortedConditionList) {
            if (abstractCondition instanceof ItemStackCondition<?> itemStackCondition && itemStack != null) {
                if (!itemStackCondition.evaluate(itemStack)) {
                    itemStackCondition.getFailActionHolder().execute(player, values);
                    return false;
                }
            } else if (abstractCondition instanceof BiomeCondition locationCondition && location != null) {
                if (!locationCondition.evaluate(location)) {
                    locationCondition.getFailActionHolder().execute(player, values);
                    return false;
                }
            }
        }

        // If all conditions passed, execute pass actions and return true
        for (AbstractCondition<?, ?> abstractCondition : sortedConditionList) {
            if (abstractCondition instanceof ItemStackCondition<?> itemStackCondition) {
                itemStackCondition.getPassActionHolder().execute(player, values);
            } else if (abstractCondition instanceof BiomeCondition locationCondition) {
                locationCondition.getPassActionHolder().execute(player, values);
            }
        }

        return true;
    }

}
