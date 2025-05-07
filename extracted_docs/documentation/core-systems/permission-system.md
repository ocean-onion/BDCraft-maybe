# BDPerms System

The BDCraft plugin includes a fully self-contained permission system called BDPerms. This integrated system manages all permissions without requiring any external plugins. This guide explains how BDPerms structures and manages permissions across the plugin.

## Permission Structure

BDPerms uses a hierarchical permission structure with the following format:

`bdcraft.<category>.<feature>.<action>`

For example:
- `bdcraft.admin.market.delete`
- `bdcraft.rank.up`
- `bdcraft.auction.sell`

## Base Permission Categories

### Player Permissions

| Permission | Description | Default |
|------------|-------------|--------|
| `bdcraft.use` | Basic plugin usage | Everyone |
| `bdcraft.market.locate` | Use market locator commands | Everyone |
| `bdcraft.rank.view` | View ranks and rank progress | Everyone |
| `bdcraft.rank.up` | Purchase rank upgrades | Everyone |
| `bdcraft.balance` | Check currency balance | Everyone |
| `bdcraft.pay` | Send currency to other players | Everyone |
| `bdcraft.top` | View leaderboards | Everyone |
| `bdcraft.auction.use` | Access the auction house | Everyone |
| `bdcraft.auction.sell` | Sell items on the auction house | Everyone |
| `bdcraft.auction.cancel` | Cancel auction listings | Everyone |
| `bdcraft.reputation` | Check trader reputation | Everyone |
| `bdcraft.rebirth.view` | View rebirth information | Everyone |
| `bdcraft.rebirth.check` | Check rebirth eligibility | Everyone |
| `bdcraft.rebirth.use` | Perform rebirth | Everyone |

### Advanced Player Permissions

| Permission | Description | Default |
|------------|-------------|--------|
| `bdcraft.sponsor` | Sponsor other players (Rank 5 only) | Everyone |
| `bdcraft.deity.bless` | Bless other players (Rebirth 10 only) | Everyone |
| `bdcraft.deity.predict` | Preview seasonal traders (Rebirth 10 only) | Everyone |
| `bdcraft.deity.aura` | Toggle abundance aura (Rebirth 10 only) | Everyone |

### Admin Permissions

| Permission | Description | Default |
|------------|-------------|--------|
| `bdcraft.admin` | Base admin permission | Operators |
| `bdcraft.admin.village` | Manage villages | Operators |
| `bdcraft.admin.villager` | Manage villagers | Operators |
| `bdcraft.admin.economy` | Manage economy | Operators |
| `bdcraft.admin.items` | Manage BD items | Operators |
| `bdcraft.admin.rank` | Manage player ranks | Operators |
| `bdcraft.admin.rebirth` | Manage rebirth levels | Operators |
| `bdcraft.admin.reload` | Reload plugin configuration | Operators |
| `bdcraft.admin.debug` | Toggle debug mode | Operators |
| `bdcraft.admin.market` | Market system management | Operators |

## BDPerms Configuration

The BDPerms system is configured through the `bdperms.yml` file:

```yaml
# In bdperms.yml
groups:
  player:
    default: true
    permissions:
      - bdcraft.use
      - bdcraft.market.locate
      - bdcraft.rank.view
      - bdcraft.rank.up
      - bdcraft.balance
      - bdcraft.auction.use
  admin:
    permissions:
      - bdcraft.admin
      - bdcraft.admin.market
      - bdcraft.admin.villager
      - bdcraft.admin.economy
      - bdcraft.admin.items
      - bdcraft.admin.rank
      - bdcraft.admin.reload
      - bdcraft.admin.debug
```

## Permission Groups

BDCraft defines several default permission groups:

### Player Group

Base permissions for all players:

```yaml
player:
  default: true
  permissions:
    - bdcraft.use
    - bdcraft.market.locate
    - bdcraft.rank.view
    - bdcraft.rank.up
    - bdcraft.balance
    - bdcraft.pay
    - bdcraft.top
    - bdcraft.auction.use
    - bdcraft.auction.sell
    - bdcraft.auction.cancel
    - bdcraft.reputation
    - bdcraft.rebirth.view
    - bdcraft.rebirth.check
    - bdcraft.rebirth.use
    - bdcraft.sponsor
    - bdcraft.deity.bless
    - bdcraft.deity.predict
    - bdcraft.deity.aura
```

### VIP Group

Advanced permissions for donors or special players:

```yaml
vip:
  permissions:
    - bdcraft.use
    - bdcraft.market.locate
    - bdcraft.rank.view
    - bdcraft.rank.up
    - bdcraft.balance
    - bdcraft.pay
    - bdcraft.top
    - bdcraft.auction.use
    - bdcraft.auction.sell
    - bdcraft.auction.cancel
    - bdcraft.reputation
    - bdcraft.rebirth.view
    - bdcraft.rebirth.check
    - bdcraft.rebirth.use
    - bdcraft.sponsor
    - bdcraft.deity.bless
    - bdcraft.deity.predict
    - bdcraft.deity.aura
    - bdcraft.vip.discounts
    - bdcraft.vip.extra.harvester
```

### Moderator Group

Limited admin permissions for staff members:

```yaml
moderator:
  permissions:
    - bdcraft.use
    - bdcraft.market.locate
    - bdcraft.rank.view
    - bdcraft.rank.up
    - bdcraft.balance
    - bdcraft.pay
    - bdcraft.top
    - bdcraft.auction.use
    - bdcraft.auction.sell
    - bdcraft.auction.cancel
    - bdcraft.reputation
    - bdcraft.rebirth.view
    - bdcraft.rebirth.check
    - bdcraft.rebirth.use
    - bdcraft.sponsor
    - bdcraft.deity.bless
    - bdcraft.deity.predict
    - bdcraft.deity.aura
    - bdcraft.admin.trader
    - bdcraft.admin.items
    - bdcraft.admin.reload
```

### Admin Group

Full admin permissions:

```yaml
admin:
  permissions:
    - bdcraft.admin
    - bdcraft.admin.market
    - bdcraft.admin.trader
    - bdcraft.admin.economy
    - bdcraft.admin.items
    - bdcraft.admin.rank
    - bdcraft.admin.rebirth
    - bdcraft.admin.reload
    - bdcraft.admin.debug
    - bdcraft.admin.perms
```

## Market Permissions

The player market system has its own permission structure:

```yaml
market-permissions:
  base:                          # Base market permissions
    - "bdcraft.market.create"    # Ability to create markets
    - "bdcraft.market.check"     # Check market boundaries
    - "bdcraft.market.info"      # View market info
  founder:                       # Market founder permissions
    - "bdcraft.market.manage"    # Manage own market
    - "bdcraft.market.associate" # Add/remove associates
    - "bdcraft.market.upgrade"   # Upgrade market
  associate:                     # Market associate permissions
    - "bdcraft.market.build"     # Build in market area
    - "bdcraft.market.harvest"   # Harvest BD crops in market
    - "bdcraft.market.collector" # Add collector houses
  admin:                         # Admin market permissions
    - "bdcraft.admin.market"     # Full control over all markets
    - "bdcraft.admin.override"   # Override market restrictions
```

## Rank-Permission Integration

BDPerms automatically manages permissions based on player ranks and progression:

```yaml
rank-permissions:
  enabled: true
  unlock-permissions:
    1: # Newcomer
      - bdcraft.auction.use
    2: # Farmer
      - bdcraft.auction.sell
      - bdcraft.pay
    3: # Expert Farmer
      - bdcraft.market.create
    4: # Master Farmer
      - bdcraft.market.upgrade
    5: # Agricultural Expert
      - bdcraft.sponsor
      - bdcraft.market.multiple
```

With this configuration, as players progress through ranks, they automatically gain new permissions that unlock additional features.

## Permission Commands

### Player Permission Commands

In simple mode, players can check their permissions:

```
/bdperms check <permission>
```

**Example**:
```
/bdperms check bdcraft.auction.sell
```

### Admin Permission Commands

Admins can manage player permissions in simple mode:

```
/bdadmin perms add <player> <permission>
/bdadmin perms remove <player> <permission>
/bdadmin perms group add <player> <group>
/bdadmin perms group remove <player> <group>
/bdadmin perms reload
```

**Examples**:
```
/bdadmin perms add Steve bdcraft.vip.discounts
/bdadmin perms group add Steve vip
```

## Context-Based Permissions

BDCraft supports context-based permissions for certain actions:

### World Contexts

Permissions can be limited to specific worlds:

```yaml
context-permissions:
  worlds:
    survival:
      - bdcraft.auction.use
      - bdcraft.auction.sell
    creative:
      - bdcraft.admin.items
```

### Rank Contexts

Permissions can require specific BD ranks:

```yaml
context-permissions:
  ranks:
    farmer:  # Requires Farmer rank (2) or higher
      - bdcraft.auction.sell
    agricultural_expert:  # Requires Agricultural Expert rank (5)
      - bdcraft.sponsor
```

## Command Cooldowns

Commands can have permission-based cooldowns:

```yaml
command-cooldowns:
  bdrank:
    cooldown: 60  # Seconds
    bypass-permission: "bdcraft.cooldown.bypass.rank"
  bdrebirth:
    cooldown: 300  # Seconds
    bypass-permission: "bdcraft.cooldown.bypass.rebirth"
```

## Market Context Permissions

The market system adds several context-based permissions:

1. **Market Owner Context**: Permissions that only apply to the market owner
2. **Associate Context**: Permissions that apply to players associated with a market
3. **Location Context**: Permissions that only apply within market boundaries

```yaml
market-contexts:
  owner:
    - bdcraft.market.upgrade
    - bdcraft.market.associate.add
    - bdcraft.market.associate.remove
  associate:
    - bdcraft.market.build
    - bdcraft.market.collector.add
  within_market:
    - bdcraft.market.harvest
    - bdcraft.market.interact
```

These contexts are automatically checked and applied when players are in or near markets.

## Technical Implementation

### Permission Checking

BDPerms checks permissions using this priority:

1. Check if player is server operator (automatic access)
2. Check the player's assigned groups and individual permissions
3. Check context-based permissions (world, rank, market)

### Permission Caching

Permissions are cached for performance:

```yaml
permission-caching:
  enabled: true
  cache-duration: 60  # Seconds
  clear-on-join: true
```

### Permission Data Storage

Permission data is stored in two primary files:

```
plugins/BDCraft/data/bdperms/groups.json   # Group definitions
plugins/BDCraft/data/bdperms/players.json  # Player assignments
```

These files contain all permission groups and player assignments.

## GUI Permission Management

BDPerms includes a built-in GUI for managing permissions:

```
/bdperms gui
```

This command opens an inventory-based GUI that allows administrators to:
1. View and edit permission groups
2. Manage player group assignments
3. Add or remove individual permissions
4. Configure rank-based permission unlocks

## Best Practices

1. **Use Group-Based Permissions**: Assign permissions to groups rather than individual players
2. **Leverage Rank Progression**: Configure rank-based permission unlocks to reward player progression
3. **Minimum Permissions**: Grant only the permissions players need
4. **Regular Audits**: Periodically review permission assignments using the GUI
5. **Use Context Permissions**: Utilize market and world contexts for granular control

The BDPerms system provides comprehensive permission management without requiring any external plugins. This self-contained approach ensures maximum compatibility and seamless operation.