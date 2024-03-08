package me.ogali.xenithlibrary.condition.domain;

import lombok.Getter;
import lombok.Setter;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.action.domain.impl.AbstractPlayerAction;
import me.ogali.xenithlibrary.action.domain.impl.impl.CancellableTypeAction;
import me.ogali.xenithlibrary.holder.impl.ActionHolder;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
@Setter
public abstract class AbstractCondition<K, V> implements Condition<K> {

    private final String id;
    private int priority;
    private boolean negate;
    private V value;

    private final ActionHolder passActionHolder;
    private final ActionHolder failActionHolder;

    protected AbstractCondition(String id, int priority, boolean negate) {
        this.id = id;
        this.priority = priority;
        this.negate = negate;
        this.passActionHolder = new ActionHolder();
        this.failActionHolder = new ActionHolder();
    }

    protected AbstractCondition(String id, int priority, boolean negate, V value) {
        this.id = id;
        this.priority = priority;
        this.negate = negate;
        this.value = value;
        this.passActionHolder = new ActionHolder();
        this.failActionHolder = new ActionHolder();
    }

    public abstract String getType();

    public abstract String getDisplayText();

    @Override
    public boolean isNegate() {
        return negate;
    }

    @Override
    public int compareTo(@NotNull Condition<?> o) {
        return Integer.compare(o.getPriority(), getPriority());
    }

    private void executePlayerActions(LivingEntity livingEntity, List<AbstractAction<?, ?>> actionsList) {
        actionsList.forEach(abstractAction -> {
            if (abstractAction instanceof AbstractPlayerAction<?> abstractPlayerAction) {
                abstractPlayerAction.execute(livingEntity);
            }
        });
    }

    private void executeCancellableActions(Cancellable cancellable, List<AbstractAction<?, ?>> actionsList) {
        actionsList.forEach(abstractAction -> {
            if (abstractAction instanceof CancellableTypeAction cancellableTypeAction) {
                cancellableTypeAction.execute(cancellable);
            }
        });
    }

}