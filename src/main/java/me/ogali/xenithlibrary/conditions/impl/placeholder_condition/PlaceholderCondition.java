package me.ogali.xenithlibrary.conditions.impl.placeholder_condition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.ogali.xenithlibrary.XenithLibrary;
import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.evaluator.Evaluator;
import me.ogali.xenithlibrary.shared.ConfigBuilder;
import me.ogali.xenithlibrary.shared.Context;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceholderCondition extends AbstractCondition {
    private String placeholder;
    private Object value;

    public PlaceholderCondition(String id, Evaluator evaluator, String placeholder, Object value) {
        super(id, evaluator);
        this.placeholder = placeholder;
        this.value = value;
    }

    @Override
    public boolean test(Context context) {
        if (context.getPlayer() == null) return false;
        if (!XenithLibrary.PAPI) return false;

        // Replace placeholders
        String replacedValue = PlaceholderAPI.setPlaceholders(context.getPlayer(), placeholder);

        return evaluator.evaluate(replacedValue, value.toString());
    }

    public static ConfigBuilder<AbstractCondition> builder() {
        return config -> {
            Evaluator evaluator;
            try {
                evaluator = Evaluator.valueOf(config.getUppercaseString("evaluator"));
            } catch (IllegalArgumentException | NullPointerException e) {
                throw new IllegalArgumentException("Invalid or missing evaluator for PlaceholderCondition", e);
            }

            String placeholder = config.getString("placeholder");
            if (placeholder == null || placeholder.isEmpty()) {
                throw new IllegalArgumentException("Placeholder cannot be null or empty for PlaceholderCondition");
            }

            Object value = config.getObject("value");
            if (value == null) {
                throw new IllegalArgumentException("Value cannot be null for PlaceholderCondition");
            }

            return new PlaceholderCondition(placeholder, value);
        };
    }
}
