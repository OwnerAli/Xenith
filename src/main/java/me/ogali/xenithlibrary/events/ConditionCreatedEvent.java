package me.ogali.xenithlibrary.events;

import lombok.Getter;
import me.ogali.xenithlibrary.condition.domain.Condition;
import org.bukkit.entity.Player;

@Getter
public class ConditionCreatedEvent extends CustomEvent {

    private final Player player;
    private final Condition<?> condition;

    public ConditionCreatedEvent(Player player, Condition<?> condition) {
        this.player = player;
        this.condition = condition;
    }

}
