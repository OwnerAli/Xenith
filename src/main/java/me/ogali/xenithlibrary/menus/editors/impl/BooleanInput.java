package me.ogali.xenithlibrary.menus.editors.impl;

import me.ogali.xenithlibrary.menus.editors.FieldInput;
import me.ogali.xenithlibrary.menus.editors.FieldInputProvider;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BooleanInput implements FieldInput {
    private final String field;

    public BooleanInput(String field) {
        this.field = field;
    }

    @Override
    public String field() {
        return field;
    }

    @Override
    public Material icon() {
        return Material.LEVER;
    }

    @Override
    public String hint() {
        return "&7Click to toggle";
    }

    @Override
    public void handle(Player player, FieldInputProvider target, Runnable onComplete) {
        Object current = target.serialize().get(field);
        String next = Boolean.toString(!"true".equalsIgnoreCase(String.valueOf(current)));
        try {
            target.applyEdit(field, next);
            persistIfAble(target);
            Chat.tellFormatted(player, "&aToggled &e%s &ato: &f%s", field, next);
        } catch (Exception ex) {
            Chat.tellFormatted(player, "&cFailed to toggle &e%s&c: &f%s", field, ex.getMessage());
        }
        onComplete.run();
    }
}