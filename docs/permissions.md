# Permissions

BDCraft uses a comprehensive permission system to control access to various features. Below is a complete list of all permissions available in the plugin.

## Core Permissions

| Permission | Description |
|------------|-------------|
| `bdcraft.admin` | Grants access to all administrative commands and features |
| `bdcraft.reload` | Allows reloading the plugin configuration |
| `bdcraft.update` | Allows checking for plugin updates |

## Economy Module Permissions

### General Economy

| Permission | Description |
|------------|-------------|
| `bdcraft.economy.use` | Allows using basic economy commands |
| `bdcraft.economy.admin` | Grants access to economy admin commands |
| `bdcraft.economy.pay` | Allows paying other players |
| `bdcraft.economy.balance` | Allows checking own balance |
| `bdcraft.economy.balance.others` | Allows checking others' balances |
| `bdcraft.economy.top` | Allows viewing top balances |

### Market System

| Permission | Description |
|------------|-------------|
| `bdcraft.market.use` | Allows basic market usage (check, info, list) |
| `bdcraft.market.donor` | Allows accessing market management remotely (donors only) |
| `bdcraft.market.multiple` | Allows creating multiple markets |
| `bdcraft.market.admin` | Grants admin access to all markets |

### Auction System

| Permission | Description |
|------------|-------------|
| `bdcraft.auction.use` | Allows using the auction house |
| `bdcraft.auction.sell` | Allows selling items on the auction house |
| `bdcraft.auction.cancel` | Allows canceling own listings |
| `bdcraft.auction.list` | Allows viewing own auction listings |
| `bdcraft.auction.admin` | Grants admin access to the auction system |

### Villager System

| Permission | Description |
|------------|-------------|
| `bdcraft.villager.use` | Allows interaction with BD villagers |
| `bdcraft.villager.dealer` | Allows trading with BD Dealers |
| `bdcraft.villager.collector` | Allows trading with Collectors |
| `bdcraft.villager.seasonal` | Allows trading with Seasonal Traders |
| `bdcraft.villager.admin` | Grants admin access to villager management |

## Progression Module Permissions

### Rank System

| Permission | Description |
|------------|-------------|
| `bdcraft.rank.use` | Allows viewing rank information |
| `bdcraft.rank.progress` | Allows checking rank progress |
| `bdcraft.rank.benefits` | Allows viewing rank benefits |
| `bdcraft.rank.admin` | Grants admin access to rank management |

### Rebirth System

| Permission | Description |
|------------|-------------|
| `bdcraft.rebirth.use` | Allows using the rebirth system |
| `bdcraft.rebirth.info` | Allows viewing rebirth information |
| `bdcraft.rebirth.requirements` | Allows checking rebirth requirements |
| `bdcraft.rebirth.admin` | Grants admin access to rebirth management |

## Vital Module Permissions

### Home System

| Permission | Description |
|------------|-------------|
| `bdcraft.home.use` | Allows basic home usage |
| `bdcraft.home.set` | Allows setting homes |
| `bdcraft.home.delete` | Allows deleting own homes |
| `bdcraft.home.list` | Allows listing own homes |
| `bdcraft.home.multiple.X` | Allows setting X number of homes |
| `bdcraft.home.others` | Allows managing others' homes |
| `bdcraft.home.bypass` | Bypasses home restrictions |

### Teleport System

| Permission | Description |
|------------|-------------|
| `bdcraft.teleport.use` | Allows basic teleportation |
| `bdcraft.teleport.tp` | Allows teleporting to players |
| `bdcraft.teleport.tpa` | Allows requesting teleports |
| `bdcraft.teleport.tpahere` | Allows requesting others to teleport to you |
| `bdcraft.teleport.back` | Allows using the /back command |
| `bdcraft.teleport.warp` | Allows using warps |
| `bdcraft.teleport.spawn` | Allows teleporting to spawn |
| `bdcraft.teleport.bypass-cooldown` | Bypasses teleport cooldowns |
| `bdcraft.teleport.others` | Allows teleporting others |

### Chat System

| Permission | Description |
|------------|-------------|
| `bdcraft.chat.global` | Allows using global chat |
| `bdcraft.chat.local` | Allows using local chat |
| `bdcraft.chat.market` | Allows using market chat |
| `bdcraft.chat.admin` | Allows using admin chat |
| `bdcraft.chat.msg` | Allows private messaging |
| `bdcraft.chat.reply` | Allows replying to messages |
| `bdcraft.chat.ignore` | Allows ignoring players |
| `bdcraft.chat.color` | Allows using color in chat |
| `bdcraft.chat.format` | Allows using formatting in chat |
| `bdcraft.chat.clear` | Allows clearing personal chat |
| `bdcraft.chat.clear.all` | Allows clearing everyone's chat |
| `bdcraft.chat.mute` | Allows muting global chat |
| `bdcraft.chat.mute.player` | Allows muting players |
| `bdcraft.chat.unmute` | Allows unmuting players |
| `bdcraft.chat.bypass-cooldown` | Bypasses chat cooldowns |

### Tab System

| Permission | Description |
|------------|-------------|
| `bdcraft.tab.admin` | Allows administering the tab list |
| `bdcraft.tab.color` | Allows colored names in tab list |
| `bdcraft.tab.bypass-hide` | Shows players even when hidden |

## Permission Inheritance

BDCraft uses permission inheritance to simplify permission management. For example:

- `bdcraft.admin` grants all permissions
- `bdcraft.economy.admin` grants all economy-related permissions
- `bdcraft.market.admin` grants all market-related permissions

## Default Permissions

The following permissions are granted to all players by default:

- `bdcraft.economy.use`
- `bdcraft.economy.balance`
- `bdcraft.economy.pay`
- `bdcraft.market.use`
- `bdcraft.auction.use`
- `bdcraft.villager.use`
- `bdcraft.rank.use`
- `bdcraft.home.use`
- `bdcraft.home.set`
- `bdcraft.home.multiple.1`
- `bdcraft.teleport.use`
- `bdcraft.teleport.tpa`
- `bdcraft.teleport.spawn`
- `bdcraft.chat.global`
- `bdcraft.chat.local`
- `bdcraft.chat.msg`
- `bdcraft.chat.reply`
- `bdcraft.chat.ignore`

## Permission System

BDCraft includes a comprehensive built-in permission system that does not require any external plugins. The permission system:

1. **Self-Contained** - Fully manages all permissions without external dependencies
2. **Groups-Based** - Organizes permissions into logical groups
3. **Inheritance Support** - Allows groups to inherit permissions from other groups
4. **UUID-Based** - Tracks players by their unique IDs for persistence
5. **Negation Support** - Supports removing specific permissions with `-permission.node` syntax

For detailed information on how to configure and use the permission system, see [Permission System](permission-system.md).

## Permission Groups

BDCraft's internal permission system comes with pre-configured permission groups. Here are recommended permission group setups:

### Member Group
```
bdcraft.economy.use
bdcraft.economy.balance
bdcraft.economy.pay
bdcraft.market.use
bdcraft.auction.use
bdcraft.villager.use
bdcraft.rank.use
bdcraft.rebirth.use
bdcraft.home.use
bdcraft.home.set
bdcraft.home.multiple.1
bdcraft.teleport.use
bdcraft.teleport.tpa
bdcraft.teleport.spawn
bdcraft.chat.*
```

### Donor Group
```
(All Member permissions)
bdcraft.home.multiple.3
bdcraft.teleport.back
bdcraft.chat.color
bdcraft.market.donor
```

### Admin Group
```
bdcraft.admin
```