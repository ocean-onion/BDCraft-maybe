package com.bdcraft.plugin.modules.economy.market;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.BDEconomyModule;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Manages markets in the game.
 */
public class MarketManager {
    private final BDCraft plugin;
    private final BDEconomyModule economyModule;
    private final Logger logger;
    private final Map<UUID, Market> markets;
    
    // Location cache for fast lookup
    private final Map<String, Market> marketsByChunk;
    
    // Data storage
    private File dataFile;
    private FileConfiguration data;
    
    /**
     * Creates a new market manager.
     * 
     * @param plugin The plugin instance
     * @param economyModule The economy module
     */
    public MarketManager(BDCraft plugin, BDEconomyModule economyModule) {
        this.plugin = plugin;
        this.economyModule = economyModule;
        this.logger = plugin.getLogger();
        this.markets = new HashMap<>();
        this.marketsByChunk = new HashMap<>();
        
        // Load market data
        loadData();
    }
    
    /**
     * Loads market data from file.
     */
    private void loadData() {
        // Clear existing data
        markets.clear();
        marketsByChunk.clear();
        
        // Create data directory if it doesn't exist
        File dataDir = new File(plugin.getDataFolder(), "data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        
        // Create or load data file
        dataFile = new File(dataDir, "markets.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to create markets data file", e);
            }
        }
        
        // Load data
        data = YamlConfiguration.loadConfiguration(dataFile);
        
        // Load markets
        if (data.contains("markets")) {
            for (String key : data.getConfigurationSection("markets").getKeys(false)) {
                String marketData = data.getString("markets." + key);
                Market market = Market.deserialize(marketData);
                
                if (market != null) {
                    markets.put(market.getId(), market);
                    updateMarketLocationCache(market);
                } else {
                    logger.warning("Failed to load market: " + key);
                }
            }
        }
        
        logger.info("Loaded " + markets.size() + " markets.");
    }
    
    /**
     * Saves market data to file.
     */
    private void saveData() {
        if (data == null || dataFile == null) {
            return;
        }
        
        // Save markets
        for (Market market : markets.values()) {
            data.set("markets." + market.getId().toString(), market.serialize());
        }
        
        // Save data file
        try {
            data.save(dataFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save markets data", e);
        }
    }
    
    /**
     * Updates the market location cache for a market.
     *
     * @param market The market
     */
    private void updateMarketLocationCache(Market market) {
        // Get the chunk key for the market center
        String chunkKey = getChunkKey(market.getCenterLocation());
        marketsByChunk.put(chunkKey, market);
        
        // Add surrounding chunks as well (market radius)
        int radius = market.getRadius();
        Location center = market.getCenterLocation();
        
        for (int x = -radius; x <= radius; x += 16) {
            for (int z = -radius; z <= radius; z += 16) {
                Location loc = center.clone().add(x, 0, z);
                String key = getChunkKey(loc);
                marketsByChunk.put(key, market);
            }
        }
    }
    
    /**
     * Gets the chunk key for a location.
     *
     * @param location The location
     * @return The chunk key
     */
    private String getChunkKey(Location location) {
        return location.getWorld().getName() + ":" + 
                location.getBlockX() / 16 + ":" + 
                location.getBlockZ() / 16;
    }
    
    /**
     * Gets all markets.
     * 
     * @return The markets
     */
    public List<Market> getMarkets() {
        return new ArrayList<>(markets.values());
    }
    
    /**
     * Gets all markets (alias for getMarkets).
     *
     * @return A list of all markets
     */
    public List<Market> getAllMarkets() {
        return getMarkets();
    }
    
    /**
     * Gets a market by ID.
     * 
     * @param id The market ID
     * @return The market, or null if not found
     */
    public Market getMarket(UUID id) {
        return markets.get(id);
    }
    
    /**
     * Gets a market at a location.
     * 
     * @param location The location
     * @return The market, or null if not found
     */
    public Market getMarketAt(Location location) {
        // First check the chunk cache for fast lookup
        String chunkKey = getChunkKey(location);
        Market market = marketsByChunk.get(chunkKey);
        
        // If found in cache, verify the location is actually within the market
        if (market != null && market.isInMarket(location)) {
            return market;
        }
        
        // Fallback to checking all markets if not found in cache
        for (Market m : markets.values()) {
            if (m.isInMarket(location)) {
                // Update cache for future lookups
                marketsByChunk.put(chunkKey, m);
                return m;
            }
        }
        
        return null;
    }
    
    /**
     * Creates a new market.
     * 
     * @param owner The owner
     * @param name The market name
     * @param center The center location
     * @return The new market, or null if creation failed
     */
    public Market createMarket(Player owner, String name, Location center) {
        // Check if the location is in another market
        if (getMarketAt(center) != null) {
            return null;
        }
        
        // Check if the player has reached their market limit
        if (!canCreateMarket(owner)) {
            return null;
        }
        
        // Check for overlap with existing markets
        for (Market market : markets.values()) {
            if (market.getWorldName().equals(center.getWorld().getName())) {
                Location marketCenter = new Location(
                        center.getWorld(),
                        market.getCenterX(),
                        market.getCenterY(),
                        market.getCenterZ()
                );
                
                double distance = center.distance(marketCenter);
                
                if (distance < market.getRadius() * 2) {
                    return null; // Too close to another market
                }
            }
        }
        
        // Create the market
        UUID id = UUID.randomUUID();
        Market market = new Market(id, owner, name, center, 32); // Default radius of 32 blocks for level 1
        
        // Add to maps
        markets.put(id, market);
        updateMarketLocationCache(market);
        
        // Save data
        saveData();
        
        logger.info("Created market '" + name + "' at " + center.getWorld().getName() + 
                " (" + center.getBlockX() + ", " + center.getBlockY() + ", " + center.getBlockZ() + ")");
        
        return market;
    }
    
    /**
     * Removes a market.
     * 
     * @param id The market ID
     * @return True if removed successfully
     */
    public boolean removeMarket(UUID id) {
        Market market = markets.remove(id);
        
        if (market != null) {
            // Remove from location cache
            List<String> keysToRemove = new ArrayList<>();
            for (Map.Entry<String, Market> entry : marketsByChunk.entrySet()) {
                if (entry.getValue().getId().equals(id)) {
                    keysToRemove.add(entry.getKey());
                }
            }
            
            for (String key : keysToRemove) {
                marketsByChunk.remove(key);
            }
            
            // Remove from data
            if (data != null) {
                data.set("markets." + id.toString(), null);
                saveData();
            }
            
            logger.info("Removed market '" + market.getName() + "' with ID " + id);
            return true;
        }
        
        return false;
    }
    
    /**
     * Deletes a market.
     *
     * @param market The market to delete
     */
    public void deleteMarket(Market market) {
        removeMarket(market.getId());
    }
    
    /**
     * Gets a player's owned markets.
     * 
     * @param playerId The player's UUID
     * @return The player's markets
     */
    public List<Market> getPlayerMarkets(UUID playerId) {
        return markets.values().stream()
                .filter(market -> market.isOwner(playerId))
                .collect(Collectors.toList());
    }
    
    /**
     * Gets a player's owned markets.
     *
     * @param player The player
     * @return A list of markets owned by the player
     */
    public List<Market> getPlayerMarkets(Player player) {
        return getPlayerMarkets(player.getUniqueId());
    }
    
    /**
     * Gets markets where a player is an associate.
     * 
     * @param playerId The player's UUID
     * @return The markets where the player is an associate
     */
    public List<Market> getPlayerAssociatedMarkets(UUID playerId) {
        return markets.values().stream()
                .filter(market -> market.isAssociate(playerId))
                .collect(Collectors.toList());
    }
    
    /**
     * Gets the maximum number of markets a player can own.
     *
     * @param player The player
     * @return The maximum number of markets
     */
    public int getPlayerMaxMarkets(Player player) {
        if (player.hasPermission("bdcraft.market.create.unlimited")) {
            return Integer.MAX_VALUE;
        } else if (player.hasPermission("bdcraft.market.create.tier3")) {
            return 3;
        } else if (player.hasPermission("bdcraft.market.create.tier2")) {
            return 2;
        } else if (player.hasPermission("bdcraft.market.create.tier1")) {
            return 1;
        } else {
            return 0;
        }
    }
    
    /**
     * Checks if a player can create a market.
     * 
     * @param player The player
     * @return True if the player can create a market
     */
    public boolean canCreateMarket(Player player) {
        int ownedMarkets = getPlayerMarkets(player).size();
        int maxMarkets = getPlayerMaxMarkets(player);
        
        return ownedMarkets < maxMarkets;
    }
    
    /**
     * Checks if a location is within a market.
     * 
     * @param location The location to check
     * @return True if the location is in a market
     */
    public boolean isInMarket(Location location) {
        return getMarketAt(location) != null;
    }
    
    /**
     * Checks if a player is within their own market at a location.
     * 
     * @param player The player
     * @param location The location
     * @return True if in their own market
     */
    public boolean isPlayerInOwnMarket(Player player, Location location) {
        Market market = getMarketAt(location);
        return market != null && market.isOwner(player.getUniqueId());
    }
    
    /**
     * Adds a collector house to a market.
     * 
     * @param player The player placing the house
     * @param location The house location
     * @return True if added successfully
     */
    public boolean addCollectorHouse(Player player, Location location) {
        Market market = getMarketAt(location);
        if (market == null) {
            return false;
        }
        
        // Check if the player has permission to add collectors to this market
        if (!market.isOwner(player.getUniqueId()) && !market.isAssociate(player.getUniqueId())) {
            return false;
        }
        
        // Check if the market can have more collectors
        if (!market.incrementCollectorsCount()) {
            return false;
        }
        
        // Save the updated market data
        saveData();
        
        return true;
    }
    
    /**
     * Removes a collector from a market.
     *
     * @param market The market
     */
    public void removeCollector(Market market) {
        market.decrementCollectorsCount();
        saveData();
    }
    
    /**
     * Reloads market data.
     */
    public void reloadData() {
        loadData();
    }
    
    /**
     * Save market data to file.
     * This public method provides other classes access to save market data.
     */
    public void saveMarketsData() {
        saveData();
    }
}