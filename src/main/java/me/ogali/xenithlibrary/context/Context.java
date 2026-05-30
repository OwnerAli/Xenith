package me.ogali.xenithlibrary.context;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@ToString
public class Context {
    private Player player;
    private Event bukkitEvent;
    private ItemStack mainHandItem;
    private ItemStack offHandItem;
    private Location location;
    private final PersistentData data = new PersistentData();

    public static Context of(Player player, Event event) {
        Context ctx = new Context();
        ctx.player = player;
        ctx.bukkitEvent = event;
        return ctx;
    }

    public static Context of(Event event) {
        return of(null, event);
    }

    public static Context of(Player player) {
        return of(player, null);
    }

    public Context withMainHandItem(ItemStack item) {
        this.mainHandItem = item;
        return this;
    }

    public Context withOffHandItem(ItemStack item) {
        this.offHandItem = item;
        return this;
    }

    public Context withLocation(Location location) {
        this.location = location;
        return this;
    }

    /**
     * Resolves the most appropriate location in priority order:
     * explicit location → event location → player location → null
     */
    public Location resolveLocation() {
        if (location != null) return location;
        if (bukkitEvent != null) {
            if (bukkitEvent instanceof org.bukkit.event.block.BlockEvent e) return e.getBlock().getLocation();
            if (bukkitEvent instanceof org.bukkit.event.entity.EntityEvent e) return e.getEntity().getLocation();
        }
        return player != null ? player.getLocation() : null;
    }

    public Optional<Player> optionalPlayer() {
        return Optional.ofNullable(player);
    }

    public static class PersistentData {

        private static final Pattern VAR_PATTERN = Pattern.compile("\\$\\{([\\w-]+)}");
        private final Map<String, Object> map = new HashMap<>();

        public void set(String key, Object value) {
            map.put(key, value);
        }

        public void addOrSet(String key, double value) {
            map.compute(key, (_, existingValue) -> {
                if (!(existingValue instanceof Number)) {
                    return value;
                }
                return ((Number) existingValue).doubleValue() + value;
            });
        }

        public void remove(String key) {
            map.remove(key);
        }

        public Optional<Object> get(String key) {
            return Optional.ofNullable(map.get(key));
        }

        @SuppressWarnings("unchecked")
        public <T> Optional<T> get(String key, Class<T> type) {
            return Optional.ofNullable(map.get(key))
                    .filter(type::isInstance)
                    .map(v -> (T) v);
        }

        public Map<String, Object> all() {
            return Collections.unmodifiableMap(map);
        }

        /**
         * Replaces ${key} placeholders in a string with values from the context map.
         * Unknown keys are replaced with an empty string.
         */
        public String interpolate(String input) {
            Matcher matcher = VAR_PATTERN.matcher(input);
            StringBuilder result = new StringBuilder();

            while (matcher.find()) {
                String key = matcher.group(1);
                String replacement = get(key).map(Object::toString).orElse("");
                matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
            }

            matcher.appendTail(result);
            return result.toString();
        }
    }
}