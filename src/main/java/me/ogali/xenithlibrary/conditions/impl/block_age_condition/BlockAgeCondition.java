package me.ogali.xenithlibrary.conditions.impl.block_age_condition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.evaluator.Evaluator;
import me.ogali.xenithlibrary.shared.ConfigBuilder;
import me.ogali.xenithlibrary.shared.Context;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlockAgeCondition extends AbstractCondition {
    private Integer age;

    @Override
    public boolean test(Context context) {
        Event bukkitEvent = context.getBukkitEvent();
        if (!(bukkitEvent instanceof BlockBreakEvent event)) return false;
        if (!(event.getBlock().getBlockData() instanceof Ageable ageable)) return false;
        return evaluator.evaluate(String.valueOf(ageable.getAge()), age.toString());
    }

    public static ConfigBuilder<AbstractCondition> builder() {
        return config -> {
            Evaluator evaluator;
            try {
                evaluator = Evaluator.valueOf(config.getUppercaseString("evaluator"));
            } catch (IllegalArgumentException | NullPointerException e) {
                throw new IllegalArgumentException("Invalid or missing evaluator for PlaceholderCondition", e);
            }

            Integer age = config.getInt("age");
            if (age == null) {
                throw new IllegalArgumentException("Placeholder cannot be null or empty for PlaceholderCondition");
            }

            return new BlockAgeCondition(age);
        };
    }
}
