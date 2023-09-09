package me.ogali.xenithlibrary.action.domain.impl.impl.impl;

import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.action.domain.impl.impl.CancellableTypeAction;
import org.bukkit.event.Cancellable;

public class UnCancelEventAction extends CancellableTypeAction {

    public UnCancelEventAction() {
        super("unCancelEventAction", false);
    }

}
