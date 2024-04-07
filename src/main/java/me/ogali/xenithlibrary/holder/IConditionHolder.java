package me.ogali.xenithlibrary.holder;

import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import org.bukkit.entity.Player;

import java.util.List;

public interface IConditionHolder {

    void addCondition(AbstractCondition<?, ?> condition);

    void removeCondition(AbstractCondition<?, ?> condition);

    /**
     * @param player the player
     * @param values any value required for the condition
     * @return evaluates all conditions in the list are evaluated as true, otherwise false
     */
    boolean evaluateConditionsAndExecuteActions(Player player, List<AbstractAction<?, ?>> executedActionsList, Object... values);

    /**
     * @param player the Player
     * @param values any value required for the condition
     * @return true if all conditions in the list are evaluated as true, otherwise false
     */
    boolean evaluateConditionsChainedAndExecuteActions(Player player, Object... values);

}
