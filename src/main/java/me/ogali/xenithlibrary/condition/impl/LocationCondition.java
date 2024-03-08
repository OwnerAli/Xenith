package me.ogali.xenithlibrary.condition.impl;

import me.ogali.xenithlibrary.condition.domain.AbstractCondition;
import org.bukkit.Location;

public abstract class LocationCondition<T> extends AbstractCondition<Location, T> {

    public LocationCondition(String id, int priority, boolean negate, T value) {
        super(id, priority, negate, value);
    }

    public LocationCondition(String id, int priority, boolean negate) {
        super(id, priority, negate);
    }

}
