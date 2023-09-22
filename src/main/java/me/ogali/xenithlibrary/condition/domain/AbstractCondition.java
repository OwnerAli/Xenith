package me.ogali.xenithlibrary.condition.domain;

import lombok.Getter;
import lombok.Setter;
import me.ogali.xenithlibrary.action.domain.AbstractAction;
import me.ogali.xenithlibrary.action.domain.impl.AbstractPlayerAction;
import me.ogali.xenithlibrary.action.domain.impl.impl.CancellableTypeAction;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class AbstractCondition<K, V> implements Condition<K> {

    private final String id;
    private final int priority;
    private final boolean negate;
    private final V value;

    private final List<AbstractAction<?, ?>> conditionPassActionList;
    private final List<AbstractAction<?, ?>> conditionFailActionList;

    protected AbstractCondition(String id, int priority, boolean negate, V value) {
        this.id = id;
        this.priority = priority;
        this.negate = negate;
        this.value = value;
        this.conditionPassActionList = new ArrayList<>();
        this.conditionFailActionList = new ArrayList<>();
    }

    public void executePassActions(Player player, Object... values) {
        executePlayerActions(player, conditionPassActionList);
        for (Object value : values) {
            if (value instanceof Cancellable cancellable) {
                executeCancellableActions(cancellable, conditionPassActionList);
            }
        }
    }

    public void executeFailActions(Player player, Object... values) {
        executePlayerActions(player, conditionFailActionList);
        for (Object value : values) {
            if (value instanceof Cancellable cancellable) {
                executeCancellableActions(cancellable, conditionFailActionList);
            }
        }
    }

    public abstract String getType();

    public abstract String getDisplayText();

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
