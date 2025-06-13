# Navigation System

BDCraft's navigation system provides comprehensive teleportation and location management tools that support economic activities and general server navigation.

## Home System

### Home Management
Personal teleportation points for quick access to important locations:

#### Setting Homes
- `/home set` - Set your default home at current location
- `/sethome` - Alternative command for setting default home
- `/home set <name>` - Set a named home at current location
- `/sethome <name>` - Alternative command for named homes

#### Using Homes
- `/home` - Teleport to your default home
- `/home <name>` - Teleport to a specific named home
- `/h` - Short alias for default home teleport

#### Managing Homes
- `/homes` - List all your set homes
- `/home list` - Alternative listing command
- `/home delete <name>` - Delete a specific home
- `/delhome <name>` - Alternative deletion command
- `/home limit` - Check your current home limit

### Home Limitations
- **Default Limit**: 1 home for regular players
- **Donor Limit**: 3 homes for donors
- **Permission-Based**: Additional homes via `bdcraft.home.multiple.X` permissions
- **Distance Requirements**: 100-block minimum spacing between homes
- **World Restrictions**: Some worlds may prohibit home setting

### Home Features
- **Cross-World**: Homes work across different worlds
- **Persistence**: Homes maintained across server restarts
- **Safety Checks**: Automatic validation of home location safety
- **Cooldown Protection**: 30-second cooldown between home teleports

## Teleportation System

### Player Teleportation
Direct teleportation between players:

#### Basic Teleportation
- `/tp <player>` - Teleport directly to another player (admin)
- `/teleport <player>` - Alternative teleport command
- `/tp <x> <y> <z>` - Teleport to specific coordinates (admin)

#### Request-Based Teleportation
- `/tpa <player>` - Request to teleport to a player
- `/tpahere <player>` - Request a player to teleport to you
- `/tpaccept` - Accept an incoming teleport request
- `/tpdeny` - Deny an incoming teleport request
- `/tpcancel` - Cancel a pending teleport request
- `/tptoggle` - Toggle acceptance of teleport requests

### Teleportation Features
- **Request Timeout**: Requests expire after 60 seconds
- **Safety Validation**: Automatic safety checks for teleport destinations
- **Warmup Delay**: 3-second preparation time before teleportation
- **Movement Cancellation**: Moving during warmup cancels teleportation
- **Cross-World Support**: Teleportation works between different worlds

### Advanced Teleportation
- `/back` - Return to your previous location
- `/tpr` - Random teleport to unexplored areas (5-minute cooldown)
- `/tpall` - Teleport all players to your location (admin)

## Warp System

### Public Warps
Server-defined teleportation points for common destinations:

#### Using Warps
- `/warp <name>` - Teleport to a named warp point
- `/warps` - List all available warps
- `/warp list` - Alternative warp listing
- `/warpinfo <name>` - View details about a specific warp

#### Managing Warps (Admin)
- `/setwarp <name>` - Create a new warp at your location
- `/delwarp <name>` - Delete an existing warp
- `/movewarp <name>` - Update an existing warp to current location
- `/warp <name> <player>` - Send a specific player to a warp

### Warp Features
- **Categorization**: Warps can be organized into categories
- **Descriptions**: Each warp can have a description and usage notes
- **Permission Gates**: Specific warps can require permissions
- **Usage Tracking**: Monitor warp usage statistics

## Spawn System

### Server Spawn
Central starting point for all players:

#### Spawn Commands
- `/spawn` - Teleport to the server spawn point
- `/setspawn` - Set the server spawn location (admin)

#### Spawn Features
- **New Player Spawn**: All new players start at spawn
- **Death Respawn**: Players respawn at spawn (configurable)
- **Safety Zone**: Spawn areas typically have protection
- **Information Hub**: Spawn often contains server information

## Location Recall

### Back System
Return to previous locations:
- **Death Locations**: `/back` returns to last death location
- **Teleport History**: Return to location before last teleport
- **Cooldown**: 30-second cooldown between uses
- **Limitations**: Limited history depth for performance

### Location Tracking
- **Automatic Tracking**: System tracks significant location changes
- **Death Points**: Death locations automatically recorded
- **Teleport Points**: Pre-teleportation locations saved
- **World Changes**: Cross-world movement tracked

## Safety and Security

### Teleportation Safety
- **Location Validation**: All destinations checked for safety
- **Block Checking**: Ensure solid ground and breathable space
- **Void Protection**: Prevent teleportation into void
- **Lava/Water Checks**: Avoid dangerous liquid teleportation

### Anti-Abuse Measures
- **Cooldown Systems**: Prevent rapid teleportation abuse
- **Request Limits**: Limit concurrent teleport requests
- **Spam Protection**: Rate limiting for teleport commands
- **Permission Requirements**: Appropriate permissions for all features

### Combat Integration
- **Combat Tagging**: Prevent teleportation during combat
- **Safe Zones**: Some areas may restrict certain teleportation
- **PvP Considerations**: Special rules in PvP-enabled areas

## Integration with Economy

### Market Navigation
- **Market Warps**: Quick access to major markets
- **Home Placement**: Strategic home placement near markets
- **Trading Convenience**: Rapid movement between trading locations

### Auction House Access
- **Quick Access**: Teleportation to auction house locations
- **Item Retrieval**: Navigation to storage pickup points
- **Trading Meetups**: Coordinate player meetings for trades

## Advanced Features

### Teleportation Delays
Configurable delays for different teleportation types:
- **Home Teleports**: 3-second warmup delay
- **Warp Teleports**: Configurable per-warp delays
- **Player Teleports**: Variable delays based on permissions
- **Emergency Teleports**: Instant teleportation for emergencies

### Cost Integration
Optional economic costs for teleportation:
- **Home Costs**: Configurable costs for home teleportation
- **Warp Fees**: Per-warp usage fees
- **Distance-Based**: Costs based on teleportation distance
- **Donor Benefits**: Reduced or eliminated costs for donors

### Permission Integration
Comprehensive permission system:
- **Feature Gates**: Individual permissions for each teleportation type
- **Bypass Permissions**: Admin overrides for restrictions
- **Limit Permissions**: Specific permissions for enhanced limits
- **Safety Bypasses**: Override safety checks when necessary

## Configuration Options

### Teleportation Settings
```yaml
teleportation:
  delays:
    home: 3          # seconds before home teleport
    warp: 5          # seconds before warp teleport
    player: 3        # seconds before player teleport
    back: 2          # seconds before back teleport
  
  cooldowns:
    home: 30         # seconds between home uses
    back: 30         # seconds between back uses
    random: 300      # seconds between random teleports
    request: 5       # seconds between teleport requests
  
  safety:
    check_destination: true
    check_suffocation: true
    check_void: true
    check_lava: true
```

### Home System Settings
```yaml
homes:
  limits:
    default: 1       # default homes for regular players
    donor: 3         # homes for donors
    admin: 10        # homes for administrators
  
  restrictions:
    min_distance: 100      # minimum blocks between homes
    disabled_worlds: []    # worlds where homes can't be set
    safe_teleport: true    # enable safety checks
  
  features:
    cross_world: true      # allow cross-world homes
    bed_homes: false       # automatically set home at bed
    death_home: false      # set home at death location
```

### Warp System Settings
```yaml
warps:
  features:
    categories: true       # enable warp categories
    descriptions: true     # allow warp descriptions
    usage_stats: true      # track warp usage
  
  gui:
    enabled: true          # enable warp GUI menu
    items_per_page: 45     # warps per GUI page
    sort_alphabetically: true
```

---

The navigation system provides efficient movement throughout the server while supporting economic activities and maintaining appropriate safety measures and anti-abuse protections.