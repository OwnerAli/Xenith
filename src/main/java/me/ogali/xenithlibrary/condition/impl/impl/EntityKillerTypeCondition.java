package me.ogali.xenithlibrary.condition.impl.impl;

import me.ogali.xenithlibrary.condition.impl.EntityCondition;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class EntityKillerTypeCondition extends EntityCondition<String> {

    public EntityKillerTypeCondition(String id, int priority, boolean negate) {
        super(id, priority, negate);
        setValue("PLAYER");
    }

    public EntityKillerTypeCondition(String id, int priority, boolean negate, String value) {
        super(id, priority, negate, value);
    }

    @Override
    public String getType() {
        return "stringMatch";
    }

    @Override
    public String getDisplayText() {
        return "Entity matches";
    }

    @Override
    public boolean evaluate(Entity input) {
        if (!(input instanceof Player player)) return false;
        if (player.getKiller() == null) return false;
        try {
            return player.getKiller().getType().equals(EntityType.valueOf(getValue())) != isNegate();
        } catch (IllegalArgumentException ignored) {
            Chat.log("&cInvalid entity name configured for this condition.");
            return false;
        }
    }

}