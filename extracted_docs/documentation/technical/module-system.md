# BDCraft Module System

The BDCraft plugin is built on a modular architecture that allows for flexible configuration, easy maintenance, and extensibility. This document explains the module system's design, how modules interact, and how server administrators can configure or extend the system.

## Module Architecture Overview

BDCraft consists of several independent but interconnected modules, each responsible for a specific aspect of the plugin's functionality:

```
BDCraft (Core)
├── BDVital
│   ├── TeleportSystem
│   ├── HomeSystem
│   ├── MessagingSystem
│   ├── MarketWarpSystem
│   └── DeathManagement
├── BDPerms
│   ├── PermissionManager
│   └── GroupManager
├── BDLogging
│   ├── ActionTracking
│   └── LogQuerySystem
├── BDEconomy
│   ├── CurrencyManager
│   ├── TradeSystem
│   ├── VillagerManager
│   ├── MarketSystem
│   └── RankSystem
└── BDPlaceholders
    └── TextReplacementSystem
```

## Core Module System

### Module Manager

The ModuleManager is the central component that handles loading, enabling, and disabling modules:

- Located in `com.bdcraft.modules.ModuleManager`
- Initializes modules in the correct order based on dependencies
- Provides API access between modules
- Handles module configuration and reload events

### Module Base Class

All modules extend the `BDModule` abstract class, which defines the standard lifecycle methods:

- `onEnable()`: Called when the module is enabled
- `onDisable()`: Called when the module is disabled
- `onReload()`: Called when the plugin is reloaded
- `getName()`: Returns the module identifier
- `getDependencies()`: Lists other modules this module depends on

## Primary Modules

### Economy Module

**Class**: `com.bdcraft.modules.economy.BDEconomyModule`

**Responsibility**: Manages all aspects of the BD economy system

**Components**:
- Villager Manager: Controls BD villagers and their trades
- Trade System: Handles trading mechanics and interfaces
- Crop System: Manages BD seeds, crops, and farming mechanics
- Market System: Manages player-driven markets and territories
- Rank System: Manages player ranks and progression
- Rebirth System: Handles the prestige rebirth mechanics

**Configuration**: `economy.yml`

### Permissions Module

**Class**: `com.bdcraft.modules.perms.BDPermsModule`

**Responsibility**: Manages permissions and group assignments

**Components**:
- Permission Manager: Handles permission assignments and checks
- Group Manager: Manages permission groups and hierarchies
- Chat Formatter: Handles rank-specific chat formatting
- Command Control: Provides cooldown and restriction mechanisms

**Configuration**: `permissions.yml`

### Vital Module

**Class**: `com.bdcraft.modules.vital.BDVitalModule`

**Responsibility**: Provides essential commands and utilities

**Components**:
- Teleport System: Player teleportation and requests
- Home System: Personal teleport locations
- Messaging System: Private messaging and offline mail
- Market Warps: Teleportation to player markets
- Death Management: Death tracking and return functionality

**Configuration**: Sections in `config.yml`

### Logging Module

**Class**: `com.bdcraft.modules.logging.BDLoggingModule`

**Responsibility**: Records and manages plugin activity logs

**Components**:
- Action Tracking: Records player and system activities
- Log Query System: Provides tools to search and filter logs
- Admin Tools: Administrative logging commands and interfaces

**Configuration**: `logging.yml`

### Placeholders Module

**Class**: `com.bdcraft.modules.placeholders.BDPlaceholdersModule`

**Responsibility**: Manages dynamic text replacement throughout the plugin

**Components**:
- Text Replacement System: Handles placeholder processing in messages
- Format Parsing: Processes formatting codes and styles
- Dynamic Value Providers: Generate real-time data for placeholders

**Configuration**: Sections in `config.yml`

## Inter-Module Communication

Modules communicate through well-defined APIs:

1. **Direct API Access**:
   ```java
   // Example of accessing the EconomyModule API
   BDEconomyModule economyModule = 
(BDEconomyModule) moduleManager.getModule("economy");
   economyModule.getCurrency(player);
   ```

2. **Event System**:
   - Custom events like `BDCropHarvestEvent`, `BDRankChangeEvent`
   - Standard Bukkit events with custom data

3. **Service Provider Interface**:
   - Modules can register service providers
   - Other modules can request services by interface

## Configuration Management

Each module has its own configuration section or file:

- **Module Enabling/Disabling**:
  ```yaml
  # In config.yml
  modules:
    bdvital: true
    bdperms: true
    bdlogging: true
    bdeconomy: true
    bdplaceholders: true
  ```

- **Module-specific Configuration**: Each module loads and manages its own settings

- **Configuration Reloading**: Changes to configuration files can be applied with `/bdadmin reload`

## Extending the Module System

### Creating Custom Modules

Developers can create custom modules by:

1. **Creating a Module Class**:
   ```java
   public class CustomModule extends BDModule {
       @Override
       public void onEnable() {
           // Initialization code
       }

       @Override
       public void onDisable() {
           // Cleanup code
       }

       @Override
       public String getName() {
           return "custom";
       }

       @Override
       public List<String> getDependencies() {
           return Arrays.asList("economy");
       }
   }
   ```

2. **Registering the Module**:
   - Add to plugin.yml
   - Register in ModuleManager initialization

### Integration Points

The module system provides several integration points:

- **Event Listeners**: Register Bukkit and custom event listeners
- **Command Registration**: Register custom commands via BDVital's command system
- **API Access**: Use provided API methods to interact with other modules
- **Economy Extensions**: Extend BDEconomy for custom market or trade functionality
- **Permission Extensions**: Add custom permission checks and group assignments
- **Placeholder Processors**: Create custom placeholders for your module's data

## Module Dependencies

Modules have dependencies that determine their loading order:

1. **Core** (always loaded first)
2. **BDPerms Module** (depends on Core)
3. **BDLogging Module** (depends on Core)
4. **BDVital Module** (depends on Core, may use BDPerms)
5. **BDEconomy Module** (depends on Core, BDPerms and BDLogging)
6. **BDPlaceholders Module** (depends on all other modules)

The dependency system ensures that modules are enabled in the correct order and that required functionality is available.

## Module Data Storage

Each module manages its own data storage:

- **BDVital Module**: Player homes, teleport requests, messaging data, death locations
- **BDPerms Module**: Permission groups, user permissions, rank-based permissions
- **BDLogging Module**: Activity logs, transaction records, action history
- **BDEconomy Module**: Player balances, ranks, villager data, market information
- **BDPlaceholders Module**: Cached placeholder data, format configurations

Data is typically stored in JSON or YAML files in the plugin's data folder.

## Error Handling

The module system includes robust error handling:

- **Startup Failures**: If a critical module fails to load, dependent modules are not enabled
- **Runtime Errors**: Modules isolate errors to prevent cascade failures
- **Graceful Degradation**: Non-critical modules can be disabled without affecting core functionality

## Conclusion

The BDCraft module system provides a flexible and maintainable architecture for the plugin. Server administrators can configure which modules are enabled, and developers can extend the system with custom modules. This modular approach ensures that the plugin can evolve and adapt to different server needs while maintaining stability and performance.