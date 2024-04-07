package me.ogali.xenithlibrary.holder.impl;

import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.holder.AbstractActionHolder;
import me.ogali.xenithlibrary.registiry.impl.ActionRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.List;

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

    public void populateWithActionsFromIdList(List<String> idList) {
        ActionRegistry registry = XenithLibrary.getInstance()
                .getRegistryManager()
                .getRegistry(ActionRegistry.class);

        idList.forEach(id -> registry.get(id).ifPresent(getActionList()::add));
    }

}
