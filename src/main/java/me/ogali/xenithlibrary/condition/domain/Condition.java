package me.ogali.xenithlibrary.condition.domain;

import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public interface Condition<T> extends Comparable<Condition<?>> {

    String getId();

    boolean isNegate();

    int getPriority();

    boolean evaluate(T input, LivingEntity livingEntity);

    @Override
    int compareTo(@NotNull Condition<?> o);

}
