# API Reference

BDCraft provides a comprehensive internal API that can be used by the plugin's modules to interact with each other. This document provides a reference for the main API interfaces and methods.

## Core API

The core API provides access to the central functionality of BDCraft.

### BDCraftAPI

The main API interface for the plugin.

- `getEconomyAPI()` - Gets the economy API
- `getProgressionAPI()` - Gets the progression API
- `getVitalAPI()` - Gets the vital API
- `getConfigManager()` - Gets the configuration manager
- `getDataManager()` - Gets the data manager
- `reload()` - Reloads the plugin configuration

### ConfigManager

Interface for accessing configuration files.

- `getConfig()` - Gets the main configuration
- `getConfig(String name)` - Gets a specific configuration file
- `reloadConfigs()` - Reloads all configuration files
- `saveConfig(String name)` - Saves a configuration file

### DataManager

Interface for accessing plugin data.

- `getPlayerData(UUID uuid)` - Gets a player's data
- `savePlayerData(PlayerData data)` - Saves a player's data
- `getAllPlayerData()` - Gets data for all players
- `saveAll()` - Saves all data

## Economy API

The economy API provides access to BDCraft's economy features.

### EconomyAPI

The main economy API interface.

- `getBalance(UUID uuid)` - Gets a player's balance
- `setBalance(UUID uuid, double amount)` - Sets a player's balance
- `giveMoney(UUID uuid, double amount)` - Gives money to a player
- `takeMoney(UUID uuid, double amount)` - Takes money from a player
- `transferMoney(UUID from, UUID to, double amount)` - Transfers money from one player to another
- `getMarketAPI()` - Gets the market API
- `getAuctionAPI()` - Gets the auction API
- `getVillagerAPI()` - Gets the villager API

### MarketAPI

Interface for interacting with markets.

- `getMarket(UUID marketId)` - Gets a market by ID
- `getPlayerMarket(UUID playerUuid)` - Gets a player's market
- `getAllMarkets()` - Gets all markets
- `handleMarketTokenPlacement(Player player, Location location, ItemFrame frame)` - Processes market creation when a player places a market token
- `deleteMarket(UUID marketId)` - Deletes a market

### AuctionAPI

Interface for interacting with the auction house.

- `listItem(UUID playerUuid, ItemStack item, double price)` - Lists an item for auction
- `buyItem(UUID playerUuid, UUID auctionId)` - Buys an item from the auction house
- `cancelAuction(UUID playerUuid, UUID auctionId)` - Cancels an auction
- `getAllAuctions()` - Gets all active auctions
- `getPlayerAuctions(UUID playerUuid)` - Gets auctions by a specific player

### VillagerAPI

Interface for interacting with BD villagers.

- `getVillager(UUID villagerId)` - Gets a BD villager by ID
- `createVillager(VillagerType type, Location location)` - Creates a BD villager
- `removeVillager(UUID villagerId)` - Removes a BD villager
- `getAllVillagers()` - Gets all BD villagers
- `getVillagersByType(VillagerType type)` - Gets BD villagers by type

## Progression API

The progression API provides access to BDCraft's progression features.

### ProgressionAPI

The main progression API interface.

- `getPlayerRank(UUID uuid)` - Gets a player's rank
- `setPlayerRank(UUID uuid, Rank rank)` - Sets a player's rank
- `getPlayerExperience(UUID uuid)` - Gets a player's experience
- `giveExperience(UUID uuid, int amount)` - Gives experience to a player
- `getRebirthLevel(UUID uuid)` - Gets a player's rebirth level
- `performRebirth(UUID uuid)` - Performs a rebirth for a player
- `getAchievementAPI()` - Gets the achievement API

#### Achievement Management
```java
// Get player achievements
List<Achievement> getPlayerAchievements(UUID playerId);

// Check achievement completion
boolean hasAchievement(UUID playerId, String achievementId);

// Grant achievement
void grantAchievement(UUID playerId, String achievementId);

// Get achievement progress
double getAchievementProgress(UUID playerId, String achievementId);

// Open achievement GUI
void openAchievementGUI(Player player, String category);

// Get achievement statistics
AchievementStats getPlayerStats(UUID playerId);
```

## Vital API

The vital API provides access to BDCraft's vital features.

### VitalAPI

The main vital API interface.

- `getHomeAPI()` - Gets the home API
- `getTeleportAPI()` - Gets the teleport API
- `getChatAPI()` - Gets the chat API
- `getTabAPI()` - Gets the tab API

### HomeAPI

Interface for interacting with homes.

- `getHomes(UUID uuid)` - Gets a player's homes
- `setHome(UUID uuid, String name, Location location)` - Sets a home for a player
- `deleteHome(UUID uuid, String name)` - Deletes a home for a player
- `getMaxHomes(UUID uuid)` - Gets the maximum number of homes a player can have

### TeleportAPI

Interface for teleportation.

- `teleport(Player player, Location location)` - Teleports a player
- `getWarp(String name)` - Gets a warp location
- `setWarp(String name, Location location)` - Sets a warp
- `deleteWarp(String name)` - Deletes a warp
- `getAllWarps()` - Gets all warps

### ChatAPI

Interface for chat management.

- `formatMessage(Player player, String message)` - Formats a chat message
- `mutePlayer(UUID uuid, long duration)` - Mutes a player
- `unmutePlayer(UUID uuid)` - Unmutes a player
- `isMuted(UUID uuid)` - Checks if a player is muted
- `getChatFormat(Player player)` - Gets the chat format for a player

### TabAPI

Interface for tab list management.

- `setHeader(String header)` - Sets the tab list header
- `setFooter(String footer)` - Sets the tab list footer
- `updateTabList(Player player)` - Updates the tab list for a player
- `updateTabListForAll()` - Updates the tab list for all players

## API Usage

BDCraft's API is designed for internal use only. The plugin modules use these APIs to interact with each other while maintaining a clean separation of concerns. Since BDCraft is self-contained, these APIs are not exposed for use by external plugins.

The API follows a hierarchical design where each major component (Economy, Progression, Vital) has its own API that provides access to more specific functionality. This design allows for modular development and easier maintenance of the plugin's codebase.