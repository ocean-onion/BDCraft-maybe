package com.bdcraft.plugin.modules.economy.market;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Manages markets in the game.
 */
public class MarketManager {
    private final BDCraft plugin;
    private final Logger logger;
    private final Map<UUID, Market> markets;
    
    /**
     * Creates a new market manager.
     * 
     * @param plugin The plugin instance
     */
    public MarketManager(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.markets = new HashMap<>();
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
        for (Market market : markets.values()) {
            if (market.isInMarket(location)) {
                return market;
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
        int radius = 24; // Default radius of 24 blocks
        Market market = new Market(id, owner, name, center, radius);
        
        // Add to markets
        markets.put(id, market);
        
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
            logger.info("Removed market '" + market.getName() + "' with ID " + id);
            return true;
        }
        
        return false;
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
     * Checks if a player can create a market.
     * 
     * @param player The player
     * @return True if the player can create a market
     */
    public boolean canCreateMarket(Player player) {
        if (player.hasPermission("bdcraft.market.create.unlimited")) {
            return true;
        }
        
        int maxMarkets = 1;
        
        if (player.hasPermission("bdcraft.market.create.tier1")) {
            maxMarkets = 1;
        } else if (player.hasPermission("bdcraft.market.create.tier2")) {
            maxMarkets = 2;
        } else if (player.hasPermission("bdcraft.market.create.tier3")) {
            maxMarkets = 3;
        }
        
        return getPlayerMarkets(player.getUniqueId()).size() < maxMarkets;
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
        
        // Implementation would normally add a collector house
        // This basic implementation just returns true
        // Actual implementation would need to spawn a villager, etc.
        return true;
    }
}