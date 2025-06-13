# Permissions Administration Guide

This guide provides practical instructions for server administrators on managing BDCraft's built-in permission system.

## Quick Setup Guide

### Initial Setup
1. **Start with default configuration** - BDCraft creates basic permission groups automatically
2. **Identify your admin UUID** - Use `/bdadmin player info <your_username>` to get your UUID
3. **Grant yourself admin permissions** - Add yourself to the admin group
4. **Configure donor group** - Set up donor permissions for server supporters

### Basic Permission Groups

#### Default Player Setup
```yaml
# Add to permissions.yml
players:
  <your-uuid>:
    group: admin
    
groups:
  default:
    permissions:
      - bdcraft.economy.use
      - bdcraft.market.use
      - bdcraft.auction.use
      - bdcraft.villager.use
      - bdcraft.rank.use
      - bdcraft.home.use
      - bdcraft.home.set
      - bdcraft.home.multiple.1
      - bdcraft.teleport.use
      - bdcraft.chat.global
      - bdcraft.chat.local
```

#### Donor Group Configuration
```yaml
donor:
  inherit: default
  permissions:
    - bdcraft.home.multiple.3
    - bdcraft.market.donor
    - bdcraft.teleport.back
    - bdcraft.chat.color
    - bdcraft.villager.seasonal
```

#### Admin Group Configuration
```yaml
admin:
  inherit: donor
  permissions:
    - bdcraft.admin
```

## Common Permission Tasks

### Adding a New Donor
1. **Get player UUID**: `/bdadmin player info <player>`
2. **Verify donation**: `/donorverified <player>` (sets donor flag)
3. **Add to donor group**:
```yaml
players:
  <player-uuid>:
    group: donor
```
4. **Reload permissions**: `/bdadmin perms reload`

### Creating Custom Staff Ranks

#### Moderator Rank
```yaml
moderator:
  inherit: donor
  permissions:
    - bdcraft.chat.mute
    - bdcraft.chat.mute.player
    - bdcraft.chat.unmute
    - bdcraft.vital.kick
    - bdcraft.vital.tempban
    - bdcraft.teleport.others
```

#### Helper Rank
```yaml
helper:
  inherit: default
  permissions:
    - bdcraft.chat.color
    - bdcraft.teleport.others
    - bdcraft.home.multiple.2
    - bdcraft.vital.feed.others
    - bdcraft.vital.heal.others
```

### Troubleshooting Permissions

#### Player Can't Use Commands
1. **Check player's group**: `/bdadmin perms player info <player>`
2. **Verify group permissions**: `/bdadmin perms group info <group>`
3. **Test specific permission**: `/bdadmin checkperm <player> <permission>`
4. **Check inheritance**: Ensure groups inherit properly

#### Permission Not Working
1. **Verify permission exists**: Check [permissions.md](../configuration/permissions.md)
2. **Check for typos**: Permission nodes are case-sensitive
3. **Reload permissions**: `/bdadmin perms reload`
4. **Test with admin**: Verify command works with admin permissions

## Advanced Permission Management

### Negative Permissions
Remove specific permissions from inherited groups:
```yaml
limited_donor:
  inherit: donor
  permissions:
    - -bdcraft.market.donor  # Remove remote market access
    - -bdcraft.teleport.back # Remove back command
```

### Permission Inheritance Chain
Design logical inheritance chains:
```
default → member → vip → donor → helper → moderator → admin
```

### Time-Based Permissions
For temporary permissions, use external tools or manual management:
1. Grant temporary permissions manually
2. Set calendar reminders to remove them
3. Use external permission plugins for advanced time-based permissions

## Permission Categories

### Essential Player Permissions
Minimum permissions every player needs:
- `bdcraft.economy.use` - Basic economy access
- `bdcraft.market.use` - Market browsing and information
- `bdcraft.auction.use` - Auction house access
- `bdcraft.villager.use` - Trade with BD villagers
- `bdcraft.home.use` - Home teleportation
- `bdcraft.chat.global` - Chat participation

### Economy Permissions
For players participating in the economy:
- `bdcraft.economy.pay` - Send money to other players
- `bdcraft.auction.sell` - List items for sale
- `bdcraft.market.use` - Market creation and management
- `bdcraft.villager.dealer` - Trade with dealers
- `bdcraft.villager.collector` - Sell crops to collectors

### Admin Permissions
For server management:
- `bdcraft.admin` - Full admin access (includes everything)
- `bdcraft.economy.admin` - Economy management only
- `bdcraft.market.admin` - Market administration only
- `bdcraft.villager.admin` - Villager management only

## Security Best Practices

### Permission Security
1. **Principle of Least Privilege** - Grant only necessary permissions
2. **Regular Audits** - Review permissions periodically
3. **Group-Based Management** - Avoid individual player permissions when possible
4. **Test Changes** - Always test permission changes with test accounts

### Admin Account Security
1. **Limit Admin Accounts** - Only trusted staff should have full admin
2. **Use Specific Permissions** - Grant specific admin permissions rather than full admin when possible
3. **Monitor Admin Activity** - Keep logs of admin actions
4. **Regular Review** - Audit admin permissions regularly

## Integration with Other Systems

### Rank Integration
BDCraft ranks can integrate with permissions:
1. **Automatic Permissions** - Higher ranks can grant additional permissions
2. **Rank-Based Groups** - Create permission groups that mirror ranks
3. **Progression Rewards** - Grant permissions as rank advancement rewards

### Donor Integration
Link donor status with permissions:
1. **Automatic Assignment** - `/donorverified` can auto-assign donor group
2. **Donor Benefits** - All donor perks controlled through permissions
3. **Multiple Tiers** - Create different donor levels with varying permissions

## Command Reference

### Permission Management Commands
- `/bdadmin perms group list` - List all permission groups
- `/bdadmin perms group info <group>` - View group permissions
- `/bdadmin perms player info <player>` - View player permissions
- `/bdadmin perms player setgroup <player> <group>` - Assign player to group
- `/bdadmin perms reload` - Reload permission configuration

### Testing Commands
- `/bdadmin checkperm <player> <permission>` - Test if player has permission
- `/bdadmin player info <player>` - Get player information including UUID
- `/donorverified <player>` - Verify player as donor (admin only)

## Common Issues and Solutions

### "Permission Denied" Errors
1. Check if player has required permission
2. Verify permission inheritance is working
3. Ensure permission spelling is correct
4. Check if permission was negated somewhere in inheritance chain

### Players Not Getting Donor Benefits
1. Verify player is in donor group or has donor flag
2. Check donor-specific permissions are granted
3. Ensure `/donorverified` was used for donor flag
4. Reload permissions after changes

### Permission Changes Not Taking Effect
1. Use `/bdadmin perms reload` to reload permissions
2. Player may need to reconnect for some changes
3. Check for typos in permission nodes
4. Verify YAML syntax is correct in permissions.yml

## Best Practices for Large Servers

### Scalable Permission Structure
1. **Standardized Groups** - Create consistent group names and structures
2. **Documentation** - Document all custom permissions and groups
3. **Change Management** - Have procedures for permission changes
4. **Regular Cleanup** - Remove unused permissions and groups

### Performance Considerations
1. **Minimize Individual Permissions** - Use groups instead of individual permissions
2. **Efficient Inheritance** - Design inheritance chains to minimize duplication
3. **Regular Maintenance** - Clean up unused permissions regularly

---

This guide provides the foundation for managing BDCraft permissions effectively. For detailed permission lists, see [Permissions Reference](../configuration/permissions.md).