package me.ogali.xenithlibrary.files.impl;

import me.ogali.xenithlibrary.action.domain.AbstractAction;

public class ActionsFile extends XenithJsonFile<AbstractAction<?, ?>> {

    public ActionsFile() {
        super("actions");
    }

    @Override
    public void save(AbstractAction<?, ?> object) {

    }

    @Override
    public void load() {

    }

}
