package me.ogali.xenithlibrary.conditions.domain;

import lombok.Getter;
import lombok.ToString;
import me.ogali.xenithlibrary.shared.Context;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
public class ConditionHolder {
    private final List<Condition> conditions;

    public ConditionHolder(Condition... conditions) {
        this.conditions = new ArrayList<>(List.of(conditions));
    }

    public ConditionHolder(List<Condition> conditions) {
        this.conditions = new ArrayList<>(conditions);
    }

    public void populate(ConditionHolder conditionHolder) {
        this.conditions.addAll(conditionHolder.getConditions());
    }

    public boolean checkAll(Context context) {
        return conditions.stream().allMatch(condition -> condition.test(context));
    }
}

