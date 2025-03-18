package me.ogali.xenithlibrary.events;

import lombok.Getter;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import org.bukkit.entity.Player;

@Getter
public class ActionCreatedEvent extends CustomEvent {

    private final Player player;
    private final AbstractAction<?, ?> action;

    public ActionCreatedEvent(Player player, AbstractAction<?, ?> action) {
        this.player = player;
        this.action = action;
    }

}
