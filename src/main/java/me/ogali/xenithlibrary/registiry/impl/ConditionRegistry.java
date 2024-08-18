package me.ogali.xenithlibrary.registiry.impl;

import de.leonhard.storage.Json;
import lombok.Getter;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import me.ogali.xenithlibrary.files.impl.ConditionsFile;
import me.ogali.xenithlibrary.registiry.domain.impl.AbstractMapRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class ConditionRegistry extends AbstractMapRegistry<String, AbstractCondition<?, ?>> {

    private final List<Class<? extends AbstractCondition<?, ?>>> conditionTypesList = new ArrayList<>();

    @Override
    public void register(AbstractCondition<?, ?> abstractCondition) {
        getObjectMap().put(abstractCondition.getId(), abstractCondition);
    }

    @Override
    public void saveToFile() {
        ConditionsFile conditionsFile = XenithLibrary.getInstance().getConditionsFile();

        getObjectMap().forEach((id, condition) -> {
            conditionsFile.setPathPrefix(id);
            conditionsFile.setDefault("condition", condition);
            conditionsFile.set("passActions", condition.getPassActionHolder().toIdList());
            conditionsFile.set("failActions", condition.getFailActionHolder().toIdList());
        });
    }

    @Override
    public void loadFromFile(Json file) {
        if (!(file instanceof ConditionsFile conditionsFile)) return;

        file.singleLayerKeySet()
                .forEach(key -> register(conditionsFile.getCondition(key)));
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
