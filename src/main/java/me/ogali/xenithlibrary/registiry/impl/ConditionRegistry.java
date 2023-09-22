package me.ogali.xenithlibrary.registiry.impl;

import lombok.Getter;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.registiry.domain.impl.AbstractMapRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class ConditionRegistry extends AbstractMapRegistry<AbstractCondition<?, ?>, String> {

    private final List<Class<? extends AbstractCondition<?, ?>>> conditionTypes = new ArrayList<>();

    @Override
    public void register(AbstractCondition<?, ?> abstractCondition) {
        getObjectMap().put(abstractCondition.getId(), abstractCondition);
    }

    @Override
    public void unRegister(String id) {
        getObjectMap().remove(id);
    }

    @Override
    public Optional<AbstractCondition<?, ?>> get(String id) {
        return Optional.ofNullable(getObjectMap().get(id));
    }

}
