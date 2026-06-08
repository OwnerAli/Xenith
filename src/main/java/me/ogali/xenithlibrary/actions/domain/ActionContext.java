package me.ogali.xenithlibrary.actions.domain;

import me.ogali.xenithlibrary.context.Context;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class ActionContext extends Context {

    public ActionContext() {
        super();
    }

    public static ActionContext of(Player player, Event event) {
        ActionContext ctx = new ActionContext();
        ctx.setPlayer(player);
        ctx.setBukkitEvent(event);
        return ctx;
    }

    public static ActionContext of(Player player, Event event,
                                   ItemStack mainHand, ItemStack offHand) {
        return (ActionContext) of(player, event)
                .withMainHandItem(mainHand)
                .withOffHandItem(offHand);
    }

    public static ActionContext of(Player player, Event event,
                                   ItemStack mainHand, ItemStack offHand,
                                   Location location) {
        return (ActionContext) of(player, event)
                .withMainHandItem(mainHand)
                .withOffHandItem(offHand)
                .withLocation(location);
    }

    public static ActionContext of(Player player) {
        return of(player, null);
    }
}