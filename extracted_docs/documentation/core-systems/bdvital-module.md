# BDVital Module

The BDVital module provides essential server utilities for the BDCraft plugin. This fully integrated module handles important quality-of-life features that enhance gameplay without requiring any external dependencies.

## Core Features

### Teleportation System

The teleportation system allows players to move around the server efficiently:

| Command | Description | Permission |
|---------|-------------|------------|
| `/bdtp <player>` | Request to teleport to a player | bdvital.tp |
| `/bdtpa` | Accept a teleport request | bdvital.tpa |
| `/bdtpd` | Deny a teleport request | bdvital.tpa |
| `/bdtpr <player>` | Request a player to teleport to you | bdvital.tpr |
| `/bdback` | Return to previous location | bdvital.back |
| `/bdspawn` | Teleport to server spawn | bdvital.spawn |

Players receive teleport requests with buttons to easily accept or deny:

```
[Player123] has requested to teleport to you.
[Accept] [Deny] (expires in 30s)
```

### Home System

Players can set multiple personal locations to easily return to:

| Command | Description | Permission |
|---------|-------------|------------|
| `/bdhome` | Teleport to your default home | bdvital.home |
| `/bdhome <name>` | Teleport to a specific home | bdvital.home |
| `/bdhome set` | Set your default home | bdvital.sethome |
| `/bdhome set <name>` | Set a named home | bdvital.sethome |
| `/bdhome del <name>` | Delete a specific home | bdvital.sethome |
| `/bdhome list` | List all your homes | bdvital.home |

Home limits are based on player ranks:
- Rank 1: 1 home
- Rank 2: 2 homes
- Rank 3: 3 homes
- Rank 4: 4 homes
- Rank 5: 5 homes

### Messaging System

The private messaging system facilitates player communication:

| Command | Description | Permission |
|---------|-------------|------------|
| `/bdmsg <player> <message>` | Send a private message | bdvital.msg |
| `/bdr <message>` | Reply to last private message | bdvital.msg |
| `/bdmail send <player> <message>` | Send offline mail | bdvital.mail |
| `/bdmail read` | Read your mail | bdvital.mail |
| `/bdmail clear` | Clear your mailbox | bdvital.mail |

Unread mail is displayed when players log in:

```
[BDVital] You have 2 unread messages. Use /bdmail read to view them.
```

### Market Warps

Players can set public market warps for their markets:

| Command | Description | Permission |
|---------|-------------|------------|
| `/bdmarket warp <name>` | Teleport to a market | bdvital.market.warp |
| `/bdmarket warp set` | Set your market warp (market founders only) | bdvital.market.setwarp |
| `/bdmarket warp remove` | Remove your market warp | bdvital.market.setwarp |
| `/bdmarket warp list` | List all public market warps | bdvital.market.warp |

Market warps are limited by market level:
- Level 1: No public warp
- Level 2: Public warp with 1 hour cooldown
- Level 3: Public warp with 30 minute cooldown
- Level 4: Public warp with 15 minute cooldown

### Protection System

BDVital includes integrated protection features:

#### Market Protection

Player markets automatically protect the area within their boundaries:

- Only the founder and associates can build within market boundaries
- Only the founder can add or remove associates
- Players cannot break or place blocks in others' markets without permission
- PvP is disabled within markets (configurable)

#### Anti-Grief Features

The module provides additional anti-grief functionality:

- Block interaction logging for protected areas
- Rollback capabilities for admins
- Alert system for suspicious behavior

| Command | Description | Permission |
|---------|-------------|------------|
| `/bdadmin inspect` | Toggle inspection mode | bdadmin.inspect |
| `/bdadmin rollback <area> <time>` | Rollback changes in an area | bdadmin.rollback |
| `/bdadmin alert <message>` | Send staff alert | bdadmin.alert |

### Death Management

The death management system helps players recover from death:

| Command | Description | Permission |
|---------|-------------|------------|
| `/bdback` | Return to death location | bdvital.back |
| `/bddeathinfo` | Show coordinates of last death | bdvital.deathinfo |

Players receive death coordinates in chat upon death:

```
[BDVital] You died at X: 125, Y: 64, Z: -250. Use /bdback to return.
```

## Configuration

### BDVital Config

```yaml
bdvital:
  teleportation:
    delay: 3                   # Seconds before teleport occurs
    cooldown: 60               # Seconds between teleport commands
    request-expiry: 30         # Seconds before request expires
    spawn-location:
      world: "world"          # World name for the spawn location
      x: 0                     # X coordinate
      y: 100                   # Y coordinate
      z: 0                     # Z coordinate
  
  homes:
    max-per-rank:              # Maximum homes per rank
      1: 1
      2: 2
      3: 3
      4: 4
      5: 5
    cooldown: 120              # Seconds between home teleports
  
  messaging:
    disabled-worlds: []        # Worlds where messaging is disabled
    mail-expiry: 7             # Days before mail expires
  
  market-warps:
    enabled: true              # Enable market warp system
    cooldowns:                 # Cooldowns per market level (minutes)
      2: 60
      3: 30
      4: 15
  
  protection:
    log-blocks: true           # Log block changes in protected areas
    log-retention: 30          # Days to keep logs
    disable-pvp-in-markets: true # Disable PvP in markets

  death:
    back-cooldown: 300         # Seconds between death back commands
    save-inventory: false      # Save inventory on death (admin recovery)
```

## BDVital Commands

### Player Commands

```
/bdtp <player>        # Request to teleport to a player
/bdtpa                # Accept a teleport request
/bdtpd                # Deny a teleport request
/bdtpr <player>       # Request a player to teleport to you
/bdback               # Return to previous location or death point
/bdspawn              # Teleport to server spawn

/bdhome               # Teleport to your default home
/bdhome <name>        # Teleport to a specific home
/bdhome set           # Set your default home
/bdhome set <name>    # Set a named home
/bdhome del <name>    # Delete a specific home
/bdhome list          # List all your homes

/bdmsg <player> <msg> # Send a private message
/bdr <message>        # Reply to last message
/bdmail send <p> <m>  # Send offline mail
/bdmail read          # Read your mail
/bdmail clear         # Clear your mailbox

/bdmarket warp <name> # Teleport to a market
/bdmarket warp list   # List all public market warps

/bddeathinfo          # Show coordinates of last death
```

### Admin Commands

```
/bdadmin tp <p1> <p2> # Force teleport player1 to player2
/bdadmin spawn set    # Set the server spawn point

/bdadmin home list <p> # List a player's homes
/bdadmin home del <p> <h> # Delete a player's home

/bdadmin mail read <p> # Read a player's mail
/bdadmin mail clear <p> # Clear a player's mailbox

/bdadmin protect check # Check protection status
/bdadmin inspect       # Toggle inspection mode
/bdadmin rollback <area> <time> # Rollback changes

/bdadmin restore <player> # Restore a player's inventory
```

## Integration with Other Modules

### BDEconomy Integration

BDVital integrates with the BDEconomy module to provide economy-based features:

- Market warp costs based on distance
- Paid teleportation for instant teleports
- Home slot purchases

### BDPerms Integration

BDVital uses the BDPerms system for permission management:

- Rank-based home limits
- Market protection permissions
- Command access control

## Technical Implementation

### Data Storage

BDVital stores user data in JSON format:

```
plugins/BDCraft/data/bdvital/homes.json    # Player homes
plugins/BDCraft/data/bdvital/warps.json    # Market warps
plugins/BDCraft/data/bdvital/teleports.json # Teleport history
plugins/BDCraft/data/bdvital/mail.json     # Player mail
plugins/BDCraft/data/bdvital/deathpoints.json # Death locations
```

### Protection System Implementation

Market protection uses a lightweight event-based system:

1. Block events (break, place, interact) are intercepted
2. The plugin checks if the location is within a market boundary
3. Player permissions are verified against market access rights
4. Event is allowed or canceled based on permissions
5. Actions are logged if logging is enabled

### Command Cooldowns

All teleportation commands use a cooldown system to prevent abuse:

```java
cooldownManager.setCooldown(player.getUniqueId(), "tp", teleportCooldown);
```

Cooldowns are stored in memory and cleared on server restart or plugin reload.

## Placeholders

BDVital provides several placeholders for use in chat plugins or other integrations:

- `%bdvital_home_count%` - Number of homes a player has set
- `%bdvital_home_max%` - Maximum number of homes a player can have
- `%bdvital_mail_count%` - Number of unread mail messages
- `%bdvital_tp_cooldown%` - Remaining teleport cooldown in seconds
- `%bdvital_back_available%` - Whether the back command is available (true/false)
- `%bdvital_market_warp_count%` - Number of available market warps

## Best Practices

### Performance Considerations

- Teleportation requests are stored in memory to reduce database access
- Protection checks use spatial indexing for efficient location lookups
- Cooldown data is cached with short expiration times

### Security Features

- Teleport requests require explicit acceptance
- Protection logs include player UUID, timestamp, and action type
- Admin commands are carefully permission-restricted

BDVital provides essential server features in a seamless, integrated package that works harmoniously with the BDEconomy and BDPerms modules.