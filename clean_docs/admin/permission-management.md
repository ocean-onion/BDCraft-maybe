# Permission Management System

BDCraft includes a comprehensive permission management system with three methods for managing player permissions: GUI interface, command system, and direct YAML configuration editing.

## Overview

The permission system provides:
- **GUI Management**: Interactive interface for visual permission editing
- **Command System**: In-game commands for quick permission changes
- **YAML Configuration**: Direct file editing for bulk changes and automation
- **Group-Based System**: Organize players into permission groups
- **Individual Overrides**: Set specific permissions for individual players

## GUI Permission Management

### Opening the Permission GUI

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdpermissions` | `/bdperms`, `/perms` | `bdcraft.permissions.admin` | Opens the main permission management GUI |
| `/bdpermissions player <player>` | `/bdperms player <player>` | `bdcraft.permissions.admin` | Opens permission GUI for a specific player |
| `/bdpermissions group <group>` | `/bdperms group <group>` | `bdcraft.permissions.admin` | Opens permission GUI for a specific group |

### GUI Features

#### Main Permission GUI
- **Player List**: Browse all registered players with their current groups
- **Group Management**: Create, edit, and delete permission groups
- **Quick Actions**: Bulk permission changes and group assignments
- **Search Function**: Find players and permissions quickly
- **Permission Tree**: Visual hierarchy of all available permissions

#### Player Permission GUI
- **Current Permissions**: View all permissions assigned to the player
- **Group Membership**: See which groups the player belongs to
- **Permission Toggle**: Click to grant/revoke individual permissions
- **Inheritance Display**: Shows permissions inherited from groups
- **Override Management**: Set player-specific permission overrides

#### Group Permission GUI
- **Group Information**: Name, description, and member count
- **Permission Matrix**: Grid view of all permissions for the group
- **Member Management**: Add/remove players from the group
- **Inheritance Chain**: View parent/child group relationships
- **Template System**: Apply permission templates to groups

### GUI Navigation

#### Permission Categories
The GUI organizes permissions into logical categories:
- **Core**: Basic plugin access and admin functions
- **Economy**: Currency, markets, auctions, villagers
- **Progression**: Ranks, achievements, rebirth system
- **Vital**: Chat, teleportation, homes, utilities
- **Moderation**: Banning, kicking, muting players

#### Permission States
Each permission can have three states:
- **✓ Granted**: Permission is explicitly allowed
- **✗ Denied**: Permission is explicitly denied
- **○ Inherited**: Permission inherited from group or default

## Command-Based Permission Management

### Player Permission Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdperm set <player> <permission> <true/false>` | `/perm set <player> <permission> <true/false>` | `bdcraft.permissions.admin` | Sets a specific permission for a player |
| `/bdperm unset <player> <permission>` | `/perm unset <player> <permission>` | `bdcraft.permissions.admin` | Removes a permission override for a player |
| `/bdperm check <player> <permission>` | `/perm check <player> <permission>` | `bdcraft.permissions.admin` | Checks if a player has a specific permission |
| `/bdperm list <player>` | `/perm list <player>` | `bdcraft.permissions.admin` | Lists all permissions for a player |
| `/bdperm copy <source_player> <target_player>` | `/perm copy <source_player> <target_player>` | `bdcraft.permissions.admin` | Copies permissions from one player to another |
| `/bdperm reset <player>` | `/perm reset <player>` | `bdcraft.permissions.admin` | Resets a player's permissions to group defaults |

### Group Management Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdgroup create <group> [parent]` | `/group create <group> [parent]` | `bdcraft.permissions.admin` | Creates a new permission group |
| `/bdgroup delete <group>` | `/group delete <group>` | `bdcraft.permissions.admin` | Deletes a permission group |
| `/bdgroup list` | `/group list` | `bdcraft.permissions.admin` | Lists all permission groups |
| `/bdgroup info <group>` | `/group info <group>` | `bdcraft.permissions.admin` | Shows detailed group information |
| `/bdgroup set <group> <permission> <true/false>` | `/group set <group> <permission> <true/false>` | `bdcraft.permissions.admin` | Sets a permission for a group |
| `/bdgroup unset <group> <permission>` | `/group unset <group> <permission>` | `bdcraft.permissions.admin` | Removes a permission from a group |
| `/bdgroup add <player> <group>` | `/group add <player> <group>` | `bdcraft.permissions.admin` | Adds a player to a group |
| `/bdgroup remove <player> <group>` | `/group remove <player> <group>` | `bdcraft.permissions.admin` | Removes a player from a group |
| `/bdgroup setdefault <group>` | `/group setdefault <group>` | `bdcraft.permissions.admin` | Sets the default group for new players |

### Advanced Permission Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdperm import <plugin>` | `/perm import <plugin>` | `bdcraft.permissions.admin` | Imports permissions from another plugin |
| `/bdperm export <format>` | `/perm export <format>` | `bdcraft.permissions.admin` | Exports permissions to file (YAML, JSON) |
| `/bdperm reload` | `/perm reload` | `bdcraft.permissions.admin` | Reloads permissions from configuration |
| `/bdperm save` | `/perm save` | `bdcraft.permissions.admin` | Saves current permissions to configuration |
| `/bdperm backup` | `/perm backup` | `bdcraft.permissions.admin` | Creates a backup of current permissions |
| `/bdperm restore <backup_name>` | `/perm restore <backup_name>` | `bdcraft.permissions.admin` | Restores permissions from a backup |

### Bulk Management Commands

| Command | Aliases | Permission | Description |
|---------|---------|------------|-------------|
| `/bdperm bulk set <group/player_list> <permission> <true/false>` | `/perm bulk set <targets> <permission> <value>` | `bdcraft.permissions.admin` | Sets permission for multiple players/groups |
| `/bdperm bulk add <player_list> <group>` | `/perm bulk add <players> <group>` | `bdcraft.permissions.admin` | Adds multiple players to a group |
| `/bdperm bulk remove <player_list> <group>` | `/perm bulk remove <players> <group>` | `bdcraft.permissions.admin` | Removes multiple players from a group |
| `/bdperm template apply <template> <targets>` | `/perm template apply <template> <targets>` | `bdcraft.permissions.admin` | Applies a permission template to targets |

## YAML Configuration Management

### permissions.yml Structure

```yaml
# BDCraft Permission System Configuration
permissions:
  # System Settings
  system:
    default_group: "player"
    save_interval: 300  # Auto-save every 5 minutes
    backup_on_changes: true
    case_sensitive: false
    wildcard_support: true
    
  # Permission Groups
  groups:
    # Default Player Group
    player:
      display_name: "Player"
      weight: 0
      default: true
      permissions:
        - "bdcraft.economy.use"
        - "bdcraft.market.use"
        - "bdcraft.auction.use"
        - "bdcraft.villager.use"
        - "bdcraft.rank.use"
        - "bdcraft.home.use"
        - "bdcraft.teleport.use"
        - "bdcraft.chat.global"
        - "bdcraft.chat.local"
        - "bdcraft.chat.msg"
        - "bdcraft.chat.reply"
        - "bdcraft.spawn.use"
      inheritance: []
      
    # VIP Group
    vip:
      display_name: "VIP"
      weight: 10
      default: false
      permissions:
        - "bdcraft.chat.color"
        - "bdcraft.home.set.multiple"
        - "bdcraft.teleport.bypass-cooldown"
        - "bdcraft.auction.featured"
        - "bdcraft.market.donor"
      inheritance:
        - "player"
        
    # Moderator Group
    moderator:
      display_name: "Moderator"
      weight: 50
      default: false
      permissions:
        - "bdcraft.chat.mute.player"
        - "bdcraft.chat.unmute"
        - "bdcraft.vital.kick"
        - "bdcraft.vital.tempban"
        - "bdcraft.teleport.others"
        - "bdcraft.economy.balance.others"
      inheritance:
        - "vip"
        
    # Administrator Group
    admin:
      display_name: "Administrator"
      weight: 100
      default: false
      permissions:
        - "bdcraft.admin"
        - "bdcraft.*"  # Wildcard for all permissions
      inheritance:
        - "moderator"
        
  # Individual Player Overrides
  players:
    # Example player with custom permissions
    "player_uuid_here":
      name: "ExamplePlayer"
      groups:
        - "vip"
        - "special_role"
      permissions:
        # Explicit grants
        "bdcraft.special.permission": true
        # Explicit denials (overrides group permissions)
        "bdcraft.chat.color": false
      expiry:
        "bdcraft.temp.permission": "2024-12-31T23:59:59Z"
        
  # Permission Templates
  templates:
    new_staff:
      permissions:
        - "bdcraft.chat.admin"
        - "bdcraft.teleport.admin"
        - "bdcraft.economy.admin"
    event_host:
      permissions:
        - "bdcraft.vital.broadcast"
        - "bdcraft.teleport.tpall"
        - "bdcraft.warp.set"
```

### Group Configuration Options

#### Basic Group Properties
- `display_name`: Human-readable group name for administrative purposes
- `weight`: Group priority (higher numbers take precedence)
- `default`: Whether this is the default group for new players

**Note:** Chat prefixes and formatting are handled by the existing rank system. The permission system focuses on access control rather than display formatting.

#### Permission Assignment
- `permissions`: List of permissions granted to the group
- `inheritance`: List of parent groups to inherit permissions from
- `wildcard_support`: Use `*` and `-` for permission patterns

#### Advanced Group Features
- `time_limits`: Set temporary group membership
- `world_specific`: Different permissions per world
- `rank_requirements`: Automatic group assignment based on rank

### Player Configuration Options

#### Basic Player Properties
- `name`: Current player name (for reference)
- `groups`: List of groups the player belongs to
- `permissions`: Individual permission overrides

#### Advanced Player Features
- `expiry`: Temporary permissions with expiration dates
- `world_overrides`: Different permissions in specific worlds
- `schedule`: Time-based permission changes

### Configuration Examples

#### Creating a Custom Group
```yaml
groups:
  builder:
    display_name: "Builder"
    weight: 25
    permissions:
      - "bdcraft.market.create"
      - "bdcraft.warp.set"
      - "worldedit.*"  # External plugin permissions
    inheritance:
      - "vip"
```

#### Temporary Permissions
```yaml
players:
  "uuid-here":
    permissions:
      "bdcraft.event.special": true
    expiry:
      "bdcraft.event.special": "2024-07-01T00:00:00Z"
```

#### World-Specific Permissions
```yaml
players:
  "uuid-here":
    world_overrides:
      "creative_world":
        permissions:
          "bdcraft.vital.fly": true
          "bdcraft.vital.god": true
```

## Permission System Features

### Inheritance System
- **Group Inheritance**: Groups can inherit permissions from parent groups
- **Multiple Groups**: Players can belong to multiple groups simultaneously
- **Weight-Based Priority**: Higher weight groups override lower weight groups
- **Cascading Permissions**: Permissions flow from groups to players

### Wildcard Support
- **Full Wildcards**: `bdcraft.*` grants all BDCraft permissions
- **Module Wildcards**: `bdcraft.economy.*` grants all economy permissions
- **Negative Permissions**: `-bdcraft.admin` explicitly denies admin access
- **Complex Patterns**: `bdcraft.market.*.admin` for advanced matching

### Temporary Permissions
- **Expiring Permissions**: Set permissions with automatic expiration
- **Scheduled Changes**: Permissions that activate/deactivate at specific times
- **Event Permissions**: Temporary permissions for special events
- **Trial Permissions**: Give players temporary access to test features

### World-Specific Permissions
- **Per-World Groups**: Different group membership in different worlds
- **World Overrides**: Specific permissions that only apply in certain worlds
- **Cross-World Inheritance**: Share permissions across multiple worlds
- **World-Specific Defaults**: Different default groups per world

## Integration with BDCraft Systems

### Rank Integration
**Important:** BDCraft ranks automatically provide their own benefits and permissions. The permission system handles additional access beyond rank benefits.

**Ranks automatically provide:**
- Chat prefixes: `[Newcomer]`, `[Farmer]`, `[Expert Farmer]`, `[Master Farmer]`, `[Agricultural Expert]`
- Economy benefits: Yield bonuses (5%-30%), reduced auction fees (4.5%-3%), extended listing durations
- Growth bonuses: Faster seed growth (5%-25%) and rare crop chances (15%-25%)
- Tool access: Automatic BD tool purchasing rights based on rank requirements
- Market features: Market creation and upgrade capabilities

**Permission system handles:**
- Administrative commands: `/bdadmin`, `/bdeco give`, `/bdrank set`
- Moderation tools: `/kick`, `/ban`, `/mute`, `/heal`, `/fly`
- Donor features: Remote market access (`/bdmarket`), enhanced limits
- Special access: Permission management, configuration editing

```yaml
# Permission groups supplement rank benefits, not replace them
groups:
  player:
    # Basic permissions that all players need regardless of rank
    permissions:
      - "bdcraft.economy.use"      # Required for BD system access
      - "bdcraft.chat.global"      # Required for chat participation
      - "bdcraft.spawn.use"        # Required for spawn teleportation
```

### Economy Integration
```yaml
# Permissions that affect economic features
economy_permissions:
  market_creation: "bdcraft.market.create"
  auction_featured: "bdcraft.auction.featured"
  villager_admin: "bdcraft.villager.admin"
```

### Rebirth Integration
```yaml
# Special permissions for rebirth players
rebirth_permissions:
  rebirth_1:
    - "bdcraft.rebirth.benefits.1"
    - "bdcraft.cosmetics.halo"
  rebirth_5:
    - "bdcraft.rebirth.mastery"
    - "bdcraft.cosmetics.particles"
```

## Administration Commands

### System Management

| Command | Permission | Description |
|---------|------------|-------------|
| `/bdperm debug <player>` | `bdcraft.permissions.admin` | Shows detailed permission calculation for debugging |
| `/bdperm validate` | `bdcraft.permissions.admin` | Validates permission configuration for errors |
| `/bdperm migrate <from_plugin>` | `bdcraft.permissions.admin` | Migrates permissions from another plugin |
| `/bdperm optimize` | `bdcraft.permissions.admin` | Optimizes permission storage and inheritance |

### Monitoring Commands

| Command | Permission | Description |
|---------|------------|-------------|
| `/bdperm stats` | `bdcraft.permissions.admin` | Shows permission system statistics |
| `/bdperm audit <player>` | `bdcraft.permissions.admin` | Shows permission change history for a player |
| `/bdperm usage <permission>` | `bdcraft.permissions.admin` | Shows which players/groups have a permission |

## Best Practices

### Group Design
1. **Hierarchical Structure**: Create logical inheritance chains
2. **Minimal Permissions**: Only grant necessary permissions
3. **Clear Naming**: Use descriptive group names and display names
4. **Weight Organization**: Leave gaps between weights for future groups

### Security Considerations
1. **Admin Protection**: Carefully control who has permission admin access
2. **Wildcard Caution**: Use wildcards sparingly and understand their scope
3. **Regular Audits**: Periodically review permission assignments
4. **Backup Strategy**: Maintain regular backups of permission data

### Performance Optimization
1. **Group-Based Assignment**: Use groups instead of individual permissions
2. **Inheritance Depth**: Limit inheritance chain depth for performance
3. **Regular Cleanup**: Remove unused groups and expired permissions
4. **Batch Operations**: Use bulk commands for multiple changes

---

This permission management system provides complete control over player access to BDCraft features while maintaining simplicity for administrators and flexibility for complex server configurations.