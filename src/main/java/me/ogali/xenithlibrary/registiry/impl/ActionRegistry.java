package me.ogali.xenithlibrary.registiry.impl;

import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.registiry.domain.impl.AbstractMapRegistry;

public class ActionRegistry extends AbstractMapRegistry<String, AbstractAction<?, ?>> {

    @Override
    public void register(AbstractAction<?, ?> object) {
        getObjectMap().put(object.getId(), object);
    }

}
