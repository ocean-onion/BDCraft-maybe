# API Reference

BDCraft provides a comprehensive internal API that can be used by the plugin's modules to interact with each other. This document provides a reference for the main API interfaces and methods.

## Core API

The core API provides access to the central functionality of BDCraft.

### BDCraftAPI

The main API interface for the plugin.

```java
public interface BDCraftAPI {
    /**
     * Gets the economy API.
     * 
     * @return The economy API
     */
    EconomyAPI getEconomyAPI();
    
    /**
     * Gets the progression API.
     * 
     * @return The progression API
     */
    ProgressionAPI getProgressionAPI();
    
    /**
     * Gets the vital API.
     * 
     * @return The vital API
     */
    VitalAPI getVitalAPI();
    
    /**
     * Gets the configuration manager.
     * 
     * @return The configuration manager
     */
    ConfigManager getConfigManager();
    
    /**
     * Gets the data manager.
     * 
     * @return The data manager
     */
    DataManager getDataManager();
    
    /**
     * Reloads the plugin configuration.
     */
    void reload();
}
```

### ConfigManager

Interface for accessing configuration files.

```java
public interface ConfigManager {
    /**
     * Gets the main configuration.
     * 
     * @return The main configuration
     */
    FileConfiguration getConfig();
    
    /**
     * Gets a specific configuration file.
     * 
     * @param name The name of the configuration file
     * @return The configuration file
     */
    FileConfiguration getConfig(String name);
    
    /**
     * Reloads all configuration files.
     */
    void reloadConfigs();
    
    /**
     * Saves a configuration file.
     * 
     * @param name The name of the configuration file
     */
    void saveConfig(String name);
}
```

### DataManager

Interface for accessing plugin data.

```java
public interface DataManager {
    /**
     * Gets a player's data.
     * 
     * @param uuid The player's UUID
     * @return The player data
     */
    PlayerData getPlayerData(UUID uuid);
    
    /**
     * Saves a player's data.
     * 
     * @param data The player data to save
     */
    void savePlayerData(PlayerData data);
    
    /**
     * Gets data for all players.
     * 
     * @return A map of player UUIDs to player data
     */
    Map<UUID, PlayerData> getAllPlayerData();
    
    /**
     * Saves all data.
     */
    void saveAll();
}
```

## Economy API

The economy API provides access to BDCraft's economy features.

### EconomyAPI

The main economy API interface.

```java
public interface EconomyAPI {
    /**
     * Gets a player's balance.
     * 
     * @param uuid The player's UUID
     * @return The player's balance
     */
    double getBalance(UUID uuid);
    
    /**
     * Sets a player's balance.
     * 
     * @param uuid The player's UUID
     * @param amount The new balance
     * @return Whether the operation was successful
     */
    boolean setBalance(UUID uuid, double amount);
    
    /**
     * Gives money to a player.
     * 
     * @param uuid The player's UUID
     * @param amount The amount to give
     * @return Whether the operation was successful
     */
    boolean giveMoney(UUID uuid, double amount);
    
    /**
     * Takes money from a player.
     * 
     * @param uuid The player's UUID
     * @param amount The amount to take
     * @return Whether the operation was successful
     */
    boolean takeMoney(UUID uuid, double amount);
    
    /**
     * Transfers money from one player to another.
     * 
     * @param from The sender's UUID
     * @param to The recipient's UUID
     * @param amount The amount to transfer
     * @return Whether the operation was successful
     */
    boolean transferMoney(UUID from, UUID to, double amount);
    
    /**
     * Gets the market API.
     * 
     * @return The market API
     */
    MarketAPI getMarketAPI();
    
    /**
     * Gets the auction API.
     * 
     * @return The auction API
     */
    AuctionAPI getAuctionAPI();
    
    /**
     * Gets the villager API.
     * 
     * @return The villager API
     */
    VillagerAPI getVillagerAPI();
}
```

### MarketAPI

Interface for interacting with markets.

```java
public interface MarketAPI {
    /**
     * Gets a market by ID.
     * 
     * @param marketId The market ID
     * @return The market, or null if not found
     */
    Market getMarket(UUID marketId);
    
    /**
     * Gets a player's market.
     * 
     * @param playerUuid The player's UUID
     * @return The player's market, or null if they don't have one
     */
    Market getPlayerMarket(UUID playerUuid);
    
    /**
     * Gets all markets.
     * 
     * @return A list of all markets
     */
    List<Market> getAllMarkets();
    
    /**
     * Creates a market for a player.
     * 
     * @param playerUuid The player's UUID
     * @param location The location of the market
     * @param name The name of the market
     * @return The created market, or null if creation failed
     */
    Market createMarket(UUID playerUuid, Location location, String name);
    
    /**
     * Deletes a market.
     * 
     * @param marketId The market ID
     * @return Whether the operation was successful
     */
    boolean deleteMarket(UUID marketId);
}
```

### AuctionAPI

Interface for interacting with the auction house.

```java
public interface AuctionAPI {
    /**
     * Lists an item for auction.
     * 
     * @param playerUuid The player's UUID
     * @param item The item to list
     * @param price The price
     * @return The auction ID, or null if listing failed
     */
    UUID listItem(UUID playerUuid, ItemStack item, double price);
    
    /**
     * Buys an item from the auction house.
     * 
     * @param playerUuid The player's UUID
     * @param auctionId The auction ID
     * @return Whether the purchase was successful
     */
    boolean buyItem(UUID playerUuid, UUID auctionId);
    
    /**
     * Cancels an auction.
     * 
     * @param playerUuid The player's UUID
     * @param auctionId The auction ID
     * @return Whether the cancellation was successful
     */
    boolean cancelAuction(UUID playerUuid, UUID auctionId);
    
    /**
     * Gets all active auctions.
     * 
     * @return A list of all active auctions
     */
    List<Auction> getAllAuctions();
    
    /**
     * Gets auctions by a specific player.
     * 
     * @param playerUuid The player's UUID
     * @return A list of the player's auctions
     */
    List<Auction> getPlayerAuctions(UUID playerUuid);
}
```

### VillagerAPI

Interface for interacting with BD villagers.

```java
public interface VillagerAPI {
    /**
     * Gets a BD villager by ID.
     * 
     * @param villagerId The villager ID
     * @return The BD villager, or null if not found
     */
    BDVillager getVillager(UUID villagerId);
    
    /**
     * Creates a BD villager.
     * 
     * @param type The villager type
     * @param location The location
     * @return The created BD villager, or null if creation failed
     */
    BDVillager createVillager(VillagerType type, Location location);
    
    /**
     * Removes a BD villager.
     * 
     * @param villagerId The villager ID
     * @return Whether the operation was successful
     */
    boolean removeVillager(UUID villagerId);
    
    /**
     * Gets all BD villagers.
     * 
     * @return A list of all BD villagers
     */
    List<BDVillager> getAllVillagers();
    
    /**
     * Gets BD villagers by type.
     * 
     * @param type The villager type
     * @return A list of BD villagers of the specified type
     */
    List<BDVillager> getVillagersByType(VillagerType type);
}
```

## Progression API

The progression API provides access to BDCraft's progression features.

### ProgressionAPI

The main progression API interface.

```java
public interface ProgressionAPI {
    /**
     * Gets a player's rank.
     * 
     * @param uuid The player's UUID
     * @return The player's rank
     */
    Rank getPlayerRank(UUID uuid);
    
    /**
     * Sets a player's rank.
     * 
     * @param uuid The player's UUID
     * @param rank The new rank
     * @return Whether the operation was successful
     */
    boolean setPlayerRank(UUID uuid, Rank rank);
    
    /**
     * Gets a player's experience.
     * 
     * @param uuid The player's UUID
     * @return The player's experience
     */
    int getPlayerExperience(UUID uuid);
    
    /**
     * Gives experience to a player.
     * 
     * @param uuid The player's UUID
     * @param amount The amount of experience to give
     * @return Whether the operation was successful
     */
    boolean giveExperience(UUID uuid, int amount);
    
    /**
     * Gets a player's rebirth level.
     * 
     * @param uuid The player's UUID
     * @return The player's rebirth level
     */
    int getRebirthLevel(UUID uuid);
    
    /**
     * Performs a rebirth for a player.
     * 
     * @param uuid The player's UUID
     * @return Whether the rebirth was successful
     */
    boolean performRebirth(UUID uuid);
}
```

## Vital API

The vital API provides access to BDCraft's vital features.

### VitalAPI

The main vital API interface.

```java
public interface VitalAPI {
    /**
     * Gets the home API.
     * 
     * @return The home API
     */
    HomeAPI getHomeAPI();
    
    /**
     * Gets the teleport API.
     * 
     * @return The teleport API
     */
    TeleportAPI getTeleportAPI();
    
    /**
     * Gets the chat API.
     * 
     * @return The chat API
     */
    ChatAPI getChatAPI();
    
    /**
     * Gets the tab API.
     * 
     * @return The tab API
     */
    TabAPI getTabAPI();
}
```

### HomeAPI

Interface for interacting with homes.

```java
public interface HomeAPI {
    /**
     * Gets a player's homes.
     * 
     * @param uuid The player's UUID
     * @return A map of home names to locations
     */
    Map<String, Location> getHomes(UUID uuid);
    
    /**
     * Sets a home for a player.
     * 
     * @param uuid The player's UUID
     * @param name The home name
     * @param location The location
     * @return Whether the operation was successful
     */
    boolean setHome(UUID uuid, String name, Location location);
    
    /**
     * Deletes a home for a player.
     * 
     * @param uuid The player's UUID
     * @param name The home name
     * @return Whether the operation was successful
     */
    boolean deleteHome(UUID uuid, String name);
    
    /**
     * Gets the maximum number of homes a player can have.
     * 
     * @param uuid The player's UUID
     * @return The maximum number of homes
     */
    int getMaxHomes(UUID uuid);
}
```

### TeleportAPI

Interface for teleportation.

```java
public interface TeleportAPI {
    /**
     * Teleports a player.
     * 
     * @param player The player
     * @param location The location
     * @return Whether the teleport was initiated successfully
     */
    boolean teleport(Player player, Location location);
    
    /**
     * Gets a warp location.
     * 
     * @param name The warp name
     * @return The warp location, or null if not found
     */
    Location getWarp(String name);
    
    /**
     * Sets a warp.
     * 
     * @param name The warp name
     * @param location The location
     * @return Whether the operation was successful
     */
    boolean setWarp(String name, Location location);
    
    /**
     * Deletes a warp.
     * 
     * @param name The warp name
     * @return Whether the operation was successful
     */
    boolean deleteWarp(String name);
    
    /**
     * Gets all warps.
     * 
     * @return A map of warp names to locations
     */
    Map<String, Location> getAllWarps();
}
```

### ChatAPI

Interface for chat management.

```java
public interface ChatAPI {
    /**
     * Formats a chat message.
     * 
     * @param player The player
     * @param message The message
     * @return The formatted message
     */
    String formatMessage(Player player, String message);
    
    /**
     * Mutes a player.
     * 
     * @param uuid The player's UUID
     * @param duration The duration in seconds, or 0 for permanent
     * @return Whether the operation was successful
     */
    boolean mutePlayer(UUID uuid, long duration);
    
    /**
     * Unmutes a player.
     * 
     * @param uuid The player's UUID
     * @return Whether the operation was successful
     */
    boolean unmutePlayer(UUID uuid);
    
    /**
     * Checks if a player is muted.
     * 
     * @param uuid The player's UUID
     * @return Whether the player is muted
     */
    boolean isMuted(UUID uuid);
    
    /**
     * Gets the chat format for a player.
     * 
     * @param player The player
     * @return The chat format
     */
    String getChatFormat(Player player);
}
```

### TabAPI

Interface for tab list management.

```java
public interface TabAPI {
    /**
     * Sets the tab list header.
     * 
     * @param header The header
     */
    void setHeader(String header);
    
    /**
     * Sets the tab list footer.
     * 
     * @param footer The footer
     */
    void setFooter(String footer);
    
    /**
     * Updates the tab list for a player.
     * 
     * @param player The player
     */
    void updateTabList(Player player);
    
    /**
     * Updates the tab list for all players.
     */
    void updateTabListForAll();
}
```

## Using the API Internally

Since BDCraft is designed as a self-contained plugin, these APIs are only used internally by the plugin's modules to interact with each other. The plugin does not expose these APIs for external plugins to use.

Example of internal API usage:

```java
// Getting a player's balance from another module
public void someMethod(UUID playerUuid) {
    double balance = plugin.getEconomyAPI().getBalance(playerUuid);
    // Do something with the balance
}

// Teleporting a player to their home
public void teleportToHome(Player player, String homeName) {
    UUID playerUuid = player.getUniqueId();
    Map<String, Location> homes = plugin.getVitalAPI().getHomeAPI().getHomes(playerUuid);
    Location homeLocation = homes.get(homeName);
    
    if (homeLocation != null) {
        plugin.getVitalAPI().getTeleportAPI().teleport(player, homeLocation);
    }
}
```