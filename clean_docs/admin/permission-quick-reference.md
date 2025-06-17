# Permission Management Quick Reference

This quick reference guide provides essential commands and configurations for managing permissions in BDCraft.

## Essential Commands

### Most Used Permission Commands

| Command | Description | Example |
|---------|-------------|---------|
| `/bdperms` | Open permission GUI | `/bdperms` |
| `/bdperm set <player> <permission> <true/false>` | Set player permission | `/bdperm set Steve bdcraft.admin true` |
| `/bdgroup add <player> <group>` | Add player to group | `/bdgroup add Steve vip` |
| `/bdperm check <player> <permission>` | Check if player has permission | `/bdperm check Steve bdcraft.market.use` |
| `/bdperm list <player>` | List all player permissions | `/bdperm list Steve` |

### Group Management Essentials

| Command | Description | Example |
|---------|-------------|---------|
| `/bdgroup create <group>` | Create new group | `/bdgroup create builder` |
| `/bdgroup set <group> <permission> <true/false>` | Set group permission | `/bdgroup set vip bdcraft.chat.color true` |
| `/bdgroup list` | List all groups | `/bdgroup list` |
| `/bdgroup info <group>` | Show group details | `/bdgroup info admin` |

## Common Permission Patterns

### Essential Player Permissions
```yaml
# Minimum permissions for basic gameplay
basic_permissions:
  - "bdcraft.economy.use"
  - "bdcraft.market.use"
  - "bdcraft.auction.use"
  - "bdcraft.villager.use"
  - "bdcraft.rank.use"
  - "bdcraft.home.use"
  - "bdcraft.chat.global"
  - "bdcraft.spawn.use"
```

### VIP/Donor Permissions
```yaml
# Enhanced permissions for donors
vip_permissions:
  - "bdcraft.chat.color"
  - "bdcraft.home.set.multiple"
  - "bdcraft.teleport.bypass-cooldown"
  - "bdcraft.auction.featured"
  - "bdcraft.market.donor"
  - "bdcraft.vital.fly"
```

### Staff Permissions
```yaml
# Moderator permissions
moderator_permissions:
  - "bdcraft.chat.mute.player"
  - "bdcraft.chat.unmute"
  - "bdcraft.vital.kick"
  - "bdcraft.vital.tempban"
  - "bdcraft.teleport.others"
  - "bdcraft.economy.balance.others"

# Administrator permissions
admin_permissions:
  - "bdcraft.admin"  # Grants access to all features
  - "bdcraft.*"      # Wildcard for all BDCraft permissions
```

## Quick Group Setup

### Creating Standard Groups

#### Player Group (Default)
```bash
/bdgroup create player
/bdgroup set player bdcraft.economy.use true
/bdgroup set player bdcraft.market.use true
/bdgroup set player bdcraft.auction.use true
/bdgroup set player bdcraft.villager.use true
/bdgroup set player bdcraft.rank.use true
/bdgroup set player bdcraft.home.use true
/bdgroup set player bdcraft.chat.global true
/bdgroup set player bdcraft.spawn.use true
/bdgroup setdefault player
```

#### VIP Group
```bash
/bdgroup create vip player
/bdgroup set vip bdcraft.chat.color true
/bdgroup set vip bdcraft.home.set.multiple true
/bdgroup set vip bdcraft.teleport.bypass-cooldown true
/bdgroup set vip bdcraft.auction.featured true
/bdgroup set vip bdcraft.market.donor true
```

#### Moderator Group
```bash
/bdgroup create moderator vip
/bdgroup set moderator bdcraft.chat.mute.player true
/bdgroup set moderator bdcraft.chat.unmute true
/bdgroup set moderator bdcraft.vital.kick true
/bdgroup set moderator bdcraft.vital.tempban true
/bdgroup set moderator bdcraft.teleport.others true
```

#### Admin Group
```bash
/bdgroup create admin moderator
/bdgroup set admin bdcraft.admin true
/bdgroup set admin bdcraft.* true
```

## Permission Categories

### Core System
- `bdcraft.admin` - Full admin access
- `bdcraft.permissions.admin` - Permission management access
- `bdcraft.economy.admin` - Economy administration
- `bdcraft.market.admin` - Market administration

### Economy Features
- `bdcraft.economy.use` - Basic economy access
- `bdcraft.economy.pay` - Pay other players
- `bdcraft.market.use` - Market browsing
- `bdcraft.market.create` - Market creation
- `bdcraft.auction.use` - Auction house access
- `bdcraft.auction.sell` - Sell on auction house

### Social Features
- `bdcraft.chat.global` - Global chat
- `bdcraft.chat.local` - Local chat
- `bdcraft.chat.color` - Colored chat
- `bdcraft.chat.msg` - Private messaging
- `bdcraft.home.use` - Home teleportation
- `bdcraft.teleport.request` - Teleport requests

### Utilities
- `bdcraft.vital.fly` - Flight mode
- `bdcraft.vital.heal` - Self healing
- `bdcraft.vital.god` - God mode
- `bdcraft.workbench` - Portable crafting
- `bdcraft.enderchest` - Portable ender chest

## Troubleshooting

### Common Issues

#### Player Can't Use Basic Features
```bash
# Check if player has essential permissions
/bdperm check PlayerName bdcraft.economy.use
/bdperm check PlayerName bdcraft.market.use

# If missing, add to player group
/bdgroup add PlayerName player
```

#### Permission Not Working
```bash
# Debug permission calculation
/bdperm debug PlayerName
/bdperm check PlayerName specific.permission

# Check group membership
/bdperm list PlayerName
```

#### Group Inheritance Issues
```bash
# Check group information
/bdgroup info groupname

# Verify inheritance chain
/bdperm validate
```

### Permission Conflicts

#### Resolving Conflicts
1. **Individual vs Group**: Individual permissions override group permissions
2. **Multiple Groups**: Higher weight groups take precedence
3. **Negative Permissions**: Use `-permission` to explicitly deny access
4. **Inheritance**: Child groups inherit from parent groups

#### Example Conflict Resolution
```bash
# Player in both 'vip' and 'restricted' groups
# Give VIP higher weight to take precedence
/bdgroup info vip
/bdgroup info restricted

# Or remove from conflicting group
/bdgroup remove PlayerName restricted
```

## YAML Configuration Examples

### Simple Group Configuration
```yaml
groups:
  player:
    display_name: "Player"
    prefix: "&7[Player]"
    weight: 0
    default: true
    permissions:
      - "bdcraft.economy.use"
      - "bdcraft.market.use"
      - "bdcraft.chat.global"
    inheritance: []
```

### Player Override Configuration
```yaml
players:
  "player-uuid-here":
    name: "PlayerName"
    groups:
      - "vip"
    permissions:
      "bdcraft.special.feature": true
      "bdcraft.chat.color": false  # Override group permission
```

## Best Practices Checklist

### Security
- [ ] Limit who has `bdcraft.admin` permission
- [ ] Use groups instead of individual permissions when possible
- [ ] Regularly audit permission assignments
- [ ] Keep backups of permission configuration

### Organization
- [ ] Use logical group hierarchy (player → vip → mod → admin)
- [ ] Set appropriate group weights
- [ ] Use descriptive group names
- [ ] Document custom permission assignments

### Performance
- [ ] Avoid deep inheritance chains
- [ ] Use wildcards sparingly
- [ ] Clean up unused permissions
- [ ] Monitor permission cache performance

## Emergency Commands

### Restore Admin Access
```bash
# If accidentally removed admin permissions
/bdperm set YourUsername bdcraft.admin true
/bdgroup add YourUsername admin
```

### Reset Player Permissions
```bash
# Reset player to default state
/bdperm reset PlayerName
/bdgroup add PlayerName player
```

### Backup Permissions
```bash
# Create backup before making changes
/bdperm backup
/bdperm save
```

## Integration Notes

### Rank System Integration
- Player groups automatically update with rank changes
- Rank-specific permissions granted automatically
- Use `rank_` prefix for rank-based groups

### Economy Integration
- Market creation requires `bdcraft.market.create`
- Featured auctions require `bdcraft.auction.featured`
- Admin economy commands require `bdcraft.economy.admin`

### Rebirth Integration
- Rebirth players get special permission groups
- Rebirth bonuses may include permission upgrades
- Use `rebirth_` prefix for rebirth-specific groups

This quick reference provides the essential information needed for day-to-day permission management in BDCraft.