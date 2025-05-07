# BD Market System Management

This guide explains how the BD market system works for administrators. The BDCraft economy is entirely player-driven through the market system without requiring any admin-created villages or infrastructure.

## BD Trading Infrastructure Overview

### Natural Dealers in Villages

The BDCraft plugin introduces natural BD Dealers that spawn in vanilla Minecraft villages:

- Dealers have a 45% chance to spawn in any vanilla village
- These dealers allow players to obtain their first BD seeds
- They appear as FARMER villagers with a green "BD Dealer" name tag
- They naturally respawn if killed, but cannot be moved or relocated
- These dealers are the starting point for new players to enter the BD economy

### Player-Driven Market System

The BDCraft economy is based entirely around player-created markets:

- Players build market stalls and place BD Market Tokens
- Players expand markets by building collector houses using House Tokens
- Players manage access, permissions, and trading through a Market GUI
- Traders have individual reputation counters rather than village-based reputation

**The key principle**: Players are in control of creating, expanding, and managing their own economic centers, with no dependency on administrator-created villages.

## Market Management for Administrators

### Testing Market System

Administrators can use these commands to test the market system:

- `/bdadmin spawn dealer` - Spawns a natural BD Dealer at your location
- `/bdadmin give <player> markettoken [amount]` - Gives market tokens to players
- `/bdadmin give <player> housetoken [amount]` - Gives house tokens to players
- `/bdadmin market visualize <id>` - Shows boundaries of an existing market

### Economy Management

- `/bdadmin economy give <player> <amount>` - Gives server currency to a player
- `/bdadmin economy take <player> <amount>` - Takes currency from a player
- `/bdadmin economy set <player> <amount>` - Sets a player's currency amount

### Market Configuration

Key configuration options in config.yml:

```yaml
# Market Settings
market:
  enabled: true                 # Allow player-created markets
  min-distance: 30              # Minimum blocks between markets
  market-radius: 49             # Radius of player markets in blocks
  max-collectors-initial: 3     # Starting collector limit
  max-collectors-upgraded: 10   # Maximum collectors after upgrades
  natural-dealer-chance: 45     # Percentage chance for dealers to spawn in villages
```

## Player Market Creation Process

1. **Build a Market Stall**:
   - Create a structure with a 3x3 roof minimum
   - Place a bed inside the structure
   - Build walls with a door (3x3 wall minimum)
   - Place an item frame above the door

2. **Craft the BD Market Token**:
   - Recipe: BD Stick surrounded by diamonds with 2 emeralds in the middle left and right positions
   - Place this token in the item frame above the market stall door

3. **Market Initialization**:
   - Upon placement of the token, a Dealer and Market Owner will spawn
   - Market is registered in the plugin's system
   - Player becomes the "Market Founder" with management permissions

## Market Technical Implementation

### Market Data Structure

BD Markets are stored with the following data structure:

```java
public class BDMarket {
    private String id;                  // Unique market identifier
    private Location center;            // Center coordinates (token location)
    private World world;                // World the market is in
    private int radius;                 // Effective radius (49 blocks by default)
    private UUID founder;               // UUID of market creator
    private List<UUID> associates;      // UIDs of market associates
    private int level;                  // Market level (1-4)
    private Map<UUID, BDTrader> traders;   // Associated traders
    private Map<UUID, Map<UUID, Integer>> reputations; // Per-trader reputation values
    // Additional properties and methods
}
```

### Storage Implementation

Market data is stored in JSON format in:

```
plugins/BDCraft/data/markets.json
```

Example data structure:

```json
{
  "markets": [
    {
      "id": "market_1",
      "world": "world",
      "x": 250,
      "y": 70,
      "z": 420,
      "radius": 49,
      "founder": "550e8400-e29b-41d4-a716-446655440000",
      "associates": [
        "6ba7b810-9dad-11d1-80b4-00c04fd430c8"
      ],
      "level": 2,
      "collectors": 3
    }
  ]
}
```

## Trader Types and Implementation

### BD Trader Types

The BDCraft plugin features three types of specialized traders:

1. **BD Dealers**: Sell seeds to players
   - Spawn naturally in vanilla villages (45% chance)
   - Always spawn when a market token is placed
   - Use FARMER profession (Villager.Profession.FARMER)
   - Have green name tags for easy identification

2. **BD Collectors**: Buy crops from players
   - Only spawn in player markets when house tokens are placed
   - Use LIBRARIAN profession (Villager.Profession.LIBRARIAN)
   - Have blue name tags for easy identification
   - Each market can have multiple collectors based on market level

3. **Seasonal BD Traders**: Offer limited-time special items
   - Only appear in level 3+ player markets
   - Use NITWIT profession (Villager.Profession.NITWIT)
   - Have yellow name tags for easy identification
   - Offer trades that change based on Minecraft seasons

### Market Owner

In addition to the traders above, each market includes a Market Owner:

- Uses CARTOGRAPHER profession (Villager.Profession.CARTOGRAPHER)
- Has gold name tag for easy identification
- Provides market upgrade options
- Provides market management GUI
- Sells house tokens at higher market levels

### Trader Implementation

All traders are created using a factory pattern that creates custom villager entities with specialized trades and abilities. Each trader is linked to a specific market (or natural village for natural dealers) and maintains individual reputation scores with players.

## Player Market Expansion

### Building Collector Houses

Players can expand their markets by adding collector houses:

1. **House Structure Requirements**:
   - 3x3 roof minimum
   - One bed inside
   - 3x3 wall with a door
   - Item frame above the door

2. **House Token Crafting**:
   - Recipe: Bed surrounded by BD crops
   - Place in item frame above house door

3. **Collector Spawning**:
   - BD Collector (LIBRARIAN profession) will spawn inside
   - Each market initially supports up to 3 collectors
   - Additional collectors require market upgrades

### Market Boundary Visualization

Players can use the following command to visualize market boundaries:

```
/bdmarket check
```

This temporarily changes blocks at the boundary:
- Top layer blocks within boundary become red wool for 10 seconds
- Corner blocks become black wool for 10 seconds
- All changed blocks automatically revert to original state
- Temporary blocks are unbreakable during visualization

## Market Configuration Settings

Key configuration options in config.yml:

```yaml
# Market Settings
market:
  enabled: true                 # Allow player-created markets
  min-distance: 30              # Minimum blocks between markets
  market-radius: 49             # Radius of player markets in blocks
  max-collectors-initial: 3     # Starting collector limit
  max-collectors-upgraded: 10   # Maximum collectors after upgrades
  natural-dealer-chance: 45     # Percentage chance for dealers to spawn in villages
  reputation:
    per-trader: true            # Track reputation per trader rather than per village
    display-changes: true       # Show reputation changes in chat
```

## Market Management GUI

The Market Management GUI provides a comprehensive interface for managing various aspects of a player's market:

1. **Accessing the GUI**:
   - Right-click the Market Owner villager
   - Select "Market Management" option
   - Only the founder and associates can access management features

2. **GUI Tabs and Functions**:
   - **Associates**: Add or remove market associates (founder only)
   - **Permissions**: Control who can trade with collectors (public/private)
   - **Security**: Manage building permissions within market area
   - **Upgrades**: Purchase market level upgrades
   - **Settings**: Configure market appearance and effects

## Player Commands

### Market Commands

- `/bdmarket check` - Visualize market boundaries with temporary wool blocks
- `/bdmarket info` - Display information about the market you're standing in
- `/bdmarket list` - List all markets you have founder or associate status in
- `/bdmarket upgrade` - Opens upgrade GUI when used near Market Owner

### Admin Commands

- `/bdadmin market list` - List all markets on the server
- `/bdadmin market info <id>` - Show detailed market information
- `/bdadmin market delete <id>` - Remove a market
- `/bdadmin market setlevel <id> <level>` - Set market upgrade level (1-4)
- `/bdadmin spawn dealer` - Spawn a natural BD Dealer at your location
- `/bdadmin give <player> markettoken [amount]` - Give market tokens
- `/bdadmin give <player> housetoken [amount]` - Give house tokens

This player-driven market system forms the backbone of the BDCraft economy, offering a fully self-contained trading infrastructure that doesn't rely on admin-created villages.