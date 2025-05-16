# Permissions Reference

BDCraft uses a comprehensive permission system to control access to various features. This guide lists all permissions organized by module.

## Core Permissions

These permissions control access to core plugin functionality:

| Permission | Description | Default |
|------------|-------------|---------|
| `bdcraft.admin` | Full administrative access to all plugin features | op |
| `bdcraft.reload` | Ability to reload the plugin configuration | op |
| `bdcraft.modules.manage` | Ability to enable/disable modules | op |
| `bdcraft.help` | Access to the help command | true |

## Economy Module Permissions

### General Economy

| Permission | Description | Default |
|------------|-------------|---------|
| `bdcraft.economy.use` | Basic economy commands and balance checking | true |
| `bdcraft.economy.pay` | Ability to pay other players | true |
| `bdcraft.economy.admin` | Administrative economy commands | op |
| `bdcraft.economy.baltop` | View the balance leaderboard | true |

### Market System

| Permission | Description | Default |
|------------|-------------|---------|
| `bdcraft.market.use` | Basic market usage and browsing | true |
| `bdcraft.market.sell` | Ability to sell items on the market | true |
| `bdcraft.market.admin` | Administrative market commands | op |

### Auction System

| Permission | Description | Default |
|------------|-------------|---------|
| `bdcraft.auction.use` | Basic auction usage and bidding | true |
| `bdcraft.auction.create` | Ability to create auctions | true |
| `bdcraft.auction.admin` | Administrative auction commands | op |
| `bdcraft.auction.featured` | Ability to create featured auctions | true |

### Villager System

| Permission | Description | Default |
|------------|-------------|---------|
| `bdcraft.villager.use` | Ability to interact with custom villagers | true |
| `bdcraft.villager.admin` | Administrative villager management | op |

## Progression Module Permissions

### Rank System

| Permission | Description | Default |
|------------|-------------|---------|
| `bdcraft.rank.use` | View rank information and progress | true |
| `bdcraft.rank.rankup` | Ability to rank up | true |
| `bdcraft.rank.admin` | Administrative rank commands | op |
| `bdcraft.rank.top` | View the rank leaderboard | true |

#### Rank-Specific Permissions

These permissions are automatically granted based on a player's rank:

| Permission | Description |
|------------|-------------|
| `bdcraft.rank.novice` | Granted to Novice and above |
| `bdcraft.rank.apprentice` | Granted to Apprentice and above |
| `bdcraft.rank.expert` | Granted to Expert and above |
| `bdcraft.rank.master` | Granted to Master and above |
| `bdcraft.rank.grandmaster` | Granted to Grandmaster and above |
| `bdcraft.rank.legend` | Granted to Legend rank |

### Rebirth System

| Permission | Description | Default |
|------------|-------------|---------|
| `bdcraft.rebirth.use` | Access to rebirth functionality | true |
| `bdcraft.rebirth.admin` | Administrative rebirth commands | op |
| `bdcraft.rebirth.top` | View the rebirth leaderboard | true |

## Vital Module Permissions

### Home System

| Permission | Description | Default |
|------------|-------------|---------|
| `bdcraft.home.use` | Basic home functionality | true |
| `bdcraft.home.admin` | Administrative home commands | op |
| `bdcraft.home.bypass-cooldown` | Bypass home teleport cooldowns | op |
| `bdcraft.home.others` | Access other players' homes | op |

#### Home Limit Permissions

These permissions control how many homes a player can have:

| Permission | Description | Default |
|------------|-------------|---------|
| `bdcraft.home.limit.1` | Allows 1 home | true |
| `bdcraft.home.limit.3` | Allows 3 homes | false |
| `bdcraft.home.limit.5` | Allows 5 homes | false |
| `bdcraft.home.limit.10` | Allows 10 homes | false |
| `bdcraft.home.limit.unlimited` | Unlimited homes | op |

### Teleport System

| Permission | Description | Default |
|------------|-------------|---------|
| `bdcraft.teleport.use` | Basic teleport functionality | true |
| `bdcraft.teleport.tpa` | Send teleport requests | true |
| `bdcraft.teleport.tpahere` | Request players to teleport to you | true |
| `bdcraft.teleport.back` | Teleport to your previous location | true |
| `bdcraft.teleport.spawn` | Teleport to spawn | true |
| `bdcraft.teleport.admin` | Administrative teleport commands | op |
| `bdcraft.teleport.bypass-cooldown` | Bypass teleport cooldowns | op |
| `bdcraft.teleport.bypass-warmup` | Bypass teleport warmup times | op |
| `bdcraft.teleport.bypass-combat` | Teleport during combat | op |
| `bdcraft.teleport.others` | Teleport other players | op |
| `bdcraft.teleport.random` | Use random teleportation | true |

### Chat System

| Permission | Description | Default |
|------------|-------------|---------|
| `bdcraft.chat.use` | Basic chat functionality | true |
| `bdcraft.chat.color` | Use color codes in chat | false |
| `bdcraft.chat.format` | Use formatting codes in chat | false |
| `bdcraft.chat.url` | Post clickable URLs in chat | false |
| `bdcraft.chat.bypass-cooldown` | Bypass chat cooldowns | op |
| `bdcraft.chat.admin` | Administrative chat commands | op |
| `bdcraft.chat.announce` | Make server announcements | op |
| `bdcraft.chat.broadcast` | Broadcast messages | op |
| `bdcraft.chat.me` | Use /me command | true |
| `bdcraft.chat.nick` | Set a nickname | false |

### Tab List System

| Permission | Description | Default |
|------------|-------------|---------|
| `bdcraft.tab.admin` | Administrative tab list commands | op |
| `bdcraft.tab.afk` | Use AFK status | true |
| `bdcraft.tab.afk.exempt` | Exempt from auto-AFK | op |
| `bdcraft.tab.color` | Use color in tab list name | false |

## Permission Inheritance

The plugin automatically sets up permission inheritance for rank-based permissions. For example, a player with the Master rank will automatically have the following permissions:

- `bdcraft.rank.novice`
- `bdcraft.rank.apprentice`
- `bdcraft.rank.expert`
- `bdcraft.rank.master`

This ensures that players with higher ranks have access to all lower rank permissions.

## Integration with Permission Plugins

BDCraft works with all major permission plugins including:

- LuckPerms
- PermissionsEx
- GroupManager
- UltraPermissions

No special setup is required - simply use your permission plugin to assign the appropriate permissions.

## Default Permission Configuration

The plugin ships with a balanced set of default permissions that can be customized in the `permissions.yml` file:

```yaml
# Default permissions configuration
defaults:
  # Core permissions
  bdcraft.help: true
  bdcraft.admin: op
  
  # Economy permissions
  bdcraft.economy.use: true
  bdcraft.economy.pay: true
  
  # Market permissions
  bdcraft.market.use: true
  bdcraft.market.sell: true
  
  # Progression permissions
  bdcraft.rank.use: true
  bdcraft.rank.rankup: true
  
  # Vital permissions
  bdcraft.home.use: true
  bdcraft.home.limit.1: true
  bdcraft.teleport.use: true
  bdcraft.teleport.tpa: true
  bdcraft.chat.use: true
```

## Permission Setup Guide

1. Decide which permissions you want to grant to different player groups
2. Configure your permission plugin to assign the appropriate permissions
3. Test the permissions to ensure they're working correctly
4. Adjust as needed based on your server's needs

For servers with a rank-based permission system, consider using the automatic rank permissions as a foundation and adding specific permissions as needed.