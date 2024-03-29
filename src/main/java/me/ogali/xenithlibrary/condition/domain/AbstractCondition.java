package me.ogali.xenithlibrary.condition.domain;

import lombok.Getter;
import lombok.Setter;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.files.impl.ConditionsFile;
import me.ogali.xenithlibrary.holder.impl.ActionHolder;
import org.jetbrains.annotations.NotNull;

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

    public void saveToFile() {
        ConditionsFile file = XenithLibrary.getInstance().getConditionsFile();
        file.set(id + ".condition", toString());
        file.set(id + ".passActions", passActionHolder.toIdList());
        file.set(id + ".failActions", failActionHolder.toIdList());
    }

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
        return getClass().getSimpleName() + " " + (isNegate() ? getPriority() + " != " + getValue() : getPriority() + " == " + getValue());
    }

}