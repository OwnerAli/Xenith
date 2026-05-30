package me.ogali.xenithlibrary.action.domain;

import lombok.Getter;
import lombok.Setter;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.menus.editors.FieldInputProvider;
import me.ogali.xenithlibrary.shared.Persistable;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public abstract class AbstractAction implements Action, Persistable, FieldInputProvider {
    private String id;
    private String typeKey;
    private double chance = 100.0;

    @Override
    public void persist() {
        ActionRegistry.register(this);
    }

    protected boolean rolledSuccessfully() {
        return XenithLibrary.getInstance().getRandom().nextDouble() * 100.0 <= chance;
    }

    @Override
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
    @Override
    public void applyEdit(String field, String value) {
        if (field.equals("chance")) {
            setChance(Double.parseDouble(value));
            return;
        }
        throw new IllegalArgumentException("Unknown field: " + field);
    }
}