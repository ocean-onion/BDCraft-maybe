# BDCraft Modular Architecture

## Overview

BDCraft is built on a modular architecture that provides flexibility, maintainability, and extensibility. This document explains the high-level architecture, module interaction patterns, and guidelines for extending the system.

## Architecture Principles

The BDCraft architecture follows these key principles:

1. **Separation of Concerns**: Each module handles a specific aspect of functionality
2. **Loose Coupling**: Modules interact through well-defined APIs
3. **High Cohesion**: Related functionality is grouped within modules
4. **Extensibility**: System is designed to be extended with new modules
5. **Configuration Over Code**: Features can be enabled/disabled through configuration

## Core System

The core system (`BDCraft` and `ModuleManager` classes) provides:

- Plugin initialization and shutdown
- Module loading, enabling, and disabling
- Configuration management
- Dependency injection
- Event distribution

## Module System

### Module Interface

All modules implement the `BDModule` interface with these key methods:

```java
public interface BDModule {
    void onEnable();
    void onDisable();
    void onReload();
    String getName();
    List<String> getDependencies();
}
```

### Module Lifecycle

1. **Registration**: Modules are registered with the ModuleManager
2. **Initialization**: Dependencies are resolved and modules are sorted
3. **Enabling**: Modules are enabled in dependency order
4. **Runtime**: Modules perform their functionality
5. **Reloading**: Configuration changes are applied without restart
6. **Disabling**: Modules are disabled in reverse dependency order

## Primary Modules

### Economy Module

**Responsibility**: Manages the BD economy system

**Components**:
- Villager Manager
- Trade System
- Crop System
- Rank System
- Rebirth System

**Dependencies**: Core

### Permissions Module

**Responsibility**: Manages permissions

**Components**:
- Permission Manager
- Group Manager

- Token Manager

**Dependencies**: Core

### Vital Module

**Responsibility**: Provides essential commands and utilities

**Components**:
- Command System
- Player Utilities
- Informational Commands

**Dependencies**: Core

## Module Dependencies

Modules can specify dependencies on other modules, ensuring they are loaded in the correct order:

```java
@Override
public List<String> getDependencies() {
    return Arrays.asList("economy", "perms");
}
```

The ModuleManager resolves these dependencies and creates a loading order that satisfies all requirements.

## Inter-Module Communication

Modules communicate through several mechanisms:

### Direct API Access

Modules can access other modules' APIs through the ModuleManager:

```java
BDEconomyModule economyModule = (BDEconomyModule) moduleManager.getModule("economy");
economyModule.getCurrency(player);
```

### Event System

Modules can publish and subscribe to events:

```java
// Publishing an event
EventManager.fireEvent(new BDCropHarvestEvent(player, location, cropType, amount));

// Subscribing to an event
@EventHandler
public void onCropHarvest(BDCropHarvestEvent event) {
    // Handle the event
}
```

### Service Registration

Modules can register services for other modules to use:

```java
// Registering a service
moduleManager.registerService(EconomyProvider.class, new BDEconomyProvider());

// Accessing a service
EconomyProvider economyProvider = moduleManager.getService(EconomyProvider.class);
```

## Configuration System

Each module has its own configuration section or file:

```yaml
# Module enabling/disabling in config.yml
modules:
  economy: true
  perms: true
  auction: true
  rebirth: true
  tutorial: true
```

Modules load their configuration in the `onEnable()` method and apply changes in the `onReload()` method.

## Data Management

Each module manages its own data storage:

- File-based storage (JSON, YAML)
- Database storage (MySQL, SQLite)
- In-memory caching

The core system provides utilities for data serialization and storage.

## Error Handling

The module system includes robust error handling:

- Module initialization errors are logged and the module is disabled
- Runtime errors are contained within modules when possible
- Graceful degradation allows the system to continue functioning when a module fails



## Extending the System

### Creating a New Module

To create a new module:

1. Create a class that implements `BDModule`
2. Implement the required methods
3. Register the module with the ModuleManager

Example:

```java
public class CustomModule implements BDModule {
    
    private final BDCraft plugin;
    private final ModuleManager moduleManager;
    
    public CustomModule(BDCraft plugin, ModuleManager moduleManager) {
        this.plugin = plugin;
        this.moduleManager = moduleManager;
    }
    
    @Override
    public void onEnable() {
        // Initialize the module
    }
    
    @Override
    public void onDisable() {
        // Clean up resources
    }
    
    @Override
    public void onReload() {
        // Apply configuration changes
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

### Registration in ModuleManager

```java
moduleManager.registerModule(new CustomModule(this, moduleManager));
```

## Best Practices

1. **Keep Modules Focused**: Each module should have a single responsibility
2. **Use APIs for Communication**: Don't access other modules' internal state directly
3. **Handle Errors Gracefully**: Prevent cascading failures by containing errors
4. **Document APIs**: Clearly document module APIs for other developers
5. **Follow Naming Conventions**: Use consistent naming for consistency

## Performance Considerations

1. **Lazy Initialization**: Initialize resources only when needed
2. **Caching**: Cache frequently accessed data
3. **Asynchronous Processing**: Use async tasks for heavy operations
4. **Resource Cleanup**: Properly dispose of resources in `onDisable()`

## Module Development Checklist

- [ ] Implement required `BDModule` methods
- [ ] Define clear module API
- [ ] Specify correct dependencies
- [ ] Create configuration section/file
- [ ] Implement proper error handling
- [ ] Add event listeners as needed
- [ ] Document public API

- [ ] Write unit tests for module functionality

This modular architecture allows BDCraft to grow and evolve while maintaining stability and performance. By following these guidelines, developers can create new modules that seamlessly integrate with the existing system.