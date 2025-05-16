# Tab List System

The BDCraft Tab List System provides customizable player listings in the tab menu, enhancing server presentation and organization.

## Tab List Features

### Custom Headers and Footers

The plugin allows server administrators to set custom headers and footers for the tab list:

- **Header**: Displays at the top of the tab list
- **Footer**: Displays at the bottom of the tab list
- **Built-in Placeholders**: Use variables like {online}, {max}, {tps}, etc.
- **Color Support**: Full support for color codes and formatting

### Player Sorting

Players can be organized in the tab list based on various criteria:

- **Rank-Based Sorting**: Group players by their BDCraft ranks
- **Alphabetical Sorting**: Sort players alphabetically
- **Custom Groups**: Create custom groups for different player types

### Custom Player Info

Modify how players appear in the tab list:

- **Custom Prefixes**: Add rank or group prefixes before player names
- **Custom Suffixes**: Add additional information after player names
- **Name Colors**: Apply colors to player names based on rank

## Configuration

The tab list system is highly configurable in the plugin's configuration:

```yaml
tab:
  # Enable custom tab list
  enabled: true
  
  # Update interval (in seconds)
  update-interval: 2
  
  # Header text (supports color codes and built-in placeholders)
  header: "&6BDCraft Server\n&eOnline: {online}/{max}"
  
  # Footer text (supports color codes and built-in placeholders)
  footer: "&7TPS: {tps} | &7Memory: {memory_used}/{memory_max}"
  
  # Sorting method (rank, alphabetical, none)
  sorting: "rank"
  
  # Prefixes for each rank
  prefixes:
    newcomer: "&7[Newcomer] "
    farmer: "&a[Farmer] "
    expert-farmer: "&2[Expert Farmer] "
    master-farmer: "&6[Master Farmer] "
    agricultural-expert: "&5[Agricultural Expert] "
  
  # Whether to show player ping in the tab list
  show-ping: true
  
  # Whether to hide vanished players (admin feature)
  hide-vanished: true
```

## Built-in Placeholders

The tab list system includes these built-in placeholders:

- `{online}` - Current number of online players
- `{max}` - Maximum player capacity
- `{tps}` - Current server TPS (ticks per second)
- `{memory_used}` - Current memory usage
- `{memory_max}` - Maximum memory allocation
- `{player}` - Player's name (for per-player formats)
- `{rank}` - Player's rank (for per-player formats)
- `{rebirth}` - Player's rebirth level (for per-player formats)

## Admin Commands

Server administrators can manage the tab list with these commands:

- `/bdtab reload` - Reload the tab list configuration
- `/bdtab header <text>` - Set a custom header temporarily
- `/bdtab footer <text>` - Set a custom footer temporarily
- `/bdtab reset` - Reset to configuration defaults

## Permissions

- `bdcraft.tab.admin` - Access to tab list admin commands
- `bdcraft.tab.color` - Allow colored names in tab list
- `bdcraft.tab.bypass-hide` - See all players, even when hidden