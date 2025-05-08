# Market Owner Implementation

This document provides technical details about the Market Owner implementation for developers.

## Class Architecture

### MarketOwnerVillager

The `MarketOwnerVillager` class extends `BDVillager` and implements a hybrid GUI system:

```java
public class MarketOwnerVillager extends BDVillager {
    public static final String TYPE = "MARKET_OWNER";
    private final BDMarket market;
    
    // Constructor and methods
}
```

Key methods:
- `onInteract(Player player)`: Opens the custom GUI menu
- `openUpgradeTrading(Player player)`: Sets up villager trades for market upgrades
- `addUpgradeTrades()`: Configures trades based on current market level

### Market Management GUI

The `MarketManagementGUI` class provides a custom interface for market management:

```java
public class MarketManagementGUI {
    // GUI types for different menus
    public enum GUIType {
        MAIN_MENU,
        ASSOCIATE_MANAGEMENT,
        PERMISSIONS_MANAGEMENT,
        SECURITY_SETTINGS,
        MARKET_UPGRADES,
        MARKET_SETTINGS,
        MARKET_LEADERBOARD
    }
    
    // Methods for different GUI screens
}
```

The GUI tracks which players have which menus open to maintain state.

## Data Flow

1. Player right-clicks Market Owner
2. `onInteract()` method runs permission checks
3. If permitted, the main menu opens via `MarketManagementGUI.openMainMenu()`
4. Player navigates through GUI or selects upgrade option
5. For upgrades, `openUpgradeTrading()` is called
6. Player completes standard villager trades for upgrades
7. Upgrade certificate is applied to market via `MarketManager.upgradeMarket()`

## Villager Trading Interface

The Market Owner uses the standard Minecraft villager trading interface for upgrades by:

1. Clearing any existing trades: `getVillager().setRecipes(new ArrayList<>())`
2. Adding specific trades based on current level: `addTrade(item1, item2, result)`
3. Creating `MerchantRecipe` objects with proper parameters
4. Adding complete recipes to the villager's recipe list

## Persistent Data

The Market Owner stores:
- Market ID in villager's persistent data container
- Upgrade certificate information in item persistent data
- Associate UUIDs in the market data

Example:
```java
// Store market ID
getVillager().getPersistentDataContainer().set(
    plugin.getNamespacedKey("market_id"),
    PersistentDataType.STRING,
    market.getId()
);

// Create upgrade certificate
meta.getPersistentDataContainer().set(
    plugin.getNamespacedKey("market_upgrade"),
    PersistentDataType.STRING,
    market.getId() + ":" + newLevel
);
```

## Market Upgrades

Each market level grants specific benefits:

| Level | Method | Benefits |
|-------|--------|----------|
| 1 → 2 | `addLevel2UpgradeTrade()` | +2 Collector slots, +10% price modifier |
| 2 → 3 | `addLevel3UpgradeTrade()` | +2 Collector slots, +10% price modifier |
| 3 → 4 | `addLevel4UpgradeTrade()` | +3 Collector slots, +10% price modifier |

## Integration with Other Systems

The Market Owner interfaces with:
- `MarketManager` for managing market data and upgrades
- `BDEconomyModule` for price modifiers and economy operations
- `VillagerManager` for villager entity management
- Bukkit inventory API for custom GUI

## Error Handling

- The Market Owner has specific error handling for null markets
- Trades have restrictions to prevent duplication exploits
- Permission checking prevents unauthorized access
- Teleportation if villager moves outside market area (24 block radius)

## Extending the System

To add new features to the Market Owner:
1. Add any new menu types to `GUIType` enum
2. Create a new method in `MarketManagementGUI` for the menu
3. Add a handler method for clicks in that menu
4. Update any associated data in the `BDMarket` class

## Performance Considerations

The Market Owner system is designed with performance in mind:
- GUIs are created on-demand rather than persisting
- Trades are generated only when needed
- Villager teleport checks occur only when necessary
- HashMap lookups maintain O(1) access for player data

## Commands and Permissions

| Command | Permission | Description |
|---------|------------|-------------|
| `/bdmarket check` | bdcraft.market.check | View market boundaries |
| `/bdmarket info` | bdcraft.market.info | View market information |
| `/bdmarket upgrade` | bdcraft.market.upgrade | Access upgrades |
| `/bdmarket associate add` | bdcraft.market.associate | Add associate |
| `/bdmarket associate remove` | bdcraft.market.associate | Remove associate |
| `/bdmarket delete` | bdcraft.admin.market | Delete a market (admin only) |