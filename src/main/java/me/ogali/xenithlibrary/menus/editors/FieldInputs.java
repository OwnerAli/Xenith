package me.ogali.xenithlibrary.menus.editors;

import lombok.experimental.UtilityClass;
import me.ogali.xenithlibrary.menus.editors.impl.BooleanInput;
import me.ogali.xenithlibrary.menus.editors.impl.CycleInput;
import me.ogali.xenithlibrary.menus.editors.impl.TextInput;
import org.bukkit.entity.Player;

import java.util.List;

@UtilityClass
public final class FieldInputs {
    public static FieldInput text(String field) {
        return new TextInput(field);
    }

    public static FieldInput bool(String field) {
        return new BooleanInput(field);
    }

    public static FieldInput cycle(String field, String... options) {
        return new CycleInput(field, options);
    }

    public static FieldInput cycle(String field, Enum<?>[] values) {
        return new CycleInput(field, values);
    }

    public static FieldInput cycle(String field, List<String> options) {
        return new CycleInput(field, options);
    }

    public static void resolve(Player player, FieldInputProvider target,
                               String field, Runnable onComplete) {
        find(target, field).handle(player, target, onComplete);
    }

    public static FieldInput find(FieldInputProvider target, String field) {
        return target.fieldInputs().stream()
                .filter(f -> f.field().equals(field))
                .findFirst()
                .orElse(FieldInputs.text(field));
    }
}