package me.ogali.xenithlibrary.action.domain.impl;

import me.ogali.xenithlibrary.action.domain.AbstractAction;
import org.bukkit.entity.LivingEntity;

public abstract class AbstractPlayerAction<V> extends AbstractAction<LivingEntity, V> {

    public AbstractPlayerAction(String id, V value, double chance) {
        super(id, value, chance);
    }

    public AbstractPlayerAction(String id) {
        super(id);
    }

}
