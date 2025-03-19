package me.ogali.xenithlibrary.condition.impl;

import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import org.bukkit.entity.Entity;

public abstract class EntityCondition<V> extends AbstractCondition<Entity, V> {

    public EntityCondition(String id, int priority, boolean negate, V value) {
        super(id, priority, negate, value);
    }

    public EntityCondition(String id, int priority, boolean negate) {
        super(id, priority, negate);
        setKey(XenithLibrary.getInstance().getServer().getWorlds().get(0)
                .getEntities()
                .get(0));
    }

    @Override
    public String getType() {
        return "entity";
    }

    @Override
    public String getDisplayText() {
        return "&fEntity matches";
    }

}
