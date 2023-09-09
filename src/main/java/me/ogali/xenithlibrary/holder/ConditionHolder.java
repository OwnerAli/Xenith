package me.ogali.xenithlibrary.holder;

import lombok.Setter;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.condition.impl.ItemStackCondition;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ConditionHolder {

    private final List<AbstractCondition<?, ?>> conditionList;

    @Setter
    private boolean singleConditionResult;

    public ConditionHolder() {
        this.conditionList = new ArrayList<>();
    }

    public void addCondition(AbstractCondition<?, ?> condition) {
        conditionList.add(condition);
    }

    public boolean checkAllConditionsInorder(Player player, ItemStack itemStack) {
        if (singleConditionResult) {
            Optional<AbstractCondition<?, ?>> lowestPriorityCondition = conditionList.stream().max(Comparator.naturalOrder());
            if (lowestPriorityCondition.isEmpty()) return false;
            if (lowestPriorityCondition.get() instanceof ItemStackCondition<?> itemStackCondition) {
                itemStackCondition.evaluate(itemStack, player);
                return true;
            }
            return false;
        }
        for (AbstractCondition<?, ?> abstractCondition : conditionList) {
            if (abstractCondition instanceof ItemStackCondition<?> itemStackCondition) {
                if (!itemStackCondition.evaluate(itemStack, player)) return false;
            }
        }
        return true;
    }

}
