package me.ogali.xenithlibrary.conditions.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.domain.ConditionContext;
import me.ogali.xenithlibrary.shared.DomainConfig;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlaceholderCondition extends AbstractCondition {
    private String placeholder;
    private String expected;

    public PlaceholderCondition(String placeholder, String expected) {
        this.placeholder = placeholder;
        this.expected = expected;
    }

    @Override
    public boolean test(ConditionContext context) {
        if (context.getPlayer() == null) return false;

        String actual;
        if (XenithLibrary.isPapiEnabled()) {
            actual = PlaceholderAPI.setPlaceholders(context.getPlayer(), placeholder);
        } else {
            throw new IllegalArgumentException("Please install PlaceHolderAPI (PAPI) to use this condition!");
        }

        return evaluate(actual, expected);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("placeholder", placeholder);
        data.put("expected", expected);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "placeholder" -> this.placeholder = value;
            case "expected" -> this.expected = value;
            default -> super.applyEdit(field, value);
        }
    }

    public static AbstractCondition fromConfig(DomainConfig config) {
        String placeholder = config.getString("placeholder", "");
        String expected = config.getString("expected", "");
        return new PlaceholderCondition(placeholder, expected);
    }
}