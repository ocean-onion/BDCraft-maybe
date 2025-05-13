package com.bdcraft.plugin.modules.economy.modules.market;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages markets in the economy system.
 */
public class MarketManager {
    private final BDCraft plugin;
    private final Logger logger;
    private final Map<UUID, Market> markets = new HashMap<>();
    private final ItemManager itemManager;
    
    /**
     * Creates a new market manager.
     * 
     * @param plugin The plugin instance
     * @param itemManager The item manager
     */
    public MarketManager(BDCraft plugin, ItemManager itemManager) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.itemManager = itemManager;
    }
    
    /**
     * Initializes the market manager and loads markets from configuration.
     */
    public void initialize() {
        loadMarkets();
    }
    
    /**
     * Loads markets from the configuration.
     */
    public void loadMarkets() {
        markets.clear();
        
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection marketsSection = config.getConfigurationSection("markets");
        
        if (marketsSection == null) {
            logger.info("No markets found in configuration.");
            return;
        }
        
        for (String key : marketsSection.getKeys(false)) {
            try {
                ConfigurationSection marketSection = marketsSection.getConfigurationSection(key);
                if (marketSection == null) continue;
                
                UUID id = UUID.fromString(key);
                String name = marketSection.getString("name", "Unknown Market");
                UUID owner = UUID.fromString(marketSection.getString("owner", "00000000-0000-0000-0000-000000000000"));
                
                String worldName = marketSection.getString("world");
                if (worldName == null) {
                    logger.warning("World null for market " + name + ", skipping");
                    continue;
                }
                
                World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    logger.warning("Could not find world " + worldName + " for market " + name + ", skipping");
                    continue;
                }
                
                double x = marketSection.getDouble("x", 0);
                double y = marketSection.getDouble("y", 0);
                double z = marketSection.getDouble("z", 0);
                
                Location center = new Location(world, x, y, z);
                
                Market market = new Market(id, name, owner, center);
                
                // Load market stalls if any
                ConfigurationSection stallsSection = marketSection.getConfigurationSection("stalls");
                if (stallsSection != null) {
                    for (String stallKey : stallsSection.getKeys(false)) {
                        // Implementation for stalls would go here
                    }
                }
                
                markets.put(id, market);
                logger.info("Loaded market: " + name);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error loading market: " + key, e);
            }
        }
        
        logger.info("Loaded " + markets.size() + " markets");
    }
    
    /**
     * Saves markets to the configuration.
     */
    public void saveMarkets() {
        FileConfiguration config = plugin.getConfig();
        
        // Clear existing markets
        config.set("markets", null);
        
        // Create market section
        for (Market market : markets.values()) {
            String path = "markets." + market.getId().toString();
            config.set(path + ".name", market.getName());
            config.set(path + ".owner", market.getOwner().toString());
            
            Location center = market.getCenter();
            config.set(path + ".world", center.getWorld().getName());
            config.set(path + ".x", center.getX());
            config.set(path + ".y", center.getY());
            config.set(path + ".z", center.getZ());
            
            // Save stalls if needed
        }
        
        plugin.saveConfig();
        logger.info("Saved " + markets.size() + " markets");
    }
    
    /**
     * Creates a new market.
     * 
     * @param owner The player who will own the market
     * @param name The name of the market
     * @param center The center location of the market
     * @return The newly created market
     */
    public Market createMarket(Player owner, String name, Location center) {
        UUID marketId = UUID.randomUUID();
        Market market = new Market(marketId, name, owner.getUniqueId(), center);
        markets.put(marketId, market);
        
        // Save markets to config after creation
        saveMarkets();
        
        return market;
    }
    
    /**
     * Deletes a market by ID.
     * 
     * @param marketId The ID of the market to delete
     * @return True if the market was deleted, false if it couldn't be found
     */
    public boolean deleteMarket(UUID marketId) {
        if (!markets.containsKey(marketId)) {
            return false;
        }
        
        markets.remove(marketId);
        saveMarkets();
        return true;
    }
    
    /**
     * Gets a market by ID.
     * 
     * @param marketId The ID of the market to retrieve
     * @return The market, or null if not found
     */
    public Market getMarket(UUID marketId) {
        return markets.get(marketId);
    }
    
    /**
     * Gets all registered markets.
     * 
     * @return A list of all markets
     */
    public List<Market> getMarkets() {
        return new ArrayList<>(markets.values());
    }
    
    /**
     * Gets a market at a specific location.
     * 
     * @param location The location to check
     * @return The market at this location, or null if none found
     */
    public Market getMarketAt(Location location) {
        for (Market market : markets.values()) {
            // Check if the location is within the market radius (30 blocks)
            if (location.getWorld().equals(market.getCenter().getWorld()) &&
                    location.distance(market.getCenter()) <= 30) {
                return market;
            }
        }
        return null;
    }
    
    /**
     * Gets a market by name (case-insensitive).
     * 
     * @param name The name to search for
     * @return The market, or null if not found
     */
    public Market getMarketByName(String name) {
        for (Market market : markets.values()) {
            if (market.getName().equalsIgnoreCase(name)) {
                return market;
            }
        }
        return null;
    }
}