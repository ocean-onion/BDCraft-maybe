# API Reference

BDCraft features a robust internal API system that allows different modules to communicate with each other efficiently. This document provides a reference for developers who need to understand how the plugin's systems work internally.

## Core API Interfaces

### ModuleManager Interface

The ModuleManager interface defines how modules are loaded, enabled, and disabled:

```java
public interface ModuleManager {
    String getName();
    void enable(BDCraft plugin);
    void disable();
    void reload();
    boolean isEnabled();
    Object getSubmodule(String name);
    void registerSubmodule(Object submodule);
}
```

### SubmoduleBase Interface

The SubmoduleBase interface defines the basic functionality for all submodules:

```java
public interface SubmoduleBase {
    String getName();
    void enable(ModuleManager parent);
    void disable();
    void reload();
    boolean isEnabled();
}
```

## Economy API

The Economy API provides interfaces for currency management and economic operations.

### EconomyAPI Interface

```java
public interface EconomyAPI {
    double getBalance(UUID playerUuid);
    boolean hasBalance(UUID playerUuid, double amount);
    boolean withdraw(UUID playerUuid, double amount);
    boolean deposit(UUID playerUuid, double amount);
    boolean transfer(UUID fromUuid, UUID toUuid, double amount);
    String formatCurrency(double amount);
    String getCurrencyName(double amount);
    String getCurrencySymbol();
}
```

### MarketAPI Interface

```java
public interface MarketAPI {
    UUID createMarket(UUID ownerUuid, String name);
    boolean deleteMarket(UUID marketId);
    Market getMarket(UUID marketId);
    List<Market> getMarkets();
    boolean addItem(UUID marketId, ItemStack item, double price);
    boolean removeItem(UUID marketId, UUID itemId);
    boolean purchaseItem(UUID buyerUuid, UUID itemId);
}
```

### AuctionAPI Interface

```java
public interface AuctionAPI {
    UUID createAuction(UUID sellerUuid, ItemStack item, double startingBid, long duration);
    boolean cancelAuction(UUID auctionId);
    boolean placeBid(UUID bidderUuid, UUID auctionId, double amount);
    boolean buyoutAuction(UUID buyerUuid, UUID auctionId);
    List<Auction> getActiveAuctions();
    Auction getAuction(UUID auctionId);
}
```

### VillagerAPI Interface

```java
public interface VillagerAPI {
    UUID createVillager(VillagerType type, Location location);
    boolean removeVillager(UUID villagerUuid);
    boolean addTrade(UUID villagerUuid, ItemStack input1, ItemStack input2, ItemStack output);
    boolean removeTrade(UUID villagerUuid, int index);
    List<VillagerTrade> getTrades(UUID villagerUuid);
    boolean restockVillager(UUID villagerUuid);
}
```

## Progression API

The Progression API provides interfaces for rank and rebirth management.

### ProgressionAPI Interface

```java
public interface ProgressionAPI {
    Rank getPlayerRank(UUID playerUuid);
    String getRankDisplayName(Rank rank);
    int getPlayerRebirthLevel(UUID playerUuid);
    int getPlayerExperience(UUID playerUuid);
    int addPlayerExperience(UUID playerUuid, int amount);
    double getProgressPercentage(UUID playerUuid);
    Rank getNextRank(UUID playerUuid);
    
    enum Rank {
        NEWCOMER,
        FARMER,
        EXPERT_FARMER,
        MASTER_FARMER,
        AGRICULTURAL_EXPERT
    }
}
```

### RankAPI Interface

```java
public interface RankAPI {
    int getPlayerRank(UUID playerUuid);
    boolean setPlayerRank(UUID playerUuid, int rank);
    String getRankName(int rank);
    String getRankDisplayName(int rank);
    int getRequiredExperience(int rank);
    boolean rankUp(UUID playerUuid);
    List<String> getRankPermissions(int rank);
}
```

### RebirthAPI Interface

```java
public interface RebirthAPI {
    int getRebirthLevel(UUID playerUuid);
    boolean setRebirthLevel(UUID playerUuid, int level);
    boolean performRebirth(UUID playerUuid);
    double getRebirthMultiplier(UUID playerUuid);
    boolean isEligibleForRebirth(UUID playerUuid);
    int getRequiredRank(int rebirthLevel);
}
```

## Vital API

The Vital API provides interfaces for essential utility features.

### HomeAPI Interface

```java
public interface HomeAPI {
    boolean setHome(UUID playerUuid, String name, Location location);
    boolean deleteHome(UUID playerUuid, String name);
    Location getHome(UUID playerUuid, String name);
    List<String> getHomeList(UUID playerUuid);
    int getMaxHomes(UUID playerUuid);
    boolean isHomePublic(UUID playerUuid, String name);
    boolean setHomeVisibility(UUID playerUuid, String name, boolean isPublic);
}
```

### TeleportAPI Interface

```java
public interface TeleportAPI {
    boolean teleport(UUID playerUuid, Location destination);
    boolean teleport(UUID playerUuid, UUID targetUuid);
    boolean sendTeleportRequest(UUID requesterUuid, UUID targetUuid);
    boolean acceptTeleportRequest(UUID targetUuid);
    boolean denyTeleportRequest(UUID targetUuid);
    boolean teleportToCoordinates(UUID playerUuid, World world, double x, double y, double z);
    Location getLastLocation(UUID playerUuid);
    boolean teleportRandom(UUID playerUuid, World world, int radius);
}
```

### ChatAPI Interface

```java
public interface ChatAPI {
    boolean sendPrivateMessage(UUID senderUuid, UUID receiverUuid, String message);
    boolean broadcastMessage(String message);
    boolean setNickname(UUID playerUuid, String nickname);
    String getNickname(UUID playerUuid);
    boolean addToChannel(UUID playerUuid, String channel);
    boolean removeFromChannel(UUID playerUuid, String channel);
    String getActiveChannel(UUID playerUuid);
    boolean setActiveChannel(UUID playerUuid, String channel);
    boolean createChannel(String name, UUID ownerUuid);
    boolean deleteChannel(String name);
}
```

### TabAPI Interface

```java
public interface TabAPI {
    boolean updateTabList(UUID playerUuid);
    boolean updateAllTabLists();
    boolean setHeader(String header);
    boolean setFooter(String footer);
    boolean setPlayerListName(UUID playerUuid, String name);
    boolean toggleAFK(UUID playerUuid);
    boolean isAFK(UUID playerUuid);
}
```

## Data Models

The plugin uses several data models to represent entities in the system:

### Economy Models

- **Currency**: Represents a currency type with name, symbol, and format.
- **Transaction**: Represents a currency transaction with sender, receiver, amount, and timestamp.
- **Market**: Represents a player-owned market with items for sale.
- **MarketItem**: Represents an item for sale in a market.
- **Auction**: Represents an auction with current bids and status.
- **Bid**: Represents a bid on an auction.
- **Villager**: Represents a custom villager with trades.
- **VillagerTrade**: Represents a trade offered by a villager.

### Progression Models

- **PlayerRank**: Represents a player's rank and experience.
- **RankDefinition**: Defines a rank with name, permissions, and requirements.
- **Rebirth**: Represents a player's rebirth status and benefits.
- **RebirthDefinition**: Defines a rebirth level with requirements and rewards.

### Vital Models

- **Home**: Represents a player's home location.
- **TeleportRequest**: Represents a request to teleport to another player.
- **ChatChannel**: Represents a chat channel with members and settings.
- **PlayerSettings**: Represents a player's personal settings for various features.

## Event System

BDCraft uses an event system for communication between modules:

### Economy Events

- **BalanceChangeEvent**: Fired when a player's balance changes.
- **TransactionEvent**: Fired when a transaction occurs between players.
- **MarketCreateEvent**: Fired when a market is created.
- **MarketDeleteEvent**: Fired when a market is deleted.
- **MarketPurchaseEvent**: Fired when an item is purchased from a market.
- **AuctionCreateEvent**: Fired when an auction is created.
- **AuctionBidEvent**: Fired when a bid is placed on an auction.
- **AuctionEndEvent**: Fired when an auction ends.

### Progression Events

- **RankChangeEvent**: Fired when a player's rank changes.
- **ExperienceGainEvent**: Fired when a player gains experience.
- **RebirthEvent**: Fired when a player is reborn.

### Vital Events

- **HomeCreateEvent**: Fired when a home is created.
- **HomeDeleteEvent**: Fired when a home is deleted.
- **TeleportEvent**: Fired when a player teleports.
- **TeleportRequestEvent**: Fired when a teleport request is sent.
- **ChatMessageEvent**: Fired when a chat message is sent.
- **ChannelJoinEvent**: Fired when a player joins a chat channel.
- **ChannelLeaveEvent**: Fired when a player leaves a chat channel.

## Using the API Internally

The API interfaces are used for communication between modules. For example, when a player sells an item in a market:

1. The MarketModule processes the sale.
2. It calls the EconomyAPI to add funds to the seller.
3. It also calls the ProgressionAPI to add experience to the seller.
4. It fires a MarketPurchaseEvent that other modules can listen for.

This modular approach keeps the code clean and maintainable while allowing modules to work together seamlessly.

## Configuration Access

Modules can access configuration options through the ConfigManager:

```java
ConfigManager configManager = plugin.getConfigManager();
FileConfiguration config = configManager.getConfig(ConfigType.ECONOMY);
double taxRate = config.getDouble("market.tax-rate", 0.05);
```

## Persistence

Data is stored using a centralized persistence system:

```java
BDCraft plugin = getBDCraftInstance();
plugin.getDataManager().savePlayerData(playerUuid, dataKey, value);
Object value = plugin.getDataManager().getPlayerData(playerUuid, dataKey);
```

This ensures consistent data handling across all modules.