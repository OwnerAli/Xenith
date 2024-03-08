package me.ogali.xenithlibrary.registiry.impl;

import lombok.Getter;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.registiry.domain.impl.AbstractMapRegistry;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ActionRegistry extends AbstractMapRegistry<String, AbstractAction<?, ?>> {

    private final List<Class<? extends AbstractAction<?, ?>>> actionTypesList = new ArrayList<>();

    @Override
    public void register(AbstractAction<?, ?> object) {
        getObjectMap().put(object.getId(), object);
    }

    public void registerActionType(Class<? extends AbstractAction<?, ?>> abstractActionClass) {
        actionTypesList.add(abstractActionClass);
    }

}
