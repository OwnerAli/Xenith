package me.ogali.xenithlibrary.registiry.impl;

import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.registiry.domain.impl.AbstractMapRegistry;

import java.util.Optional;

public class ActionRegistry extends AbstractMapRegistry<AbstractAction, String> {

    @Override
    public void register(AbstractAction abstractAction) {
    }

    @Override
    public void unRegister(String object) {

    }

    @Override
    public Optional<AbstractAction> get(String object) {
        return Optional.empty();
    }

}
