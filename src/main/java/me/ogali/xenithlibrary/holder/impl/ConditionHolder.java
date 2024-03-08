package me.ogali.xenithlibrary.holder.impl;

import lombok.Getter;
import lombok.Setter;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.holder.IConditionHolder;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ConditionHolder implements IConditionHolder {

    private boolean chain;

    public Set<AbstractCondition<?, ?>> getConditionSet() {
        return conditionSet;
    }

    public final boolean checkAllConditionsAndExecuteActions(Player player, List<AbstractAction<?, ?>> list, Object... values) {
        if (chain) {
            return evaluateConditionsChainedAndExecuteActions(player, values);
        }
        return evaluateConditionsAndExecuteActions(player, list, values);
    }

}
