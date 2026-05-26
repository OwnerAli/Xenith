package me.ogali.xenithlibrary.action.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds a list of actions and executes them in order.
 */
public final class ActionHolder {

    private final List<Action> actions;

    public ActionHolder(List<Action> actions) {
        this.actions = new ArrayList<>(actions);
    }

    public ActionHolder(Action... actions) {
        this(List.of(actions));
    }

    public static ActionHolder empty() {
        return new ActionHolder();
    }

    public void executeAll(ActionContext context) {
        actions.forEach(a -> a.execute(context));
    }

    public boolean isEmpty() {
        return actions.isEmpty();
    }
}