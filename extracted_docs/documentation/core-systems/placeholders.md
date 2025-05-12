# BDPlaceholders System

The BDPlaceholders system provides dynamic text replacement throughout the BDCraft plugin. This system allows for personalized, context-aware messaging and GUI elements without requiring external dependencies.

## Available Placeholders

The following placeholders are available throughout BDCraft:

### Player & Economy Placeholders

| Placeholder | Description | Example Output |
|-------------|-------------|----------------|
| `{rank}` | Player's current BD rank | "Expert Farmer" |
| `{rank_id}` | Player's numeric rank ID | "3" |
| `{balance}` | Player's BD currency balance | "12,500" |
| `{rebirth}` | Player's rebirth level | "2" |
| `{rank_progress}` | Progress to next rank | "45.2%" |
| `{next_rank}` | Next rank name | "Master Farmer" |
| `{trade_count}` | Total trades completed | "567" |
| `{crops_harvested}` | Total BD crops harvested | "1,245" |
| `{rank_cost_next}` | Cost of next rank | "30,000" |

### Market Placeholders

| Placeholder | Description | Example Output |
|-------------|-------------|----------------|
| `{market_id}` | ID of player's current market | "market_12345" |
| `{market_level}` | Level of player's market | "3" |
| `{market_name}` | Name of player's market | "Steve's Farm" |
| `{market_reputation}` | Reputation in current market | "42" |
| `{market_collectors}` | Number of collectors in market | "5" |
| `{market_max_collectors}` | Maximum collectors allowed | "7" |
| `{market_daily_income}` | Daily income from market | "2,500" |
| `{market_owner}` | Owner of the current market | "Steve" |
| `{market_associates}` | List of market associates | "Alex, Jane, Bob" |

### Villager Placeholders

| Placeholder | Description | Example Output |
|-------------|-------------|----------------|
| `{dealer_count}` | Number of BD dealers owned | "3" |
| `{collector_count}` | Number of BD collectors owned | "5" |
| `{seasonal_trader}` | Current seasonal trader type | "Summer Trader" |
| `{seasonal_end}` | Time until seasonal trader leaves | "2d 14h" |

### BDVital Placeholders

| Placeholder | Description | Example Output |
|-------------|-------------|----------------|
| `{home_count}` | Number of homes set | "3" |
| `{home_max}` | Maximum homes allowed | "5" |
| `{mail_count}` | Unread mail messages | "2" |
| `{tp_cooldown}` | Teleport cooldown remaining | "30s" |
| `{back_available}` | Whether /back is available | "Yes" |
| `{market_warp_count}` | Available market warps | "12" |

## Using Placeholders in Messages

Placeholders can be used in any BDCraft message by surrounding the placeholder name with curly braces:

```yaml
# Example in messages.yml
messages:
  market-welcome: "&2Welcome to your BD Market!\n&eRank: &a{rank}\n&eBalance: &a{balance}"
  rank-up: "&aCongratulations! You've reached &6{rank}&a rank!"
  trade-complete: "&eYou completed a trade! You now have &a{balance}&e BD currency."
```

## Using Placeholders in GUIs

Placeholders can enhance GUI item names and lore:

```java
// Example in code
ItemStack rankItem = new ItemStack(Material.EMERALD);
ItemMeta meta = rankItem.getItemMeta();
meta.setDisplayName(ChatColor.GREEN + "Your Rank");
List<String> lore = new ArrayList<>();
lore.add(ChatColor.YELLOW + "Current Rank: " + ChatColor.WHITE + "{rank}");
lore.add(ChatColor.YELLOW + "Progress: " + ChatColor.WHITE + "{rank_progress}");
lore.add(ChatColor.YELLOW + "Next Rank: " + ChatColor.WHITE + "{next_rank}");
meta.setLore(lore);
rankItem.setItemMeta(meta);

// The placeholders will be processed when setting the item in the inventory
inventory.setItem(13, placeholderManager.processItem(rankItem, player));
```

## PlaceholderAPI Integration

BDCraft automatically integrates with PlaceholderAPI if it's installed on the server. This allows BDCraft placeholders to be used in other plugins that support PlaceholderAPI.

### Using BDCraft Placeholders in PlaceholderAPI

When PlaceholderAPI is installed, all BDCraft placeholders become available with the `%bdcraft_*%` prefix:

```
%bdcraft_rank%            # Player's rank
%bdcraft_balance%         # Player's balance
%bdcraft_market_level%    # Player's market level
```

This integration works automatically without any additional setup required.

## Custom Placeholders in Modules

Module developers can register custom placeholders with the BDPlaceholders system:

```java
// Example in a module class
PlaceholderManager placeholderManager = plugin.getPlaceholderManager();

// Register a placeholder
placeholderManager.registerPlaceholder("custom_value", player -> {
    // Calculate and return the value
    return calculateCustomValue(player);
});
```

## Technical Implementation

The BDPlaceholders system is implemented in the `PlaceholderManager` class:

- Placeholders are stored in a concurrent map for thread safety
- Pattern matching is used to efficiently find and replace placeholders
- Placeholders are processed in both text and ItemStack lore/names
- PlaceholderAPI integration occurs automatically if available

## Best Practices

1. **Performance Considerations**
   - Placeholder values are calculated on demand, so avoid expensive operations
   - Consider caching values that are expensive to calculate

2. **Formatting**
   - Number formatting is handled automatically for numerical values
   - Use the `formatNumber()` method for consistent formatting

3. **Fallbacks**
   - Always provide sensible defaults when a placeholder might not have a value
   - Use conditional checks to avoid exceptions when data is missing

BDPlaceholders provides a flexible and powerful way to display dynamic information throughout the plugin without requiring external dependencies.