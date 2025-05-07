# BDCraft Technical Guide

## Introduction

This technical guide provides detailed information for developers and advanced users working with the BDCraft plugin. It covers the technical architecture, API usage, internal systems, and advanced configuration options.

BDCraft is a comprehensive Minecraft server management plugin built for Paper servers version 1.21+. It implements a modular design with three core modules: BDEconomyModule, BDVitalModule, and BDPermsModule.

## Technology Stack

- **Java 21+**: Required for running the plugin
- **Paper API**: Core Minecraft server interface (version 1.21+)
- **Adventure API**: Modern text and UI components

- **MySQL/MariaDB**: Optional database backend
- **SQLite**: Alternative embedded database option
- **Gson**: JSON serialization and deserialization
- **Guava**: Utility libraries and data structures
- **JUnit 5**: Testing framework

## System Architecture

### Core Architecture

BDCraft follows a modular architecture with a central plugin class that orchestrates module loading and communication:

```
com.bdcraft.plugin/
├── BDCraft.java                # Main plugin class
├── config/                     # Configuration handlers
├── modules/                    # Module system
│   ├── BDModule.java           # Base module class
│   ├── economy/                # Economy module
│   ├── vital/                  # Essential utilities module
│   └── perms/                  # Permissions module
├── api/                        # Public API classes
│   ├── EconomyAPI.java
│   ├── VillagerAPI.java
│   └── PermissionAPI.java
├── data/                       # Data storage
├── events/                     # Custom events
├── economy/                    # Economy implementation
├── villagers/                  # Villager implementation
├── markets/                    # Market implementation

└── util/                       # Utility classes
```

### Module System

Each module extends the abstract `BDModule` class and implements the following lifecycle methods:

- `onEnable()`: Called when the module is enabled
- `onDisable()`: Called when the module is disabled
- `reload()`: Reloads the module configuration
- `saveData()`: Saves module data to persistent storage

Modules communicate through the central BDCraft class using a service-based approach, allowing for loose coupling between components.

### Data Storage

BDCraft uses a flexible data storage system with multiple backends:

1. **File-based Storage**: YAML files for configuration and basic data
2. **SQLite**: Embedded database for medium-sized servers
3. **MySQL/MariaDB**: External database for large servers and multi-server networks

Data access is abstracted through repository interfaces, allowing for consistent access regardless of the underlying storage mechanism.

### Threading Model

BDCraft follows Bukkit's threading guidelines:

- Main thread: All Bukkit API calls
- Async thread pool: Database operations and file I/O
- Scheduled tasks: Regular operations like saving data

Critical sections are properly synchronized to prevent race conditions and ensure data consistency.

## Economy System Implementation

### BD Currency

The BD currency is implemented as an extension of the Vault economy system:

1. **BDEconomyProvider**: Implements the Vault Economy interface
2. **BDCurrencyManager**: Manages currency transactions and balances
3. **BDTransactionLogger**: Records all currency movements for auditing

### Crops and Farming

The custom crop system uses the following components:

1. **BDCropManager**: Manages crop types, growth rates, and yields
2. **BDCropListener**: Handles events related to crop farming
3. **BDSeedRegistry**: Registers and manages custom seed types

The crops are represented using block data with custom NBT tags for additional properties.

### Villager AI

Custom villager behavior is implemented through:

1. **BDVillagerFactory**: Creates custom villager entities
2. **BDTradeManager**: Manages trade offers and pricing
3. **BDVillagerListener**: Handles villager interaction events
4. **BDPathfindingGoals**: Custom pathfinding for special behaviors

## Permission System

The permission system extends beyond Bukkit's basic functionality:

1. **BDPermissionManager**: Manages permission groups and assignments
2. **BDPermissionResolver**: Resolves permission inheritance
3. **BDPermissionListener**: Handles permission-related events
4. **BDChatFormatter**: Formats chat based on permission groups

## Web Interface

The integrated web server provides a REST API and web interface:

1. **BDWebServer**: Spring Boot application for web hosting
2. **BDRestController**: REST endpoints for API access
3. **BDWebSecurity**: Token-based authentication and security
4. **BDDashboardController**: Admin dashboard views

API endpoints follow REST principles with proper authentication and rate limiting.

## Configuration System

BDCraft uses a hierarchical configuration system:

1. **ConfigManager**: Manages configuration loading and saving
2. **ModuleConfig**: Module-specific configuration
3. **YamlConfig**: YAML-based configuration implementation
4. **ConfigMigrator**: Handles configuration updates between versions

## Events and Listeners

The plugin uses Bukkit's event system with custom events:

1. **BDEconomyEvents**: Economy-related events
2. **BDVillagerEvents**: Villager-related events
3. **BDMarketEvents**: Market-related events
4. **BDPlayerEvents**: Player progression events

## API Usage Examples

### Economy API

```java
// Get player balance
BDEconomyAPI economyAPI = BDCraft.getInstance().getEconomyAPI();
double balance = economyAPI.getBalance(player);

// Add currency to player
economyAPI.depositCurrency(player, amount);

// Check if player can afford something
boolean canAfford = economyAPI.has(player, cost);
```

### Villager API

```java
// Create a dealer villager
BDVillagerAPI villagerAPI = BDCraft.getInstance().getVillagerAPI();
BDVillager dealer = villagerAPI.createDealer(location);

// Add custom trade
BDTrade trade = new BDTrade()
    .setInput(new ItemStack(Material.EMERALD, 5))
    .setOutput(BDItems.GREEN_SEED, 1);
dealer.addTrade(trade);
```

### Permission API

```java
// Check if player has a specific BD permission
BDPermissionAPI permAPI = BDCraft.getInstance().getPermissionAPI();
boolean hasPermission = permAPI.hasPermission(player, "bdcraft.market.create");

// Get player's permission group
String group = permAPI.getGroup(player);
```

## Performance Optimization

BDCraft includes several performance optimizations:

1. **Caching**: Frequently accessed data is cached to reduce database load
2. **Async Processing**: Heavy operations run asynchronously
3. **Batch Processing**: Database operations are batched where possible
4. **Memory Management**: Careful object pooling and reuse

## Security Considerations

The plugin implements several security measures:

1. **SQL Injection Prevention**: Prepared statements for all database queries
2. **XSS Prevention**: Output escaping in web interface
3. **CSRF Protection**: Token-based form submission
4. **Authentication**: Secure token generation and validation
5. **Authorization**: Strict permission checking

## Testing Framework

BDCraft includes a comprehensive testing framework:

1. **Unit Tests**: For isolated class testing
2. **Integration Tests**: For testing module interactions
3. **Mock Server**: For testing Bukkit API interactions
4. **Load Testing**: For performance verification

## Extending BDCraft

Developers can extend BDCraft in several ways:

1. **API Usage**: Utilize the provided APIs for integration
2. **Event Listening**: Listen to BDCraft custom events
3. **Module Creation**: Create custom modules that extend BDModule
4. **Hook Implementations**: Implement hooks for specific functionality

## Troubleshooting

Common issues and their solutions:

1. **Database Connection Issues**: Check credentials and network connectivity
2. **Performance Problems**: Enable debug logging and check for bottlenecks
3. **Plugin Conflicts**: Verify compatibility with other installed plugins
4. **Web Interface Issues**: Check port availability and firewall settings

## Version Migration

When upgrading from older versions:

1. **Configuration Migration**: Automatically updates config file structure
2. **Database Schema Updates**: Handles database schema changes
3. **API Compatibility**: Maintains backward compatibility where possible

## Documentation Standards

All code follows these documentation standards:

1. **Javadoc**: All public methods and classes include Javadoc
2. **README**: Each module includes a comprehensive README
3. **Examples**: Code examples for common use cases
4. **Changelog**: Detailed version changes

## Third-Party Integration

BDCraft supports integration with various third-party plugins:

1. **Vault**: Economy integration
2. **PlaceholderAPI**: Dynamic text replacements
3. **WorldGuard**: Region protection integration
4. **Dynmap**: Map visualization

## Advanced Configuration

Advanced configuration options are available in the following files:

1. **advanced.yml**: Technical settings for advanced users
2. **database.yml**: Detailed database configuration
3. **web-server.yml**: Web server configuration
4. **performance.yml**: Performance tuning options

## Conclusion

This technical guide provides an overview of BDCraft's architecture and implementation details. For further information, consult the API documentation and code comments.
