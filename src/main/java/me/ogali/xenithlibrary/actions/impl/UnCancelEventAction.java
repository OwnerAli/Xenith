package me.ogali.xenithlibrary.actions.impl;

import me.ogali.xenithlibrary.actions.domain.AbstractAction;
import me.ogali.xenithlibrary.actions.domain.ActionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.event.Cancellable;

public class UnCancelEventAction extends AbstractAction {

    @Override
    public void execute(ActionContext context) {
        if (!rolledSuccessfully()) return;
        if (!(context.getBukkitEvent() instanceof Cancellable cancellable)) return;
        cancellable.setCancelled(false);
    }

    public static AbstractAction fromConfig(DomainConfig config) {
        UnCancelEventAction action = new UnCancelEventAction();
        action.setChance(config.getDouble("chance", 100.0));
        return action;
    }
}