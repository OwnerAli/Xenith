package me.ogali.xenithlibrary.conditions.domain;

import me.ogali.xenithlibrary.shared.Context;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class ConditionContext extends Context {

    public static ConditionContext of(Player player, Event event) {
        ConditionContext ctx = new ConditionContext();
        ctx.setPlayer(player);
        ctx.setBukkitEvent(event);
        return ctx;
    }

    public static ConditionContext of(Event event) {
        return of(null, event);
    }
}