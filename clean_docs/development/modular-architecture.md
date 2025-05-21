# BDCraft Modular Architecture

BDCraft is designed with a modular architecture that allows for clean separation of concerns, easier maintenance, and the ability to enable or disable specific features as needed. This document explains the structure of BDCraft's modules and how they interact.

## Core Architecture

The plugin is organized into three primary modules, each containing specialized submodules:

```
BDCraft
├── Economy Module
│   ├── Market System
│   ├── Auction System
│   └── Villager System
├── Progression Module
│   ├── Rank System
│   └── Rebirth System
└── Vital Module
    ├── Home System
    ├── Teleport System
    ├── Chat System
    └── Tab System
```

Each module and submodule is designed to function both independently and as part of the larger system, allowing for great flexibility in configuration.

## Module Interactions

The modules interact with each other through well-defined APIs, maintaining a clean separation of concerns while still allowing for powerful integrations.

### Example Interactions

1. **Economy → Progression**: When players complete market transactions, they can earn experience points that contribute to their rank progression.

2. **Progression → Vital**: A player's rank can determine how many homes they can set or which chat formatting options are available to them.

3. **Vital → Economy**: The teleport system may charge players for certain types of teleportation, integrating with the economy system.

## Module Manager

The ModuleManager is responsible for:

- Loading and initializing modules in the correct order
- Managing dependencies between modules
- Providing a central registry for modules to locate and communicate with each other
- Handling module enable/disable operations
- Coordinating configuration reloads across all modules

## Module Lifecycle

Each module follows a standard lifecycle:

1. **Registration**: Module is registered with the ModuleManager
2. **Initialization**: Module loads configuration and prepares resources
3. **Activation**: Module's listeners and commands are registered
4. **Runtime**: Module performs its functions
5. **Deactivation**: Module's listeners and commands are unregistered
6. **Shutdown**: Module saves data and releases resources

## Internal Module Structure

Each module follows a consistent internal structure:

```
ModuleName/
├── api/         # Public API interfaces
├── commands/    # Command implementations
├── config/      # Configuration classes
├── events/      # Event handlers
├── listeners/   # Event listeners
├── managers/    # Feature managers
├── models/      # Data models
└── utils/       # Utility classes
```

This consistent structure makes the codebase more maintainable and helps new developers quickly understand the organization.

## Module Configuration

Each module has its own configuration file in the plugin's config directory. The module is responsible for:

- Defining default configuration values
- Loading its configuration from file
- Validating configuration values
- Reloading configuration when requested

## Extending the System

BDCraft's modular design makes it easy to add new features:

1. Create a new submodule implementing the SubmoduleBase interface
2. Register the submodule with its parent module
3. Implement the required methods (onEnable, onDisable, etc.)
4. Add configuration options to the appropriate configuration file

## Common Module Services

All modules have access to common services:

- **DataService**: For persistent data storage
- **ConfigService**: For configuration management
- **PermissionService**: For checking and managing permissions
- **LogService**: For unified logging

## Example: Adding a New Feature

If you wanted to add a new feature to an existing module, such as a new type of auction for the Auction System:

1. Create a new class in the appropriate submodule
2. Register any necessary commands and listeners
3. Update the submodule's configuration
4. Implement the feature logic
5. Ensure proper integration with other systems

The modular architecture ensures that your new feature will cleanly integrate with the rest of the plugin without causing unexpected side effects.

## Module Dependencies

While modules are designed to be as independent as possible, some natural dependencies exist:

- The Economy module is fairly independent
- The Progression module may depend on the Economy module for certain features
- The Vital module may depend on both the Economy and Progression modules

These dependencies are managed through the Module Manager, which ensures proper initialization order and handles optional dependencies.

## Best Practices for Working with BDCraft's Architecture

1. **Respect Module Boundaries**: Access other modules only through their public APIs
2. **Maintain Consistency**: Follow established patterns for new code
3. **Document Integration Points**: Clearly document how your code interacts with other modules
4. **Consider Performance**: Be mindful of cross-module method calls in performance-critical code
5. **Handle Graceful Degradation**: If a module your code depends on is disabled, handle this gracefully