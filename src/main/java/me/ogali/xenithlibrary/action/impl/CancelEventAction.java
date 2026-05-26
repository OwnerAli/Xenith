package me.ogali.xenithlibrary.action.impl;

import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.action.domain.ActionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;
import org.bukkit.event.Cancellable;

public class CancelEventAction extends AbstractAction {

    @Override
    public void execute(ActionContext context) {
        if (!rolledSuccessfully()) return;
        if (!(context.getBukkitEvent() instanceof Cancellable cancellable)) return;
        cancellable.setCancelled(true);
    }

    public static AbstractAction fromConfig(DomainConfig config) {
        CancelEventAction action = new CancelEventAction();
        action.setChance(config.getDouble("chance", 100.0));
        return action;
    }
}