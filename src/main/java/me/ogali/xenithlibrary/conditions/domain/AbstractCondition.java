package me.ogali.xenithlibrary.conditions.domain;

import lombok.Getter;
import lombok.Setter;
import me.ogali.xenithlibrary.conditions.evaluator.Evaluator;
import me.ogali.xenithlibrary.menus.editors.FieldInputProvider;
import me.ogali.xenithlibrary.shared.Persistable;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public abstract class AbstractCondition implements Condition, Persistable, FieldInputProvider {
    private String id;
    private String typeKey;
    private Evaluator evaluator;

    @Override
    public void persist() {
        ConditionRegistry.register(this);
    }

    /**
     * Convenience method for subclasses that need to compare values.
     * Falls back to EQUAL if no evaluator was configured.
     */
    protected boolean evaluate(String actual, String expected) {
        Evaluator ev = evaluator != null ? evaluator : Evaluator.EQUAL;
        return ev.evaluate(actual, expected);
    }

    /**
     * Serializes this condition to a map for persistence.
     * Subclasses override and call super() to include base fields, then add their own.
     */
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("type", typeKey);
        if (id != null) data.put("id", id);
        if (evaluator != null) data.put("evaluator", evaluator.name());
        return data;
    }

    /**
     * Applies a raw string edit to a named field.
     * Subclasses override to handle their own fields.
     * this keeps the pattern consistent with AbstractAction.
     */
    @Override
    public void applyEdit(String field, String value) {
        if (field.equals("evaluator")) {
            setEvaluator(Evaluator.valueOf(value.toUpperCase()));
            return;
        }
        throw new IllegalArgumentException("Unknown field: '" + field + "' on " + getTypeKey());
    }
}