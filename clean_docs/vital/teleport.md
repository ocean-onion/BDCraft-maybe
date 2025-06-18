# Teleport System

The BDCraft teleport system provides comprehensive player transportation infrastructure with request-based mechanics, warp management, and administrative controls.

## Player Transportation

### Request-Based Teleportation

- `/tp <player>` - Teleport yourself to another player
- `/tpa <player>` - Send a teleport request to a player
- `/tpaccept` - Accept an incoming teleport request
- `/tpdeny` - Deny an incoming teleport request
- `/tpahere <player>` - Request a player to teleport to you
- `/tpcancel` - Cancel a pending teleport request

### Location-Based Transportation
- `/warp <name>` - Teleport to a predefined warp point
- `/spawn` - Teleport to the server spawn point
- `/back` - Return to your previous location
- `/tpr` - Teleport to a random location (with cooldown)

## Transportation Infrastructure

### Safety and Timing Controls

- Teleport requests expire after 60 seconds if not accepted
- Teleportation has a 3-second warmup time by default
- Moving during warmup cancels the teleportation
- `/back` command has a 30-second cooldown
- `/tpr` (random teleport) has a 5-minute cooldown

### Admin Teleport Commands

- `/tp <player1> <player2>` - Teleport player1 to player2
- `/tpall` - Teleport all players to your location
- `/tppos <x> <y> <z>` - Teleport to specific coordinates
- `/tpworld <world>` - Teleport to a specific world

## Warp Infrastructure

Server warps provide administrative teleportation points for common destinations:

### Using Warps

- `/warp <name>` - Teleport to a named warp
- `/warps` - View a list of all available warps
- `/warpinfo <name>` - View details about a specific warp

### Managing Warps (Admin)

- `/setwarp <name>` - Create a new warp at your location
- `/delwarp <name>` - Delete an existing warp
- `/movewarp <name>` - Update an existing warp to your current location
- `/warp <name> <player>` - Send a player to a warp

## Teleport Permissions

### Player Permissions

- `bdcraft.teleport.tp` - Use `/tp` to teleport to players
- `bdcraft.teleport.tpa` - Use teleport requests
- `bdcraft.teleport.tpahere` - Request others to teleport to you
- `bdcraft.teleport.back` - Use the `/back` command
- `bdcraft.teleport.warp` - Use the warp system
- `bdcraft.teleport.spawn` - Use the `/spawn` command
- `bdcraft.teleport.tpr` - Use random teleport command

### Admin Permissions

- `bdcraft.teleport.admin` - Access to all teleport commands
- `bdcraft.teleport.bypass` - Bypass teleport cooldowns and delays
- `bdcraft.teleport.tpall` - Teleport all players to you
- `bdcraft.teleport.tppos` - Teleport to coordinates
- `bdcraft.warp.set` - Create and manage warps
- `bdcraft.warp.delete` - Delete warps

## Configuration

The teleport system can be customized in the plugin configuration:

```yaml
teleport:
  delays:
    tp-warmup: 3          # Seconds to wait before teleport completes
    back-cooldown: 30     # Seconds between /back usages
    tpr-cooldown: 300     # Seconds between random teleports
    request-timeout: 60   # Seconds before teleport requests expire
  
  options:
    cancel-on-move: true  # Cancel teleport if player moves during warmup
    back-on-death: true   # Allow /back to return to death location
    
  warp:
    menu-enabled: true    # Enable GUI menu for warps
    max-per-page: 45      # Maximum warps to show per page in GUI
```