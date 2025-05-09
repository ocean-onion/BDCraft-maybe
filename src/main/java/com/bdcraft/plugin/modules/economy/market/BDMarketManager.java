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
import java.util.stream.Collectors;

/**
 * Manages all markets in the BDCraft economy system.
 */
public class BDMarketManager {
    private final BDCraft plugin;
    private final BDEconomyModule economyModule;
    private File dataFile;
    private FileConfiguration data;
    
    // All markets
    private final Map<UUID, BDMarket> markets;
    
    // Location cache for fast lookup
    private final Map<String, BDMarket> marketsByChunk;
    
    /**
     * Creates a new market manager.
     *
     * @param plugin The plugin instance
     * @param economyModule The economy module
     */
    public BDMarketManager(BDCraft plugin, BDEconomyModule economyModule) {
        this.plugin = plugin;
        this.economyModule = economyModule;
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
                plugin.getLogger().log(Level.SEVERE, "Failed to create markets data file", e);
            }
        }
        
        // Load data
        data = YamlConfiguration.loadConfiguration(dataFile);
        
        // Load markets
        if (data.contains("markets")) {
            for (String key : data.getConfigurationSection("markets").getKeys(false)) {
                String marketData = data.getString("markets." + key);
                BDMarket market = BDMarket.deserialize(marketData);
                
                if (market != null) {
                    markets.put(market.getId(), market);
                    updateMarketLocationCache(market);
                } else {
                    plugin.getLogger().warning("Failed to load market: " + key);
                }
            }
        }
        
        plugin.getLogger().info("Loaded " + markets.size() + " markets.");
    }
    
    /**
     * Saves market data to file.
     */
    private void saveData() {
        if (data == null || dataFile == null) {
            return;
        }
        
        // Save markets
        for (BDMarket market : markets.values()) {
            data.set("markets." + market.getId().toString(), market.serialize());
        }
        
        // Save data file
        try {
            data.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save markets data", e);
        }
    }
    
    /**
     * Updates the market location cache for a market.
     *
     * @param market The market
     */
    private void updateMarketLocationCache(BDMarket market) {
        // Get the chunk key for the market center
        String chunkKey = getChunkKey(market.getCenterLocation());
        marketsByChunk.put(chunkKey, market);
        
        // Add surrounding chunks as well (market radius)
        int radius = getMarketRadius(market.getLevel());
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
     * Gets the market radius based on level.
     *
     * @param level The market level
     * @return The market radius in blocks
     */
    private int getMarketRadius(int level) {
        switch (level) {
            case 1:
                return 32;
            case 2:
                return 48;
            case 3:
                return 64;
            case 4:
                return 80;
            default:
                return 32;
        }
    }
    
    /**
     * Creates a new market.
     *
     * @param name The market name
     * @param owner The market owner
     * @param location The market center location
     * @return The created market
     */
    public BDMarket createMarket(String name, Player owner, Location location) {
        // Check if the location is in another market
        if (getMarketAt(location) != null) {
            return null;
        }
        
        // Check if the player has reached their market limit
        int ownedMarkets = getPlayerMarkets(owner).size();
        int maxMarkets = getPlayerMaxMarkets(owner);
        
        if (ownedMarkets >= maxMarkets) {
            return null;
        }
        
        // Create the market
        UUID marketId = UUID.randomUUID();
        BDMarket market = new BDMarket(marketId, name, owner.getUniqueId(), location);
        
        // Add to maps
        markets.put(marketId, market);
        updateMarketLocationCache(market);
        
        // Save data
        saveData();
        
        return market;
    }
    
    /**
     * Gets a market by ID.
     *
     * @param id The market ID
     * @return The market, or null if not found
     */
    public BDMarket getMarket(UUID id) {
        return markets.get(id);
    }
    
    /**
     * Gets the market at a specific location.
     *
     * @param location The location
     * @return The market at this location, or null if none exists
     */
    public BDMarket getMarketAt(Location location) {
        String chunkKey = getChunkKey(location);
        return marketsByChunk.get(chunkKey);
    }
    
    /**
     * Gets all markets owned by a player.
     *
     * @param player The player
     * @return A list of markets owned by the player
     */
    public List<BDMarket> getPlayerMarkets(Player player) {
        return markets.values().stream()
                .filter(market -> market.getOwnerUuid().equals(player.getUniqueId()))
                .collect(Collectors.toList());
    }
    
    /**
     * Gets the maximum number of markets a player can own.
     *
     * @param player The player
     * @return The maximum number of markets
     */
    public int getPlayerMaxMarkets(Player player) {
        if (player.hasPermission("bdcraft.market.create.tier3")) {
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
     * Gets all markets.
     *
     * @return A list of all markets
     */
    public List<BDMarket> getAllMarkets() {
        return new ArrayList<>(markets.values());
    }
    
    /**
     * Gets all markets (alias for getAllMarkets).
     *
     * @return A list of all markets
     */
    public List<BDMarket> getMarkets() {
        return getAllMarkets();
    }
    
    /**
     * Deletes a market.
     *
     * @param market The market to delete
     */
    public void deleteMarket(BDMarket market) {
        // Remove from maps
        markets.remove(market.getId());
        
        // Remove from location cache
        List<String> keysToRemove = new ArrayList<>();
        for (Map.Entry<String, BDMarket> entry : marketsByChunk.entrySet()) {
            if (entry.getValue().getId().equals(market.getId())) {
                keysToRemove.add(entry.getKey());
            }
        }
        
        for (String key : keysToRemove) {
            marketsByChunk.remove(key);
        }
        
        // Remove from data
        if (data != null) {
            data.set("markets." + market.getId().toString(), null);
            saveData();
        }
    }
    
    /**
     * Reloads market data.
     */
    public void reloadData() {
        loadData();
    }
}