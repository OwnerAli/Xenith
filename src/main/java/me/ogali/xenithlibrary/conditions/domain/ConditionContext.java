package me.ogali.xenithlibrary.conditions.domain;

import me.ogali.xenithlibrary.context.Context;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class ConditionContext extends Context {

    public static ConditionContext of(Player player, Event event) {
        ConditionContext ctx = new ConditionContext();
        ctx.setPlayer(player);
        ctx.setBukkitEvent(event);
        return ctx;
    }

    public static ConditionContext of(Player player, Event event,
                                      ItemStack mainHand, ItemStack offHand) {
        return (ConditionContext) of(player, event)
                .withMainHandItem(mainHand)
                .withOffHandItem(offHand);
    }

    public static ConditionContext of(Player player, Event event,
                                      ItemStack mainHand, ItemStack offHand,
                                      Location location) {
        return (ConditionContext) of(player, event)
                .withMainHandItem(mainHand)
                .withOffHandItem(offHand)
                .withLocation(location);
    }

    public static ConditionContext of(Event event) {
        return of(null, event);
    }
}