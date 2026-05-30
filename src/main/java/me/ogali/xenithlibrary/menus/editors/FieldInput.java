package me.ogali.xenithlibrary.menus.editors;

import me.ogali.xenithlibrary.shared.Persistable;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public interface FieldInput {

    String field();

    Material icon();

    String hint();

    /**
     * Triggers the edit interaction.
     * target is whatever owns the field — condition, action, or anything else.
     * onComplete is called after the edit — caller rebuilds their menu.
     */
    void handle(Player player, FieldInputProvider target, Runnable onComplete);

    // FieldInput.java — add this default method
    default void persistIfAble(FieldInputProvider target) {
        if (!(target instanceof Persistable persistable)) return;
        persistable.persist();
    }
}