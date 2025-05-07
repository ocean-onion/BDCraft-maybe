# Self-Contained Systems

BDCraft is designed as a fully self-contained plugin that does not require or support external plugin integrations. This guide covers the built-in systems that provide complete functionality without any dependencies.

## Built-in Systems

BDCraft includes the following built-in systems that work seamlessly together:

| System | Purpose | Description |
|--------|---------|-------------|
| BDVital | Essential Utilities | Core server features like teleportation, homes, and messaging |
| BDPerms | Permission Management | Robust permission system with group support |
| BDLogging | Activity Tracking | Comprehensive logging of all BD activities |
| BDEconomy | Economy Management | Complete internal economy system without Vault |
| BDPlaceholders | Dynamic Text | Internal text replacement system |


## BDEconomy System

The BDCraft plugin includes a fully self-contained economy system that doesn't require any external plugins.

### Economy Features

- **Dual Currency**: Emerald/server currency hybrid system
- **Balance Management**: Complete in-game currency handling
- **Configuration**: Fully customizable in `economy.yml`
- **Market Trading**: Integrated with the player market system

### Economy Commands

```
# Check balance
/bdbal

# Check transaction history
/bdbal history

# Transfer currency to another player
/bdpay <player> <amount>

# Admin currency management
/bdadmin economy give <player> <amount>
/bdadmin economy take <player> <amount>
/bdadmin economy set <player> <amount>
```

## BDPlaceholders System

BDCraft includes an internal placeholders system for displaying dynamic text in messages and GUIs.

### Available Placeholders

| Placeholder | Description | Example Output |
|-------------|-------------|----------------|
| `{rank}` | Player's BD rank | "Expert Farmer" |
| `{rank_id}` | Player's numeric rank | "3" |
| `{balance}` | Player's currency balance | "12,500" |
| `{rebirth}` | Player's rebirth level | "2" |
| `{reputation}` | Reputation in current market | "42" |
| `{market_id}` | ID of player's current market | "market_12345" |
| `{market_level}` | Level of player's market | "3" |
| `{trade_count}` | Total trades completed | "567" |
| `{crops_harvested}` | Total BD crops harvested | "1,245" |
| `{rank_progress}` | Progress to next rank | "45%" |
| `{rank_cost_next}` | Cost of next rank | "30,000" |

### Using Placeholders

These placeholders can be used in any BDCraft message:

```yaml
# Example in messages.yml
messages:
  market-welcome: "&2Welcome to your BD Market!\n&eRank: &a{rank}\n&eBalance: &a{balance}"
  rank-up: "&aCongratulations! You've reached &6{rank}&a rank!"
  trade-complete: "&eYou completed a trade! You now have &a{balance}&e BD currency."
```

## BDVital System

The BDVital module provides essential server utilities for the BDCraft plugin including teleportation, homes, messaging, and market interaction features.

### Core Features

- **Teleportation System**: Player teleport requests and navigation
- **Home System**: Personal teleport locations based on player rank
- **Messaging System**: Private messaging and offline mail
- **Market Warps**: Public teleport points for player markets
- **Protection Features**: Integrated market area security
- **Death Management**: Location tracking and return capabilities

### Key Commands

```
# Teleportation
/bdtp <player>      # Request to teleport to player
/bdtpa              # Accept a teleport request
/bdspawn            # Go to server spawn point

# Homes
/bdhome             # Go to your default home
/bdhome set         # Set your current location as home
/bdhome list        # View all your homes

# Messaging
/bdmsg <p> <msg>    # Send private message
/bdmail send <p> <m> # Send offline mail

# Market Integration
/bdmarket warp <id>  # Teleport to a public market
```

### Configuration

```yaml
# Core settings in config.yml
bdvital:
  teleportation:
    delay: 3         # Seconds before teleport
    cooldown: 60     # Seconds between teleports
  homes:
    max-per-rank:    # Homes per player rank
      1: 1
      2: 2
      5: 5
  market-warps:
    enabled: true    # Enable market teleports
```

## BDLogging System

BDCraft includes a comprehensive built-in logging system that tracks all important actions within the plugin.

### Logged Actions

The logging system automatically tracks all key activities:

- BD crop planting and harvesting
- Market creation and modification
- Villager spawning and removal
- Trading activities (purchases and sales)
- Currency transactions
- Player rank changes
- Administrative commands
- BD item creation and usage

### Lookup Commands

```
# View all logs
/bdadmin logs list

# Filter logs by player
/bdadmin logs player <playername>

# Filter logs by action type
/bdadmin logs action <type>

# Filter logs by time period
/bdadmin logs time <hours>

# Filter logs by location
/bdadmin logs radius <blocks>

# Combined filters example
/bdadmin logs player Steve action trade time 24
```

## BDPerms System

BDCraft includes a comprehensive built-in permissions system that manages player access and groups.

### Permission Features

- Complete group-based permission system
- Permission inheritance between groups
- Temporary permissions with time expiration
- Player-specific permission overrides
- Permission context support (world, dimension)
- Rank-based permission automation

### Permission Configuration

```yaml
# In permissions.yml
groups:
  default:
    permissions:
      - bdcraft.market.create
      - bdcraft.market.use
      - bdcraft.rank.view
  vip:
    inherit: default
    permissions:
      - bdcraft.market.teleport
      - bdcraft.market.associate.10
      - bdcraft.market.size.2
  admin:
    permissions:
      - bdcraft.*

# Automatic rank permissions
rank-groups:
  1: default
  2: farmer
  3: expert_farmer
  4: master_farmer
  5: agricultural_expert
```

When a player reaches a new BD rank, they are automatically granted the corresponding permission group.

## Modular System Architecture

BDCraft uses a modular system architecture with components that work together seamlessly.

### Module Configuration

Each built-in system can be enabled or disabled in `config.yml`:

```yaml
modules:
  bdvital: true
  bdperms: true
  bdlogging: true
  bdeconomy: true
  bdplaceholders: true
```

### Startup Process

When BDCraft starts, it loads its self-contained modules in the correct order:

```
[BDCraft] Initializing BDCraft v5.0.0
[BDCraft] Loading BDVital module...
[BDCraft] Loading BDPerms module...
[BDCraft] Loading BDLogging module...
[BDCraft] Loading BDEconomy module...
[BDCraft] Loading BDPlaceholders module...
[BDCraft] All modules loaded successfully!
```

## Module Extension System

BDCraft's module system can be extended with additional features by server administrators, though the plugin does not support third-party plugin integration.

### Custom Data Handlers

Advanced users can implement custom data handlers for exporting data:

```java
// This is for reference only - BDCraft is fully self-contained
// and does not support external plugins

import com.bdcraft.api.module.BDModule;

public class CustomDataHandler {
    
    // Example method to export market data in custom format
    public void exportMarketData(String format, File destination) {
        // Data export implementation
    }
    
    // Example method to import market data from an external source
    public boolean importMarketData(File source) {
        // Data import implementation
        return true;
    }
}
```

## Troubleshooting Guide

### Common Issues

1. **Economy Issues**:
   - Check player has enough currency for operations
   - Verify market creation location is valid (not too close to other markets)
   - Make sure market token was placed correctly in an item frame

2. **Market Functionality**:
   - Confirm villagers spawned inside the market
   - Check that player has appropriate rank for operations
   - Verify block protection settings

3. **Permission Problems**:
   - Use `/bdadmin perms check <player> <permission>` to verify permissions
   - Check permission group assignments
   - Verify rank-based permission grants

### Diagnosing Problems

Enable debug mode to get more detailed logs about what's happening:

```
/bdadmin debug on
```

This will produce detailed logs about all plugin operations.

## Best Practices

1. **Plugin Setup**: Configure your server to handle the plugin efficiently
2. **Resource Usage**: Monitor server performance and adjust settings as needed
3. **Regular Maintenance**: Use built-in tools to manage player data and markets
4. **Backup Schedule**: Create regular backups of BDCraft data files
5. **Player Guidance**: Create clear tutorials for players on using markets

By properly configuring BDCraft's self-contained systems, you can provide a seamless farming-based economy experience for your players.