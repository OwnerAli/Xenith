package me.ogali.xenithlibrary.condition.impl.impl;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.ogali.xenithlibrary.condition.impl.LocationCondition;
import me.ogali.xenithlibrary.utilities.Chat;
import org.bukkit.Location;

public class WorldGuardRegionCondition extends LocationCondition<String> {

    public WorldGuardRegionCondition(String id, int priority, boolean negate, String regionName) {
        super(id, priority, negate, regionName);
    }

    public WorldGuardRegionCondition(String id, int priority, boolean negate) {
        super(id, priority, negate);
        setValue("Region Name");
    }

    @Override
    public String getType() {
        return "worldGuardRegion";
    }

    @Override
    public String getDisplayText() {
        return Chat.colorize("&fPlayer is in worldguard region");
    }

    @Override
    public boolean evaluate(Location location) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet applicableRegions = query.getApplicableRegions(BukkitAdapter.adapt(location));

        for (ProtectedRegion region : applicableRegions.getRegions()) {
            if (region.getId().equalsIgnoreCase(getValue())) return !isNegate();
        }
        return false;
    }

}
