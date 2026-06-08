package me.ogali.xenithlibrary.actions.domain;

/**
 * An action that runs against a context.
 * Mirrors Condition — testable, composable, simple.
 */
@FunctionalInterface
public interface Action {
    void execute(ActionContext context);

    default Action andThen(Action next) {
        return context -> {
            this.execute(context);
            next.execute(context);
        };
    }
}