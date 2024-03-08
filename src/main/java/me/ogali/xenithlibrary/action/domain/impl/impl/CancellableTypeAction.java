package me.ogali.xenithlibrary.action.domain.impl.impl;

import me.ogali.xenithlibrary.action.domain.AbstractAction;
import org.bukkit.event.Cancellable;

public abstract class CancellableTypeAction extends AbstractAction<Cancellable, Boolean> {

    public CancellableTypeAction(String id, Boolean value, double chance) {
        super(id, value, chance);
    }

    @Override
    public void execute(Cancellable type) {
        if (!isSuccessful(getChance())) return;
        type.setCancelled(getValue());
    }

}
