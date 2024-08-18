package me.ogali.xenithlibrary.condition.domain;

import lombok.Getter;
import lombok.Setter;
import me.ogali.xenithlibrary.holder.impl.ActionHolder;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public abstract class AbstractCondition<K, V> implements Condition<K> {

    private String id;
    private int priority;
    private boolean negate;
    private K key;
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

    @Override
    public String toString() {
        return getClass().getName()
                + " " +
                (isNegate() ? getPriority() + " != " + getValue() :
                        getPriority() + " == " + getValue());
    }

}