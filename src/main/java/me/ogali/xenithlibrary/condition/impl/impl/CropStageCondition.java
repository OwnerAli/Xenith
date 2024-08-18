package me.ogali.xenithlibrary.condition.impl.impl;

import me.ogali.xenithlibrary.condition.impl.LocationCondition;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;

public class CropStageCondition extends LocationCondition<Integer> {

    public CropStageCondition(String id, int priority, boolean negate, String value) {
        super(id, priority, negate, Integer.parseInt(value));
    }

    public CropStageCondition(String id, int priority, boolean negate, int value) {
        super(id, priority, negate, value);
    }

    public CropStageCondition(String id, int priority, boolean negate) {
        super(id, priority, negate);
        setValue(0);
    }

    @Override
    public boolean evaluate(Location input) {
        if (input.getBlock().getType() == Material.AIR) return false;
        if (!(input.getBlock().getBlockData() instanceof Ageable ageable)) return false;
        return ageable.getAge() == getValue() != isNegate();
    }

    @Override
    public String getType() {
        return "cropStage";
    }

    @Override
    public String getDisplayText() {
        return Chat.colorize("&fCrop's stage matches");
    }

}
