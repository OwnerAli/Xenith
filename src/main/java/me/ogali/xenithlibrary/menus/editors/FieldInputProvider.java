package me.ogali.xenithlibrary.menus.editors;

import java.util.List;
import java.util.Map;

/**
 * Implemented by anything that has editable fields — conditions, actions, etc.
 * Declares how each field should be edited in the GUI.
 */
public interface FieldInputProvider {

    /**
     * Applies a raw string edit to a named field.
     */
    void applyEdit(String field, String value);

    /**
     * Returns the current serialized value of a field.
     */
    Map<String, Object> serialize();

    /**
     * Declares the input type for each field. Defaults to TEXT for unlisted fields.
     */
    default List<FieldInput> fieldInputs() {
        return List.of();
    }
}