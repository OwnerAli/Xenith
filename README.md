# Xenith

Xenith is a Spigot/Paper **library plugin** for building configurable **conditions** ("should this happen?") and **actions** ("make this happen"). Server owners create and edit them in-game with commands and GUIs. Your plugin then refers to them by id and runs them against a player, an event or a location.

It is meant for plugins that would otherwise each build their own "requirements + rewards" system: custom drops, block-break rewards, item abilities, region triggers and so on. You write the trigger logic. Xenith provides the configurable building blocks, persists them, and gives server owners an editor.

- **Conditions:** predicates such as `PLAYER_HEALTH >= 10`, `TOOL_MATERIAL == DIAMOND_PICKAXE` or a PlaceholderAPI value check.
- **Actions:** effects such as sending a message or title, playing a sound, running a command or cancelling the event. Each action has its own trigger chance.
- **Registries:** every type is registered by key. Your plugin can add its own condition and action types.
- **Persistence:** instances are saved to `plugins/Xenith/conditions.yml` and `plugins/Xenith/actions.yml`.
- **In-game editor:** `/xc` and `/xa` open GUIs to create, edit, test and delete instances.

---

## Requirements

| | |
|---|---|
| Java | 25 |
| Server | Spigot/Paper 1.21.x (built against `1.21.11-R0.1-SNAPSHOT`) |
| Optional | PlaceholderAPI (enables placeholders in messages and the `PLACEHOLDER` condition), WorldGuard |

---

## Installation

### On the server

Put the `Xenith-<version>.jar` in your server's `plugins/` folder, next to your plugin.

### In your plugin

Xenith is published through [JitPack](https://jitpack.io/#OwnerAli/Xenith). Use `compileOnly`, because Xenith runs as its own plugin on the server and you should not shade it.

**Gradle**

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.OwnerAli:Xenith:<tag-or-commit>'
}
```

**Maven**

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.OwnerAli</groupId>
    <artifactId>Xenith</artifactId>
    <version>TAG-OR-COMMIT</version>
    <scope>provided</scope>
</dependency>
```

**plugin.yml**: declare the dependency so Xenith loads first:

```yaml
depend: [Xenith]
```

---

## Core concepts

```
 ConditionType / ActionType   ── a "kind" of thing, registered by key (e.g. PLAYER_HEALTH)
          │  builds from DomainConfig
          ▼
 AbstractCondition / AbstractAction ── a configured instance with an id (e.g. "low_health")
          │  grouped by
          ▼
 ConditionHolder / ActionHolder     ── what your plugin stores and runs
          │  evaluated against
          ▼
 ConditionContext / ActionContext   ── player, event, block, location, world, data
```

| Class | Package | Purpose |
|---|---|---|
| `Condition` | `conditions.domain` | Functional interface `boolean test(ConditionContext)`, with `and` / `or` combinators |
| `AbstractCondition` | `conditions.domain` | Base class for persisted conditions. Has `id`, `typeKey` and `evaluator` |
| `ConditionRegistry` | `conditions.domain` | Registers types, stores instances, loads and saves them |
| `ConditionHolder` | `conditions.domain` | A list of conditions with `checkAll` and `checkAny` |
| `Evaluator` | `conditions.evaluator` | `EQUAL`, `NOT_EQUAL`, `LESS_THAN`, `GREATER_THAN`, `LESS_OR_EQUAL`, `GREATER_OR_EQUAL`. Compares numerically when both sides parse as numbers, otherwise lexically |
| `Action` | `actions.domain` | Functional interface `void execute(ActionContext)`, with `andThen` |
| `AbstractAction` | `actions.domain` | Base class for persisted actions. Has `id`, `typeKey` and `chance` (0–100) |
| `ActionRegistry` | `actions.domain` | Registers types, stores instances, loads and saves them |
| `ActionHolder` | `actions.domain` | A list of actions with `executeAll` |
| `Context` | `context` | Shared context data and the `PersistentData` scratch map |
| `DomainConfig` | `shared` | Typed read-only view over a config map, passed to builders |

---

## Using conditions and actions in your plugin

Server owners create instances in-game, for example a condition `holding_diamond_pick` and an action `reward_msg`. Your plugin stores their **ids** in its own config and resolves them at runtime.

```yaml
# your-plugin/config.yml
rewards:
  diamond-break:
    conditions: [holding_diamond_pick, is_survival]
    actions: [reward_msg, reward_sound]
```

```java
public final class RewardListener implements Listener {

    private final ConditionHolder conditions;
    private final ActionHolder actions;

    public RewardListener(ConfigurationSection section) {
        this.conditions = ConditionHolder.fromIdList(section.getStringList("conditions"));
        this.actions = ActionHolder.fromIdList(section.getStringList("actions"));
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        ConditionContext conditionContext = ConditionContext.of(event.getPlayer(), event);
        conditionContext.setBlock(event.getBlock());   // needed by BLOCK_TYPE

        if (!conditions.checkAll(conditionContext)) return;

        actions.executeAll(ActionContext.of(event.getPlayer(), event));
    }
}
```

Notes:

- Resolve holders **after** Xenith has loaded, in your `onEnable` or later. Xenith loads its YAML files in its own `onEnable`.
- `fromIdList` calls `Registry.get(id)`, which **throws** `IllegalArgumentException` for an unknown id. Check with `ConditionRegistry.isRegistered(id)` / `ActionRegistry.isRegistered(id)` first if the ids come from user config.
- To save a holder back to your config, use `holder.toIdList()`.
- Pass the Bukkit event into the context. `CANCEL_EVENT` / `UNCANCEL_EVENT` and `BLOCK_AGE` read it.

### Fetching a single instance

```java
AbstractCondition cond = ConditionRegistry.get("low_health");
boolean ok = cond.test(ConditionContext.of(player, null));

ActionRegistry.get("reward_msg").execute(ActionContext.of(player));
```

### Building instances in code

`ConditionRegistry.parse` builds a condition from a plain map, using the same format as `conditions.yml`:

```java
AbstractCondition lowHealth = ConditionRegistry.parse("low_health", Map.of(
        "type", "PLAYER_HEALTH",
        "health", 6.0,
        "evaluator", "LESS_OR_EQUAL"
));

lowHealth.persist(); // optional: register and save it so it shows up in /xc
```

For actions, build through the type:

```java
AbstractAction msg = ActionRegistry.getType("PLAYER_MESSAGE").builder()
        .build(new DomainConfig(Map.of("message", "&aNice find!", "chance", 50.0)));
msg.setId("lucky_msg");
msg.setTypeKey("PLAYER_MESSAGE");
msg.persist();
```

You can also skip persistence completely and compose lambdas, because `Condition` and `Action` are functional interfaces:

```java
Condition sneakingInNether = ConditionRegistry.get("is_sneaking")
        .and(ctx -> ctx.getPlayer().getWorld().getEnvironment() == World.Environment.NETHER);
```

### Context data

Every context carries a `PersistentData` map for passing values between your code and custom types:

```java
ActionContext ctx = ActionContext.of(player, event);
ctx.getData().set("amount", 5);
ctx.getData().addOrSet("multiplier", 0.5);

String text = ctx.getData().interpolate("You got ${amount} gems"); // "You got 5 gems"
```

The built-in types do not read this map yet. It is there for your own types.

---

## Adding your own types

Every type is registered against a key and a builder (`DomainConfig -> instance`). Third-party types must be registered with a **namespace**, normally your plugin name. The stored type key then becomes `THIRD_PARTY::<NAMESPACE>::<KEY>`.

### Custom condition

```java
public class PlayerFlyingCondition extends AbstractCondition {
    private boolean flying;

    public PlayerFlyingCondition(boolean flying) {
        this.flying = flying;
    }

    @Override
    public boolean test(ConditionContext context) {
        Player player = context.getPlayer();
        return player != null && player.isFlying() == flying;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("flying", flying);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "flying" -> this.flying = Boolean.parseBoolean(value);
            default -> super.applyEdit(field, value);
        }
    }

    // Optional: control how the GUI edits each field (defaults to a chat text prompt)
    @Override
    public List<FieldInput> fieldInputs() {
        return List.of(FieldInputs.bool("flying"));
    }

    public static AbstractCondition fromConfig(DomainConfig config) {
        return new PlayerFlyingCondition(config.getBoolean("flying", true));
    }
}
```

```java
@Override
public void onEnable() {
    ConditionRegistry.registerType(
            new ConditionType("PLAYER_FLYING", PlayerFlyingCondition::fromConfig, Material.ELYTRA),
            getName());
}
```

### Custom action

```java
public class HealAction extends AbstractAction {
    private double amount;

    public HealAction(double amount) {
        this.amount = amount;
    }

    @Override
    public void execute(ActionContext context) {
        if (!rolledSuccessfully()) return;           // honours the "chance" field
        Player player = context.getPlayer();
        if (player == null) return;
        double max = player.getAttribute(Attribute.MAX_HEALTH).getValue();
        player.setHealth(Math.min(max, player.getHealth() + amount));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new LinkedHashMap<>(super.serialize());
        data.put("amount", amount);
        return data;
    }

    @Override
    public void applyEdit(String field, String value) {
        switch (field) {
            case "amount" -> this.amount = Double.parseDouble(value);
            default -> super.applyEdit(field, value);
        }
    }

    public static AbstractAction fromConfig(DomainConfig config) {
        HealAction action = new HealAction(config.getDouble("amount", 4.0));
        action.setChance(config.getDouble("chance", 100.0));
        return action;
    }
}
```

```java
@Override
public void onEnable() {
    ActionRegistry.registerType(
            new ActionType("HEAL", HealAction::fromConfig, Material.GOLDEN_APPLE),
            getName());
    ActionRegistry.loadThirdParty(); // load saved instances of third-party action types
}
```

### What to implement

| Method | Purpose |
|---|---|
| `test` / `execute` | The behaviour |
| `serialize()` | Call `super.serialize()` first, then add your fields. Used for saving **and** to list the editable fields in the GUI |
| `applyEdit(field, value)` | Parse a raw string from the GUI into your field. Delegate unknown fields to `super` |
| `static fromConfig(DomainConfig)` | Build an instance from saved or parsed config |
| `fieldInputs()` *(optional)* | `FieldInputs.text(..)`, `bool(..)`, `cycle(field, options...)` or `cycle(field, MyEnum.values())` |

In conditions, use the inherited `evaluate(actual, expected)` helper so the server owner's chosen `Evaluator` applies. It falls back to `EQUAL`.

---

## Built-in types

### Conditions

Every condition also accepts an `evaluator` (default `EQUAL`).

| Type | Fields | Notes |
|---|---|---|
| `COMPOSITE` | `operator` (`AND`/`OR`), `conditions` (list of ids) | Combines other conditions |
| `BLOCK_TYPE` | `material` | Reads `context.getBlock()`. Set it yourself |
| `BLOCK_AGE` | `age` | Requires a `BlockBreakEvent` in the context |
| `WORLD` | `world` | |
| `LOCATION_BIOME` | `biome` | |
| `TOOL_MATERIAL` | `material`, `hand` (`MAIN_HAND`/`OFF_HAND`) | |
| `TOOL_ENCHANTMENT` | `enchantment`, `hand` | |
| `TOOL_ENCHANTMENT_LEVEL` | `enchantment`, `level`, `hand` | |
| `TOOL_DURABILITY` | `durability`, `hand` | |
| `TOOL_NAME` | `name`, `hand` | |
| `TOOL_LORE` | `lore`, `hand` | |
| `PLAYER_GAMEMODE` | `gamemode` | |
| `PLAYER_HEALTH` | `health` | |
| `PLAYER_LEVEL` | `level` | |
| `PLAYER_PERMISSION` | `permission` | |
| `PLAYER_IS_SNEAKING` | `sneaking` | |
| `PLAYER_IS_SPRINTING` | `sprinting` | |
| `PLACEHOLDER` | `placeholder`, `expected` | Requires PlaceholderAPI |

### Actions

Every action also accepts a `chance` from 0 to 100 (default `100`).

| Type | Fields |
|---|---|
| `COMPOSITE` | `actions` (list of ids) |
| `CANCEL_EVENT` / `UNCANCEL_EVENT` | none (needs a `Cancellable` event in the context) |
| `BROADCAST` | `message` |
| `PLAYER_MESSAGE` | `message` |
| `PLAYER_ACTION_BAR` | `message` |
| `PLAYER_TITLE` | `title`, `subtitle`, `fade-in`, `stay`, `fade-out` (ticks) |
| `CONSOLE_COMMAND` | `command` |
| `PLAYER_COMMAND` | `command` |
| `PLAYER_SOUND` | `sound`, `volume`, `pitch` |
| `GLOBAL_SOUND` | `sound`, `volume`, `pitch` |

Messages support `&` color codes and, when PlaceholderAPI is installed, placeholders.

### Example YAML

```yaml
# plugins/Xenith/conditions.yml
low_health:
  type: PLAYER_HEALTH
  health: 6.0
  evaluator: LESS_OR_EQUAL
holding_diamond_pick:
  type: TOOL_MATERIAL
  material: DIAMOND_PICKAXE
  hand: MAIN_HAND

# plugins/Xenith/actions.yml
reward_msg:
  type: PLAYER_MESSAGE
  message: '&aYou found something!'
  chance: 25.0
reward_bundle:
  type: COMPOSITE
  actions: [reward_msg, reward_sound]
```

---

## Commands and permissions

| Command | Permission | Description |
|---|---|---|
| `/xc create <id>` | `xenith.condition.create` | Pick a type and configure a new condition |
| `/xc edit <id>` | `xenith.condition.edit` | Edit a condition |
| `/xc list` | `xenith.condition.list` | Browse conditions |
| `/xc delete <id>` | `xenith.condition.delete` | Delete a condition |
| `/xa create <id>` | `xenith.action.create` | Pick a type and configure a new action |
| `/xa edit <id>` | `xenith.action.edit` | Edit an action |
| `/xa list` | `xenith.action.list` | Browse actions |
| `/xa delete <id>` | `xenith.action.delete` | Delete an action |
| `/xa test <id>` | `xenith.action.test` | Run an action on yourself |

Aliases: `/xenithcondition` for `/xc`, `/xenithaction` for `/xa`.

In the GUI, clicking a field opens a chat prompt for text fields (type `cancel` to abort; times out after 60 s). Boolean and cycle fields change with each click.

---

## Known limitations

These are current behaviours worth knowing when you build on the library:

- **Third-party condition instances are not reloaded.** Xenith loads `conditions.yml` before your plugin registers its types, so those entries fail to load and are logged. There is no `ConditionRegistry.loadThirdParty()` yet. Re-create them with `ConditionRegistry.parse` from your plugin if needed.
- **The `hand` field is only read from config by `TOOL_MATERIAL`.** The other `TOOL_*` conditions only get `hand` through the GUI or `applyEdit("hand", ...)`. After a reload it is `null`, so set it explicitly.
- **Edits save immediately.** Every field edit in the GUI persists the instance, so abandoning `/xa create` or `/xc create` partway still leaves an entry.
- **Saves are asynchronous.** `saveAll()` on shutdown is not awaited.

---

## Building from source

```bash
./gradlew build            # shaded jar in build/libs/Xenith-<version>.jar
./gradlew publishToMavenLocal
```

InventoryFramework and ACF are shaded. InventoryFramework is relocated to `me.ogali.xenithlibrary.inventoryframework`.
