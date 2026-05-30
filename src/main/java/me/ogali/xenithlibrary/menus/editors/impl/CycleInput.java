package me.ogali.xenithlibrary.menus.editors.impl;

import me.ogali.xenithlibrary.menus.editors.FieldInput;
import me.ogali.xenithlibrary.menus.editors.FieldInputProvider;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CycleInput implements FieldInput {

    private final String field;
    private final List<String> options;

    public CycleInput(String field, List<String> options) {
        this.field = field;
        this.options = options;
    }

    public CycleInput(String field, String... options) {
        this(field, List.of(options));
    }

    public CycleInput(String field, Enum<?>[] values) {
        this(field, Arrays.stream(values).map(Enum::name).toList());
    }

    @Override
    public String field() {
        return field;
    }

    @Override
    public Material icon() {
        return Material.COMPARATOR;
    }

    @Override
    public String hint() {
        return "&7Click to cycle &8(" + options.size() + " options)";
    }

    @Override
    public void handle(Player player, FieldInputProvider target, Runnable onComplete) {
        Object current = target.serialize().get(field);
        String next = next(String.valueOf(current));
        try {
            target.applyEdit(field, next);
            persistIfAble(target);
            Chat.tellFormatted(player, "&aCycled &e%s &ato: &f%s", field, next);
        } catch (Exception ex) {
            Chat.tellFormatted(player,
                    "&cFailed to cycle &e%s&c: &f%s", field, ex.getMessage());
        }
        onComplete.run();
    }

    private String next(String current) {
        int idx = options.indexOf(current);
        return options.get((idx + 1) % options.size());
    }
}