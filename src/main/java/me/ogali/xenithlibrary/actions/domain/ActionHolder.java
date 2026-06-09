package me.ogali.xenithlibrary.actions.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds a list of actions and executes them in order.
 */
@Getter
public final class ActionHolder {
    private final List<AbstractAction> actions;

    public ActionHolder(List<AbstractAction> actions) {
        this.actions = new ArrayList<>(actions);
    }

    public ActionHolder(AbstractAction... actions) {
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

    public List<String> toIdList() {
        return actions.stream().map(AbstractAction::getId).toList();
    }

    public ActionHolder fromIdList(List<String> ids) {
        return new ActionHolder(
                ids.stream()
                        .map(ActionRegistry::get)
                        .toList()
        );
    }

    public ActionHolder copy() {
        return new ActionHolder(actions);
    }
}