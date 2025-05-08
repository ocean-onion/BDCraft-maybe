package com.bdcraft.plugin.modules.economy.market;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Manages BD markets in the game.
 */
public class BDMarketManager {
    private final BDCraft plugin;
    private final Logger logger;
    private final MarketManager marketManager;
    private final Map<UUID, BDMarket> bdMarkets;
    
    /**
     * Creates a new BD market manager.
     * 
     * @param plugin The plugin instance
     * @param marketManager The market manager
     */
    public BDMarketManager(BDCraft plugin, MarketManager marketManager) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.marketManager = marketManager;
        this.bdMarkets = new HashMap<>();
    }
    
    /**
     * Gets all BD markets.
     * 
     * @return The BD markets
     */
    public List<BDMarket> getMarkets() {
        return new ArrayList<>(bdMarkets.values());
    }
    
    /**
     * Gets a BD market by ID.
     * 
     * @param id The market ID
     * @return The BD market, or null if not found
     */
    public BDMarket getMarket(UUID id) {
        BDMarket bdMarket = bdMarkets.get(id);
        if (bdMarket != null) {
            return bdMarket;
        }
        
        // Check if we have a Market from the original manager
        Market market = marketManager.getMarket(id);
        if (market != null) {
            // Convert to BDMarket and cache it
            bdMarket = convertToBDMarket(market);
            bdMarkets.put(id, bdMarket);
            return bdMarket;
        }
        
        return null;
    }
    
    /**
     * Gets a BD market at a location.
     * 
     * @param location The location
     * @return The BD market, or null if not found
     */
    public BDMarket getMarketAt(Location location) {
        Market market = marketManager.getMarketAt(location);
        if (market != null) {
            return getMarket(market.getId());
        }
        return null;
    }
    
    /**
     * Creates a new BD market.
     * 
     * @param owner The owner
     * @param name The market name
     * @param center The center location
     * @return The new BD market, or null if creation failed
     */
    public BDMarket createMarket(Player owner, String name, Location center) {
        Market market = marketManager.createMarket(owner, name, center);
        if (market != null) {
            BDMarket bdMarket = convertToBDMarket(market);
            bdMarkets.put(bdMarket.getId(), bdMarket);
            return bdMarket;
        }
        return null;
    }
    
    /**
     * Removes a BD market.
     * 
     * @param id The market ID
     * @return True if removed successfully
     */
    public boolean removeMarket(UUID id) {
        bdMarkets.remove(id);
        return marketManager.removeMarket(id);
    }
    
    /**
     * Gets a player's owned BD markets.
     * 
     * @param playerId The player's UUID
     * @return The player's BD markets
     */
    public List<BDMarket> getPlayerMarkets(UUID playerId) {
        List<BDMarket> playerBDMarkets = new ArrayList<>();
        List<Market> playerMarkets = marketManager.getPlayerMarkets(playerId);
        
        for (Market market : playerMarkets) {
            playerBDMarkets.add(getMarket(market.getId()));
        }
        
        return playerBDMarkets;
    }
    
    /**
     * Gets BD markets where a player is a member.
     * 
     * @param playerId The player's UUID
     * @return The BD markets
     */
    public List<BDMarket> getPlayerAssociatedMarkets(UUID playerId) {
        List<BDMarket> associatedBDMarkets = new ArrayList<>();
        List<Market> associatedMarkets = marketManager.getPlayerAssociatedMarkets(playerId);
        
        for (Market market : associatedMarkets) {
            associatedBDMarkets.add(getMarket(market.getId()));
        }
        
        return associatedBDMarkets;
    }
    
    /**
     * Gets all market worlds.
     * 
     * @return List of world names with markets
     */
    public List<String> getMarketWorlds() {
        Set<String> worldNames = new HashSet<>();
        
        for (BDMarket market : getMarkets()) {
            worldNames.add(market.getWorldName());
        }
        
        return new ArrayList<>(worldNames);
    }
    
    /**
     * Gets markets in a specific world.
     * 
     * @param worldName The world name
     * @return List of markets in that world
     */
    public List<BDMarket> getMarketsInWorld(String worldName) {
        List<BDMarket> worldMarkets = new ArrayList<>();
        
        for (BDMarket market : getMarkets()) {
            if (market.getWorldName().equals(worldName)) {
                worldMarkets.add(market);
            }
        }
        
        return worldMarkets;
    }
    
    /**
     * Converts a Market to a BDMarket.
     * 
     * @param market The Market to convert
     * @return The BDMarket
     */
    private BDMarket convertToBDMarket(Market market) {
        BDMarket bdMarket = new BDMarket(
            market.getId(),
            market.getName(),
            market.getOwnerId(),
            market.getOwnerName(),
            market.getWorldName(),
            market.getCenterX(),
            market.getCenterY(),
            market.getCenterZ(),
            market.getLevel(),
            market.getRadius()
        );
        
        // Convert any additional properties as needed
        
        return bdMarket;
    }
}