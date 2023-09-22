package me.ogali.xenithlibrary.holder;

import lombok.Setter;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.condition.impl.ItemStackCondition;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ConditionHolder {

    private final List<AbstractCondition<?, ?>> conditionList;

    @Setter
    private boolean chain;

    public ConditionHolder() {
        this.conditionList = new ArrayList<>();
    }

    public void addCondition(AbstractCondition<?, ?> condition) {
        conditionList.add(condition);
    }

    public final boolean checkAllConditionsAndExecuteActions(Player player, Object... values) {
        if (chain) {
            return evaluateItemStackConditionsChainedAndExecuteActions(player, values);
        }
        return evaluateConditionsAndExecuteActions(player, values);
    }

    public final boolean checkAllConditionsAndExecuteFailActions(Player player, Object... values) {
        if (chain) {
            return evaluateItemStackConditionsChainedAndExecuteFailActions(player, values);
        }
        return evaluateConditionsAndExecuteFailActions(player, values);
    }

    /**
     * @param player the player
     * @param values any value required for the condition
     * @return evaluates all conditions in the list are evaluated as true, otherwise false
     */
    private boolean evaluateConditionsAndExecuteActions(Player player, Object... values) {
        ItemStack itemStack = null;

        for (Object value : values) {
            if (value instanceof ItemStack item) {
                itemStack = item;
            }
        }

        // Sort conditions from the lowest priority to highest
        List<AbstractCondition<?, ?>> sortedConditionList = conditionList.stream().sorted(Comparator.reverseOrder()).toList();

        for (AbstractCondition<?, ?> abstractCondition : sortedConditionList) {
            if (abstractCondition instanceof ItemStackCondition<?> itemStackCondition) {
                if (itemStack == null) return false;
                if (itemStackCondition.evaluate(itemStack, player)) {
                    itemStackCondition.executePassActions(player, values);
                    return true;
                }
                itemStackCondition.executeFailActions(player, values);
                return false;
            }
        }
        return true;
    }

    /**
     * @param player the player
     * @param values any value required for the condition
     * @return evaluates all conditions in the list are evaluated as true, otherwise false
     */
    private boolean evaluateConditionsAndExecuteFailActions(Player player, Object... values) {
        ItemStack itemStack = null;

        for (Object value : values) {
            if (value instanceof ItemStack item) {
                itemStack = item;
            }
        }

        // Sort conditions from the lowest priority to highest
        List<AbstractCondition<?, ?>> sortedConditionList = conditionList.stream().sorted(Comparator.reverseOrder()).toList();

        for (AbstractCondition<?, ?> abstractCondition : sortedConditionList) {
            if (abstractCondition instanceof ItemStackCondition<?> itemStackCondition) {
                if (itemStack == null) return false;
                if (itemStackCondition.evaluate(itemStack, player)) return true;
                itemStackCondition.executeFailActions(player, values);
                return false;
            }
        }
        return true;
    }

    /**
     * @param player the Player
     * @param values any value required for the condition
     * @return true if all conditions in the list are evaluated as true, otherwise false
     */
    private boolean evaluateItemStackConditionsChainedAndExecuteActions(Player player, Object... values) {
        ItemStack itemStack = null;

        for (Object value : values) {
            if (value instanceof ItemStack item) {
                itemStack = item;
            }
        }

        // Sort conditions from the lowest priority to highest
        List<AbstractCondition<?, ?>> sortedConditionList = conditionList.stream().sorted(Comparator.reverseOrder()).toList();

        for (AbstractCondition<?, ?> abstractCondition : sortedConditionList) {
            if (abstractCondition instanceof ItemStackCondition<?> itemStackCondition) {
                if (itemStack == null) return false;
                if (!itemStackCondition.evaluate(itemStack, player)) {
                    itemStackCondition.executeFailActions(player, values);
                    return false;
                }
                itemStackCondition.executePassActions(player, values);
            }
        }
        return true;
    }

    /**
     * @param player the Player
     * @param values any value required for the condition
     * @return true if all conditions in the list are evaluated as true, otherwise false
     */
    private boolean evaluateItemStackConditionsChainedAndExecuteFailActions(Player player, Object... values) {
        ItemStack itemStack = null;

        for (Object value : values) {
            if (value instanceof ItemStack item) {
                itemStack = item;
            }
        }

        // Sort conditions from the lowest priority to highest
        List<AbstractCondition<?, ?>> sortedConditionList = conditionList.stream().sorted(Comparator.reverseOrder()).toList();

        for (AbstractCondition<?, ?> abstractCondition : sortedConditionList) {
            if (abstractCondition instanceof ItemStackCondition<?> itemStackCondition) {
                if (itemStack == null) return false;
                if (!itemStackCondition.evaluate(itemStack, player)) {
                    itemStackCondition.executeFailActions(player, values);
                    return false;
                }
            }
        }
        return true;
    }

}
