package me.ogali.xenithlibrary.registiry.impl;

import lombok.Getter;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.registiry.domain.impl.AbstractMapRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class ConditionRegistry extends AbstractMapRegistry<String, AbstractCondition<?, ?>> {

    private final List<Class<? extends AbstractCondition<?, ?>>> conditionTypes = new ArrayList<>();


    @Override
    public void register(AbstractCondition<?, ?> abstractCondition) {
        getObjectMap().put(abstractCondition.getId(), abstractCondition);
    }

    /**
     * Retrieves a collection of all registered AbstractConditions.
     *
     * @return A collection of AbstractConditions stored in the registry.
     * @apiNote This method is part of the public API for external use.
     */
    public Collection<AbstractCondition<?, ?>> getRegisteredConditions() {
        return getObjectMap().values();
    }

}
