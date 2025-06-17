# Permissions

BDCraft includes a comprehensive built-in permission system with GUI management, command-based editing, and YAML configuration. The system provides group-based access control with inheritance and individual player overrides.

## Permission Management

### GUI Management
- **Main Command**: `/bdpermissions` - Opens interactive permission management GUI
- **Player Management**: `/bdpermissions player <player>` - Edit specific player permissions
- **Group Management**: `/bdpermissions group <group>` - Manage group permissions and members

### Command Management
- **Set Permissions**: `/bdperm set <player> <permission> <true/false>` - Grant or revoke individual permissions
- **Group Operations**: `/bdgroup add <player> <group>` - Add players to permission groups
- **Check Permissions**: `/bdperm check <player> <permission>` - Verify permission status

### YAML Configuration
Permissions can be configured directly in `permissions.yml` for bulk operations and automation.

## Permission System Role

**Important:** BDCraft ranks automatically provide economy benefits, chat formatting, and progression features. The permission system handles administrative and utility commands beyond rank benefits.

## Essential Player Permissions

| Permission | Description |
|------------|-------------|
| `bdcraft.economy.use` | Basic economy access (required for all players to use BD systems) |
| `bdcraft.chat.global` | Chat participation (required for server communication) |
| `bdcraft.spawn.use` | Spawn teleportation (required for basic navigation) |

**Note:** Market access, auction benefits, villager trading, and rank progression are automatically granted based on player rank. No additional permissions needed.

## Admin Permissions

| Permission | Description |
|------------|-------------|
| `bdcraft.admin` | Full admin access (includes everything) |
| `bdcraft.permissions.admin` | Permission management system access |

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
| `bdcraft.market.donor` | Allows accessing market management remotely (donors only) |
| `bdcraft.market.admin` | Grants admin access to all markets |

**Note:** Market usage, creation, and upgrades are controlled by rank requirements, not permissions.

### Auction System

| Permission | Description |
|------------|-------------|
| `bdcraft.auction.featured` | Allows creating featured auction listings (donor feature) |
| `bdcraft.auction.admin` | Grants admin access to the auction system |

**Note:** Auction house access and benefits (fees, duration) are controlled by rank, not permissions.

### Villager System

| Permission | Description |
|------------|-------------|
| `bdcraft.villager.admin` | Grants admin access to villager management |

**Note:** Villager trading is available to all players. Specific trades unlock based on rank requirements.

## Progression Module Permissions

### Rank System

| Permission | Description |
|------------|-------------|
| `bdcraft.rank.admin` | Grants admin access to rank management |

**Note:** All players can view rank information, progress, and benefits by default. Ranks automatically provide their own benefits.

### Rebirth System

| Permission | Description |
|------------|-------------|
| `bdcraft.rebirth.admin` | Grants admin access to rebirth management |

**Note:** All players with Agricultural Expert rank can use rebirth system, view information, and check requirements automatically.

## Vital Module Permissions

### Home System

| Permission | Description |
|------------|-------------|
| `bdcraft.home.use` | Allows basic home usage |
| `bdcraft.home.set` | Allows setting homes |
| `bdcraft.home.delete` | Allows deleting own homes |
| `bdcraft.home.list` | Allows listing own homes |
| `bdcraft.home.limit` | Allows viewing home limits |
| `bdcraft.home.multiple.X` | Allows setting X number of homes (replace X with number) |
| `bdcraft.home.others` | Allows managing others' homes |
| `bdcraft.home.bypass` | Bypasses home restrictions |

### Teleport System

| Permission | Description |
|------------|-------------|
| `bdcraft.teleport.use` | Allows basic teleportation |
| `bdcraft.teleport.player` | Allows teleporting to players |
| `bdcraft.teleport.location` | Allows teleporting to coordinates |
| `bdcraft.teleport.others` | Allows teleporting others |
| `bdcraft.teleport.request` | Allows requesting teleports (tpa) |
| `bdcraft.teleport.requesthere` | Allows requesting others to teleport to you (tpahere) |
| `bdcraft.teleport.accept` | Allows accepting teleport requests |
| `bdcraft.teleport.deny` | Allows denying teleport requests |
| `bdcraft.teleport.cancel` | Allows canceling teleport requests |
| `bdcraft.teleport.toggle` | Allows toggling teleport request acceptance |
| `bdcraft.teleport.back` | Allows using the /back command |
| `bdcraft.teleport.random` | Allows using random teleport |
| `bdcraft.teleport.bypass-cooldown` | Bypasses teleport cooldowns |

### Warp System

| Permission | Description |
|------------|-------------|
| `bdcraft.warp.use` | Allows using warps |
| `bdcraft.warp.list` | Allows listing all warps |
| `bdcraft.warp.info` | Allows viewing warp information |
| `bdcraft.warp.set` | Allows creating warps |
| `bdcraft.warp.delete` | Allows deleting warps |

### Spawn System

| Permission | Description |
|------------|-------------|
| `bdcraft.spawn.use` | Allows teleporting to spawn |
| `bdcraft.spawn.set` | Allows setting spawn location |

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
| `bdcraft.chat.unignore` | Allows unignoring players |
| `bdcraft.chat.mail` | Allows using the mail system |
| `bdcraft.chat.nick` | Allows setting own nickname |
| `bdcraft.chat.nick.others` | Allows setting others' nicknames |
| `bdcraft.chat.color` | Allows using color in chat |
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

### Server Management

| Permission | Description |
|------------|-------------|
| `bdcraft.vital.ban` | Allows banning players |
| `bdcraft.vital.unban` | Allows unbanning players |
| `bdcraft.vital.tempban` | Allows temporarily banning players |
| `bdcraft.vital.kick` | Allows kicking players |
| `bdcraft.vital.broadcast` | Allows broadcasting messages |
| `bdcraft.vital.motd` | Allows viewing/setting MOTD |

### Player Utilities

| Permission | Description |
|------------|-------------|
| `bdcraft.vital.feed` | Allows feeding yourself |
| `bdcraft.vital.feed.others` | Allows feeding other players |
| `bdcraft.vital.heal` | Allows healing yourself |
| `bdcraft.vital.heal.others` | Allows healing other players |
| `bdcraft.vital.fly` | Allows flight mode |
| `bdcraft.vital.fly.others` | Allows setting flight for others |
| `bdcraft.vital.fly.speed` | Allows changing flight speed |
| `bdcraft.vital.god` | Allows god mode (invincibility) |
| `bdcraft.vital.vanish` | Allows vanishing from other players |
| `bdcraft.vital.speed` | Allows changing movement speed |
| `bdcraft.vital.gamemode` | Allows changing own gamemode |
| `bdcraft.vital.gamemode.others` | Allows changing others' gamemode |

### Item Management

| Permission | Description |
|------------|-------------|
| `bdcraft.vital.clearinventory` | Allows clearing inventories |
| `bdcraft.vital.enderchest` | Allows accessing enderchest |
| `bdcraft.vital.endersee` | Allows viewing others' enderchests |
| `bdcraft.vital.invsee` | Allows viewing others' inventories |
| `bdcraft.vital.workbench` | Allows opening crafting table remotely |
| `bdcraft.vital.anvil` | Allows opening anvil remotely |
| `bdcraft.vital.repair` | Allows repairing items |
| `bdcraft.vital.repair.all` | Allows repairing all items |
| `bdcraft.vital.hat` | Allows wearing blocks as hats |
| `bdcraft.vital.more` | Allows increasing item stack size |

### Item Information and Modification

| Permission | Description |
|------------|-------------|
| `bdcraft.vital.itemdb` | Allows viewing item database information |
| `bdcraft.vital.itemname` | Allows setting custom item names |
| `bdcraft.vital.itemlore` | Allows setting custom item lore |
| `bdcraft.vital.enchant` | Allows adding enchantments to items |

### Experience and Kits

| Permission | Description |
|------------|-------------|
| `bdcraft.vital.moreexp` | Allows giving experience to yourself |
| `bdcraft.vital.moreexp.others` | Allows giving experience to others |
| `bdcraft.vital.kit` | Allows using kits |
| `bdcraft.vital.kits` | Allows listing available kits |

### World Management

| Permission | Description |
|------------|-------------|
| `bdcraft.vital.time` | Allows changing world time |
| `bdcraft.vital.weather` | Allows changing weather |
| `bdcraft.vital.lightning` | Allows striking lightning |
| `bdcraft.vital.spawner` | Allows modifying spawners |

### Player Information

| Permission | Description |
|------------|-------------|
| `bdcraft.vital.seen` | Allows checking when players were last online |
| `bdcraft.vital.ping` | Allows checking own ping |
| `bdcraft.vital.ping.others` | Allows checking others' ping |
| `bdcraft.vital.near` | Allows seeing nearby players |
| `bdcraft.vital.kill` | Allows killing players |
| `bdcraft.vital.suicide` | Allows committing suicide |

### Brewing and Potions

| Permission | Description |
|------------|-------------|
| `bdcraft.vital.potionbrew` | Allows brewing custom potions |

## Donor-Specific Permissions

These permissions are typically granted to donors and provide enhanced gameplay benefits:

| Permission | Description |
|------------|-------------|
| `bdcraft.market.donor` | Remote market GUI access |
| `bdcraft.auction.discount` | Fixed 2% auction fees |
| `bdcraft.rank.discount` | 15% discount on rank purchases |
| `bdcraft.rebirth.retention` | Keep 20% currency during rebirth |
| `bdcraft.home.multiple.3` | Allows setting 3 homes |
| `bdcraft.teleport.back` | Access to /back command |
| `bdcraft.chat.color` | Colored chat usage |

## Permission Inheritance

BDCraft uses permission inheritance to simplify permission management:

- `bdcraft.admin` grants all permissions
- `bdcraft.economy.admin` grants all economy-related permissions
- `bdcraft.market.admin` grants all market-related permissions
- `bdcraft.auction.admin` grants all auction-related permissions
- `bdcraft.villager.admin` grants all villager-related permissions
- `bdcraft.rank.admin` grants all rank-related permissions
- `bdcraft.rebirth.admin` grants all rebirth-related permissions
- `bdcraft.vital.*` grants all vital module permissions

## Default Permissions

The following permissions are granted to all players by default:

```yaml
default_permissions:
  - bdcraft.economy.use
  - bdcraft.economy.balance
  - bdcraft.economy.pay
  - bdcraft.market.use
  - bdcraft.auction.use
  - bdcraft.auction.sell
  - bdcraft.auction.cancel
  - bdcraft.auction.list
  - bdcraft.villager.use
  - bdcraft.villager.dealer
  - bdcraft.villager.collector
  - bdcraft.rank.use
  - bdcraft.rank.progress
  - bdcraft.rank.benefits
  - bdcraft.rebirth.use
  - bdcraft.rebirth.info
  - bdcraft.rebirth.requirements
  - bdcraft.home.use
  - bdcraft.home.set
  - bdcraft.home.delete
  - bdcraft.home.list
  - bdcraft.home.multiple.1
  - bdcraft.teleport.use
  - bdcraft.teleport.request
  - bdcraft.teleport.requesthere
  - bdcraft.teleport.accept
  - bdcraft.teleport.deny
  - bdcraft.teleport.cancel
  - bdcraft.teleport.toggle
  - bdcraft.warp.use
  - bdcraft.warp.list
  - bdcraft.spawn.use
  - bdcraft.chat.global
  - bdcraft.chat.local
  - bdcraft.chat.msg
  - bdcraft.chat.reply
  - bdcraft.chat.ignore
  - bdcraft.chat.unignore
  - bdcraft.chat.mail
  - bdcraft.chat.clear
```

## Permission System

BDCraft includes a comprehensive built-in permission system that does not require any external plugins. The permission system:

1. **Self-Contained** - Fully manages all permissions without external dependencies
2. **Groups-Based** - Organizes permissions into logical groups
3. **Inheritance Support** - Allows groups to inherit permissions from other groups
4. **UUID-Based** - Tracks players by their unique IDs for persistence
5. **Negation Support** - Supports removing specific permissions with `-permission.node` syntax

For detailed permission lists and configuration options, refer to the sections above or contact your server administrator.

## Recommended Permission Groups

### Default Group
```yaml
default:
  permissions:
    - bdcraft.economy.use
    - bdcraft.economy.balance
    - bdcraft.economy.pay
    - bdcraft.market.use
    - bdcraft.auction.use
    - bdcraft.auction.sell
    - bdcraft.auction.cancel
    - bdcraft.auction.list
    - bdcraft.villager.use
    - bdcraft.villager.dealer
    - bdcraft.villager.collector
    - bdcraft.rank.use
    - bdcraft.rank.progress
    - bdcraft.rebirth.use
    - bdcraft.rebirth.info
    - bdcraft.home.use
    - bdcraft.home.set
    - bdcraft.home.multiple.1
    - bdcraft.teleport.use
    - bdcraft.teleport.request
    - bdcraft.teleport.requesthere
    - bdcraft.teleport.accept
    - bdcraft.teleport.deny
    - bdcraft.warp.use
    - bdcraft.spawn.use
    - bdcraft.chat.global
    - bdcraft.chat.local
    - bdcraft.chat.msg
    - bdcraft.chat.reply
    - bdcraft.chat.ignore
    - bdcraft.chat.mail
```

### Donor Group
```yaml
donor:
  inherit: default
  permissions:
    - bdcraft.home.multiple.3
    - bdcraft.teleport.back
    - bdcraft.chat.color
    - bdcraft.market.donor
    - bdcraft.auction.discount
    - bdcraft.rank.discount
    - bdcraft.rebirth.retention
    - bdcraft.villager.seasonal
```

### Admin Group
```yaml
admin:
  inherit: donor
  permissions:
    - bdcraft.admin
```

---

This comprehensive permission list covers all features available in BDCraft. Use BDCraft's built-in permission system or configure these permissions in your preferred permission management plugin.