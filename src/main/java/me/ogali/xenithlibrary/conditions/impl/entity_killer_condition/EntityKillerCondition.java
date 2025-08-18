package me.ogali.xenithlibrary.conditions.impl.entity_killer_condition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.ogali.xenithlibrary.conditions.domain.AbstractCondition;
import me.ogali.xenithlibrary.conditions.evaluator.Evaluator;
import me.ogali.xenithlibrary.conditions.impl.block_age_condition.BlockAgeCondition;
import me.ogali.xenithlibrary.shared.ConfigBuilder;
import me.ogali.xenithlibrary.shared.Context;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntityKillerCondition extends AbstractCondition {
    private EntityType entityType;

    @Override
    public boolean test(Context context) {
        if (!(context.getBukkitEvent() instanceof EntityDeathEvent event)) return false;
        EntityDamageEvent lastDamageCause = event.getEntity().getLastDamageCause();
        if (lastDamageCause == null) return false;
        Entity entity = lastDamageCause.getEntity();

        return evaluator.evaluate(entity.getType().toString(),
                entityType.toString());
    }

    // TODO: REDO
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
