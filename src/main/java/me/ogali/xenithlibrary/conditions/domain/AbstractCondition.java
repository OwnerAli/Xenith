package me.ogali.xenithlibrary.conditions.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.ogali.xenithlibrary.conditions.evaluator.Evaluator;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractCondition implements Condition {
    private String id;
    protected Evaluator evaluator;
}
