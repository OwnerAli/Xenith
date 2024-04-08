package me.ogali.xenithlibrary.holder.impl;

import lombok.Getter;
import lombok.Setter;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.holder.AbstractConditionHolder;
import me.ogali.xenithlibrary.registiry.impl.ConditionRegistry;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
@Setter
public class ConditionHolder extends AbstractConditionHolder {

    private boolean chain;

    public final boolean checkAllConditionsAndExecuteActions(Player player, List<AbstractAction<?, ?>> list, Object... values) {
        if (chain) {
            return evaluateConditionsChainedAndExecuteActions(player, values);
        }
        return evaluateConditionsAndExecuteActions(player, list, values);
    }

    public void populateWithConditionsFromIdList(List<String> idList) {
        ConditionRegistry registry = XenithLibrary.getInstance()
                .getRegistryManager()
                .getRegistry(ConditionRegistry.class);

        idList.forEach(id -> registry.get(id).ifPresent(getConditionSet()::add));
    }

    public List<String> toIdList() {
        return getConditionSet().stream().map(AbstractCondition::getId).toList();
    }

}