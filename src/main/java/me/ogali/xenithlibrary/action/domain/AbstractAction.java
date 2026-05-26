package me.ogali.xenithlibrary.action.domain;

import lombok.Getter;
import lombok.Setter;
import me.ogali.xenithlibrary.XenithLibrary;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public abstract class AbstractAction implements Action {
    private String id;
    private String typeKey;
    private double chance = 100.0;

    /**
     * Subclasses call this at the top of execute() to respect the chance field.
     * <p>
     * Usage:
     * if (!rolledSuccessfully()) return;
     */
    protected boolean rolledSuccessfully() {
        return XenithLibrary.getInstance().getRandom().nextDouble() * 100.0 <= chance;
    }

    /**
     * Serializes this action to a map for persistence.
     * Subclasses override and call super() to include base fields, then add their own.
     */
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("type", typeKey);
        data.put("chance", chance);
        return data;
    }

    /**
     * Applies a raw string edit to a named field.
     * Subclasses override to handle their own fields.
     * Base class handles "chance".
     */
    public void applyEdit(String field, String value) {
        if (field.equals("chance")) {
            setChance(Double.parseDouble(value));
            return;
        }
        throw new IllegalArgumentException("Unknown field: " + field);
    }
}