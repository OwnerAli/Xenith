package me.ogali.xenithlibrary.events;

import me.ogali.xenithlibrary.XenithLibrary;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CustomEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public void call() {
        Bukkit.getScheduler()
                .runTask(XenithLibrary.getInstance(), () -> Bukkit.getServer().getPluginManager().callEvent(this));
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

}
