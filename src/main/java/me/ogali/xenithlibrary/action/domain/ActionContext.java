package me.ogali.xenithlibrary.action.domain;

import me.ogali.xenithlibrary.shared.Context;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

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

    public static ActionContext of(Player player) {
        return of(player, null);
    }
}