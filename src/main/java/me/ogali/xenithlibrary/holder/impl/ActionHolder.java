package me.ogali.xenithlibrary.holder.impl;

import me.ogali.xenithlibrary.holder.AbstractActionHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class ActionHolder extends AbstractActionHolder {

    @Override
    public void execute(Player player, Object... values) {
        executePlayerActions(player);
        for (Object value : values) {
            if (value instanceof Cancellable cancellable) {
                executeCancellableActions(cancellable);
            }
        }
    }

}
