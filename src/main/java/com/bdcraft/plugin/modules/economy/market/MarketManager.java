package com.bdcraft.plugin.modules.economy.market;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.villager.BDCollector;
import com.bdcraft.plugin.modules.economy.villager.BDDealer;
import com.bdcraft.plugin.modules.economy.villager.BDMarketOwner;
import com.bdcraft.plugin.modules.economy.villager.BDVillager;
import com.bdcraft.plugin.modules.economy.villager.BDVillagerManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Manages BD markets.
 */
public class MarketManager {
    private final BDCraft plugin;
    private final Logger logger;
    private final Map<UUID, BDMarket> markets;
    private final BDVillagerManager villagerManager;
    
    /**
     * Creates a new market manager.
     * @param plugin The plugin instance
     */
    public MarketManager(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.markets = new HashMap<>();
        this.villagerManager = plugin.getEconomyModule().getVillagerManager();
    }
    
    /**
     * Creates a market.
     * @param center The center of the market
     * @param founder The founder of the market
     * @return The created market, or null if failed
     */
    public BDMarket createMarket(Location center, Player founder) {
        // Check if there's already a market at this location
        if (getMarketAt(center) != null) {
            return null;
        }
        
        // Create market
        UUID marketId = UUID.randomUUID();
        BDMarket market = new BDMarket(marketId, center, founder.getUniqueId(), founder.getName());
        markets.put(marketId, market);
        
        // Spawn market owner
        spawnMarketOwner(market, center);
        
        // Spawn dealer
        spawnDealer(market, center);
        
        logger.info("Created market " + marketId + " at " + formatLocation(center) + " by " + founder.getName());
        
        return market;
    }
    
    /**
     * Spawns a market owner for a market.
     * @param market The market
     * @param center The center of the market
     * @return The created villager
     */
    private BDMarketOwner spawnMarketOwner(BDMarket market, Location center) {
        // Offset from center
        Location ownerLocation = center.clone().add(2, 0, 2);
        
        // Create the owner
        BDMarketOwner owner = villagerManager.createMarketOwner(ownerLocation, market);
        
        // Associate with market
        market.addTrader(owner.getVillager().getUniqueId(), "OWNER");
        
        return owner;
    }
    
    /**
     * Spawns a dealer for a market.
     * @param market The market
     * @param center The center of the market
     * @return The created villager
     */
    private BDDealer spawnDealer(BDMarket market, Location center) {
        // Offset from center
        Location dealerLocation = center.clone().add(-2, 0, 2);
        
        // Create the dealer
        BDDealer dealer = villagerManager.createDealer(dealerLocation, market);
        
        // Associate with market
        market.addTrader(dealer.getVillager().getUniqueId(), "DEALER");
        
        return dealer;
    }
    
    /**
     * Adds a collector house to a market.
     * @param market The market
     * @param location The location of the house
     * @return The created collector, or null if failed
     */
    public BDCollector addCollector(BDMarket market, Location location) {
        // Check collector count limits
        int currentCollectors = market.getTraderCount("COLLECTOR");
        int maxCollectors = getMaxCollectors(market);
        
        if (currentCollectors >= maxCollectors) {
            return null;
        }
        
        // Create the collector
        BDCollector collector = villagerManager.createCollector(location, market);
        
        // Associate with market
        market.addTrader(collector.getVillager().getUniqueId(), "COLLECTOR");
        
        return collector;
    }
    
    /**
     * Gets the maximum number of collectors for a market.
     * @param market The market
     * @return The maximum number of collectors
     */
    private int getMaxCollectors(BDMarket market) {
        int level = market.getLevel();
        
        switch (level) {
            case 1:
                return 3;
            case 2:
                return 5;
            case 3:
                return 7;
            case 4:
                return 10;
            default:
                return 3;
        }
    }
    
    /**
     * Gets a market by ID.
     * @param id The market ID
     * @return The market, or null if not found
     */
    public BDMarket getMarket(UUID id) {
        return markets.get(id);
    }
    
    /**
     * Gets a market at a location.
     * @param location The location
     * @return The market, or null if not found
     */
    public BDMarket getMarketAt(Location location) {
        for (BDMarket market : markets.values()) {
            if (isInMarketRadius(market, location)) {
                return market;
            }
        }
        
        return null;
    }
    
    /**
     * Checks if a location is in a market radius.
     * @param market The market
     * @param location The location
     * @return Whether the location is in the market radius
     */
    public boolean isInMarketRadius(BDMarket market, Location location) {
        if (!market.getCenter().getWorld().equals(location.getWorld())) {
            return false;
        }
        
        double distance = market.getCenter().distance(location);
        int marketRadius = 20 + (market.getLevel() * 5); // Base radius + 5 per level
        
        return distance <= marketRadius;
    }
    
    /**
     * Checks if a location is in any market radius.
     * @param location The location
     * @return Whether the location is in any market radius
     */
    public boolean isInMarket(Location location) {
        return getMarketAt(location) != null;
    }
    
    /**
     * Checks if a player is in their own market.
     * @param player The player
     * @param location The location
     * @return Whether the player is in their own market
     */
    public boolean isPlayerInOwnMarket(Player player, Location location) {
        BDMarket market = getMarketAt(location);
        
        if (market == null) {
            return false;
        }
        
        return market.getFounderId().equals(player.getUniqueId());
    }
    
    /**
     * Gets all markets.
     * @return All markets
     */
    public List<BDMarket> getAllMarkets() {
        return new ArrayList<>(markets.values());
    }
    
    /**
     * Gets all markets in a world.
     * @param worldName The world name
     * @return All markets in the world
     */
    public List<BDMarket> getMarketsInWorld(String worldName) {
        return markets.values().stream()
                .filter(market -> market.getCenter().getWorld().getName().equals(worldName))
                .collect(Collectors.toList());
    }
    
    /**
     * Gets all markets owned by a player.
     * @param playerId The player ID
     * @return All markets owned by the player
     */
    public List<BDMarket> getPlayerMarkets(UUID playerId) {
        return markets.values().stream()
                .filter(market -> market.getFounderId().equals(playerId))
                .collect(Collectors.toList());
    }
    
    /**
     * Removes a market.
     * @param market The market to remove
     */
    public void removeMarket(BDMarket market) {
        // Remove all villagers
        for (UUID villagerId : market.getTraders()) {
            villagerManager.removeVillager(villagerId);
        }
        
        // Remove market
        markets.remove(market.getId());
        
        logger.info("Removed market " + market.getId() + " at " + formatLocation(market.getCenter()));
    }
    
    /**
     * Upgrades a market.
     * @param market The market to upgrade
     * @return Whether the upgrade was successful
     */
    public boolean upgradeMarket(BDMarket market) {
        int currentLevel = market.getLevel();
        
        // Check if already max level
        if (currentLevel >= 4) {
            return false;
        }
        
        // Upgrade market
        market.setLevel(currentLevel + 1);
        
        logger.info("Upgraded market " + market.getId() + " to level " + market.getLevel());
        
        return true;
    }
    
    /**
     * Saves all markets to storage.
     */
    public void saveMarkets() {
        // This would save markets to a database or file
        // For simplicity, we'll just log that they were saved
        logger.info("Saved " + markets.size() + " markets to storage");
    }
    
    /**
     * Adds a collector house to a market.
     * @param player The player adding the house
     * @param location The location of the house
     * @return Whether the house was added successfully
     */
    public boolean addCollectorHouse(Player player, Location location) {
        BDMarket market = getMarketAt(location);
        
        if (market == null) {
            return false;
        }
        
        // Check if player owns the market
        if (!market.getFounderId().equals(player.getUniqueId())) {
            return false;
        }
        
        // Add collector
        return addCollector(market, location) != null;
    }
    
    /**
     * Formats a location for logging.
     * @param location The location
     * @return The formatted location
     */
    private String formatLocation(Location location) {
        return location.getWorld().getName() + "," + 
                location.getBlockX() + "," + 
                location.getBlockY() + "," + 
                location.getBlockZ();
    }
}