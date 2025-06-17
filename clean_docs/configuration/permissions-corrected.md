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

## How BDCraft Permissions Work

**Important:** BDCraft ranks automatically provide economy benefits, chat formatting, and progression features. The permission system handles administrative and utility commands beyond rank benefits.

### What Ranks Handle Automatically
- **Economy Access**: Market usage, auction access, villager trading
- **Chat Formatting**: `[Newcomer]`, `[Farmer]`, `[Expert Farmer]`, `[Master Farmer]`, `[Agricultural Expert]`
- **Economic Benefits**: Yield bonuses, reduced auction fees, extended listing durations
- **Progression Features**: Rank viewing, achievement tracking, rebirth eligibility

### What Permissions Control
- **Administrative Commands**: `/bdadmin`, `/bdeco give`, `/bdrank set`
- **Moderation Tools**: `/kick`, `/ban`, `/mute`, `/heal`, `/fly`
- **Utility Commands**: Home management, teleportation, special features
- **Donor Features**: Remote market access, enhanced limits
- **System Access**: Permission management, configuration editing

## Essential Player Permissions

| Permission | Description |
|------------|-------------|
| `bdcraft.economy.use` | Basic economy access (required for all players to use BD systems) |
| `bdcraft.chat.global` | Global chat participation |
| `bdcraft.spawn.use` | Spawn teleportation |

## Admin Permissions

| Permission | Description |
|------------|-------------|
| `bdcraft.admin` | Full admin access (includes everything) |
| `bdcraft.permissions.admin` | Permission management system access |

## Economy Module Permissions

### General Economy

| Permission | Description |
|------------|-------------|
| `bdcraft.economy.use` | Basic economy access (required for all players) |
| `bdcraft.economy.admin` | Economy admin commands |
| `bdcraft.economy.pay` | Pay other players |
| `bdcraft.economy.balance.others` | Check others' balances |
| `bdcraft.economy.top` | View top balances |

### Market System

| Permission | Description |
|------------|-------------|
| `bdcraft.market.donor` | Remote market access via `/bdmarket` (donors only) |
| `bdcraft.market.admin` | Admin access to all markets |

### Auction System

| Permission | Description |
|------------|-------------|
| `bdcraft.auction.featured` | Create featured auction listings (donor feature) |
| `bdcraft.auction.admin` | Admin access to auction system |

### Villager System

| Permission | Description |
|------------|-------------|
| `bdcraft.villager.admin` | Admin access to villager management |

## Progression Module Permissions

### Rank System

| Permission | Description |
|------------|-------------|
| `bdcraft.rank.admin` | Admin access to rank management |

### Rebirth System

| Permission | Description |
|------------|-------------|
| `bdcraft.rebirth.admin` | Admin access to rebirth management |

## Vital Module Permissions

### Home System

| Permission | Description |
|------------|-------------|
| `bdcraft.home.use` | Basic home usage |
| `bdcraft.home.set` | Setting homes |
| `bdcraft.home.set.multiple` | Setting multiple homes (donors) |
| `bdcraft.home.delete` | Deleting own homes |
| `bdcraft.home.list` | Listing own homes |
| `bdcraft.home.others` | Managing others' homes (admin) |

### Teleportation System

| Permission | Description |
|------------|-------------|
| `bdcraft.teleport.player` | Teleport to players (admin) |
| `bdcraft.teleport.location` | Teleport to coordinates (admin) |
| `bdcraft.teleport.others` | Teleport others (admin) |
| `bdcraft.teleport.request` | Request teleports (tpa) |
| `bdcraft.teleport.requesthere` | Request others teleport to you (tpahere) |
| `bdcraft.teleport.accept` | Accept teleport requests |
| `bdcraft.teleport.deny` | Deny teleport requests |
| `bdcraft.teleport.cancel` | Cancel teleport requests |
| `bdcraft.teleport.toggle` | Toggle teleport request acceptance |
| `bdcraft.teleport.back` | Use /back command |
| `bdcraft.teleport.random` | Use random teleport |
| `bdcraft.teleport.bypass-cooldown` | Bypass teleport cooldowns (donors) |

### Warp System

| Permission | Description |
|------------|-------------|
| `bdcraft.warp.use` | Use warps |
| `bdcraft.warp.list` | List all warps |
| `bdcraft.warp.info` | View warp information |
| `bdcraft.warp.set` | Create warps (admin) |
| `bdcraft.warp.delete` | Delete warps (admin) |

### Spawn System

| Permission | Description |
|------------|-------------|
| `bdcraft.spawn.use` | Teleport to spawn |
| `bdcraft.spawn.set` | Set spawn location (admin) |

### Chat System

| Permission | Description |
|------------|-------------|
| `bdcraft.chat.global` | Use global chat |
| `bdcraft.chat.local` | Use local chat |
| `bdcraft.chat.market` | Use market chat |
| `bdcraft.chat.admin` | Use admin chat |
| `bdcraft.chat.msg` | Private messaging |
| `bdcraft.chat.reply` | Reply to messages |
| `bdcraft.chat.ignore` | Ignore players |
| `bdcraft.chat.mail` | Use mail system |
| `bdcraft.chat.nick` | Set own nickname |
| `bdcraft.chat.nick.others` | Set others' nicknames (admin) |
| `bdcraft.chat.color` | Use colored chat (donors) |
| `bdcraft.chat.clear` | Clear personal chat |
| `bdcraft.chat.clear.all` | Clear everyone's chat (admin) |
| `bdcraft.chat.mute` | Mute global chat (admin) |
| `bdcraft.chat.mute.player` | Mute players (admin) |
| `bdcraft.chat.unmute` | Unmute players (admin) |

### Utility Commands

| Permission | Description |
|------------|-------------|
| `bdcraft.vital.fly` | Flight mode |
| `bdcraft.vital.fly.others` | Set flight for others (admin) |
| `bdcraft.vital.heal` | Heal yourself |
| `bdcraft.vital.heal.others` | Heal others (admin) |
| `bdcraft.vital.feed` | Feed yourself |
| `bdcraft.vital.feed.others` | Feed others (admin) |
| `bdcraft.vital.god` | God mode |
| `bdcraft.vital.vanish` | Vanish mode |
| `bdcraft.vital.speed` | Set movement speed |
| `bdcraft.vital.gamemode` | Change game mode (admin) |
| `bdcraft.vital.gamemode.others` | Change others' game mode (admin) |

### Moderation

| Permission | Description |
|------------|-------------|
| `bdcraft.vital.kick` | Kick players |
| `bdcraft.vital.ban` | Ban players |
| `bdcraft.vital.unban` | Unban players |
| `bdcraft.vital.tempban` | Temporarily ban players |
| `bdcraft.vital.broadcast` | Broadcast messages |

### Item Management

| Permission | Description |
|------------|-------------|
| `bdcraft.vital.repair` | Repair items |
| `bdcraft.vital.repair.all` | Repair all items |
| `bdcraft.vital.hat` | Wear items as hats |
| `bdcraft.vital.itemname` | Rename items |
| `bdcraft.vital.enchant` | Enchant items |
| `bdcraft.vital.more` | Fill item stacks |

### Virtual Interfaces

| Permission | Description |
|------------|-------------|
| `bdcraft.vital.workbench` | Portable crafting table |
| `bdcraft.vital.anvil` | Portable anvil |
| `bdcraft.vital.enderchest` | Portable ender chest |
| `bdcraft.vital.invsee` | View others' inventories (admin) |

### Tab System

| Permission | Description |
|------------|-------------|
| `bdcraft.tab.admin` | Administer tab list |

## Permission System Notes

### Rank Integration
- Ranks automatically provide economy access, chat formatting, and progression features
- Permission system supplements ranks with utility and administrative commands
- No need to grant market, auction, or villager permissions - ranks handle these

### Donor Benefits
- Donors receive enhanced permissions like `bdcraft.market.donor` and `bdcraft.chat.color`
- Donor status is separate from permission groups but can be combined

### Administrative Access
- `bdcraft.admin` grants full access to all features
- `bdcraft.permissions.admin` grants access to permission management only
- Specific admin permissions like `bdcraft.economy.admin` for granular control

### Default Setup
Most servers only need:
- Basic permissions for all players: `bdcraft.economy.use`, `bdcraft.chat.global`, `bdcraft.spawn.use`
- Admin permissions for staff: `bdcraft.admin` or specific admin permissions
- Donor permissions for supporters: `bdcraft.market.donor`, `bdcraft.chat.color`, etc.