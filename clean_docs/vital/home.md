# Home System

The BDCraft home system allows players to set personal teleportation points, making navigation through the Minecraft world convenient and easy.

## Basic Home Commands

- `/home` - Teleport to your default home
- `/home <name>` - Teleport to a specific named home
- `/sethome` - Set your default home at your current location
- `/sethome <name>` - Set a named home at your current location
- `/delhome <name>` - Delete a specific home
- `/homes` - List all your set homes

## Home Features

### Multiple Homes

Players can set multiple homes based on their status:
- Regular players: 1 home
- Donors: 3 homes

### Home Limitations

- Homes must have unique names per player
- Home names must be between 3-16 characters
- Home names can only contain letters, numbers, and underscores
- Homes cannot be set in disabled worlds (configurable)
- Minimum distance between homes: 100 blocks (configurable)

### Home Teleportation

- 3-second warmup delay before teleportation completes
- Movement during warmup cancels teleportation
- 30-second cooldown between home teleports (configurable)
- Teleporting to a home records previous location for `/back` command

## Admin Home Commands

- `/home <player>` - View a list of a player's homes
- `/home <player> <name>` - Teleport to a specific player's home
- `/delhome <player> <name>` - Delete a player's home
- `/sethome <player> <name>` - Set a home for another player

## Home Permissions

- `bdcraft.home.set` - Allow setting homes
- `bdcraft.home.set.multiple` - Allow setting multiple homes
- `bdcraft.home.delete` - Allow deleting own homes
- `bdcraft.home.list` - Allow listing own homes
- `bdcraft.home.teleport` - Allow teleporting to own homes
- `bdcraft.home.limit.X` - Set maximum homes to X (where X is a number)

### Admin Permissions

- `bdcraft.home.admin` - Access all home commands
- `bdcraft.home.others` - View and teleport to other players' homes
- `bdcraft.home.others.set` - Set homes for other players
- `bdcraft.home.others.delete` - Delete other players' homes

## Home Configuration

The home system can be customized in the plugin configuration:

```yaml
homes:
  delays:
    teleport-warmup: 3     # Seconds to wait before teleport completes
    teleport-cooldown: 30  # Seconds between home teleports
  
  limits:
    default-max-homes: 1   # Default maximum homes per player
    min-distance: 100      # Minimum blocks between homes
    
  options:
    cancel-on-move: true   # Cancel teleport if player moves during warmup
    
  disabled-worlds:
    - "world_nether"       # Worlds where homes cannot be set
    - "world_the_end"
```