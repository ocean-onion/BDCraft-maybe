package com.bdcraft.plugin.modules.economy.market;

import com.bdcraft.plugin.BDCraft;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Manages player markets in the BDCraft economy system.
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
     * Gets a player's owned markets.
     * 
     * @param playerId The player's UUID
     * @return The player's markets
     */
    public List<Market> getPlayerMarkets(UUID playerId) {
        List<Market> playerMarkets = new ArrayList<>();
        
        for (Market market : markets.values()) {
            if (market.getOwnerId().equals(playerId)) {
                playerMarkets.add(market);
            }
        }
        
        return playerMarkets;
    }
    
    /**
     * Gets markets where a player is an associate.
     * 
     * @param playerId The player's UUID
     * @return The markets
     */
    public List<Market> getPlayerAssociatedMarkets(UUID playerId) {
        List<Market> associatedMarkets = new ArrayList<>();
        
        for (Market market : markets.values()) {
            if (market.isAssociate(playerId)) {
                associatedMarkets.add(market);
            }
        }
        
        return associatedMarkets;
    }
    
    /**
     * Creates a new market.
     * 
     * @param location The market location
     * @param player The player owner
     * @return The new market, or null if creation failed
     */
    public Market createMarket(Location location, Player player) {
        // Generate a default name based on player's name
        String name = player.getName() + "'s Market";
        return createMarket(player, name, location);
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
        // Check if player can create a market (permissions, etc.)
        if (!canCreateMarket(owner)) {
            return null;
        }
        
        // Check for market area overlap
        if (isOverlappingExistingMarket(center)) {
            return null;
        }
        
        // Create the market
        Market market = new Market(
                UUID.randomUUID(), 
                owner.getUniqueId(), 
                owner.getName(), 
                name, 
                center.getBlockX(), 
                center.getBlockY(), 
                center.getBlockZ(), 
                center.getWorld().getName()
        );
        
        // Add to registry
        markets.put(market.getId(), market);
        
        return market;
    }
    
    /**
     * Removes a market.
     * 
     * @param id The market ID
     * @return True if removed successfully
     */
    public boolean removeMarket(UUID id) {
        return markets.remove(id) != null;
    }
    
    /**
     * Gets a market at a location.
     * 
     * @param location The location
     * @return The market, or null if not found
     */
    public Market getMarketAt(Location location) {
        for (Market market : markets.values()) {
            if (market.isInBounds(location)) {
                return market;
            }
        }
        
        return null;
    }
    
    /**
     * Checks if a player can create a market.
     * 
     * @param player The player
     * @return True if the player can create a market
     */
    private boolean canCreateMarket(Player player) {
        // Check permission
        if (!player.hasPermission("bdcraft.market.create")) {
            return false;
        }
        
        // Check market limit based on rank
        int rank = plugin.getProgressionModule().getRankManager().getPlayerRank(player);
        int maxMarkets = getMaxMarketsByRank(rank);
        
        List<Market> playerMarkets = getPlayerMarkets(player.getUniqueId());
        return playerMarkets.size() < maxMarkets;
    }
    
    /**
     * Gets the maximum number of markets a player can have based on rank.
     * 
     * @param rank The player's rank
     * @return The maximum number of markets
     */
    private int getMaxMarketsByRank(int rank) {
        switch (rank) {
            case 1: // Newcomer
                return 0;
            case 2: // Farmer
                return 1;
            case 3: // Expert Farmer
                return 2;
            case 4: // Master Farmer
                return 3;
            case 5: // Agricultural Expert
                return 5;
            default:
                return 0;
        }
    }
    
    /**
     * Checks if a location overlaps with an existing market.
     * 
     * @param location The location to check
     * @return True if overlapping
     */
    private boolean isOverlappingExistingMarket(Location location) {
        return getMarketAt(location) != null;
    }
    
    /**
     * Checks if a location is within a market.
     * 
     * @param location The location
     * @return True if within a market
     */
    public boolean isInMarket(Location location) {
        return getMarketAt(location) != null;
    }
    
    /**
     * Checks if a player is in their own market at a location.
     * 
     * @param player The player
     * @param location The location
     * @return True if in own market
     */
    public boolean isPlayerInOwnMarket(Player player, Location location) {
        Market market = getMarketAt(location);
        if (market == null) {
            return false;
        }
        
        return market.getOwnerId().equals(player.getUniqueId());
    }
    
    /**
     * Adds a collector house to a market.
     * 
     * @param player The player
     * @param location The location
     * @return True if added successfully
     */
    public boolean addCollectorHouse(Player player, Location location) {
        Market market = getMarketAt(location);
        if (market == null || !market.getOwnerId().equals(player.getUniqueId())) {
            return false;
        }
        
        // Implementation would add a collector house to the market
        // This is a placeholder - actual implementation would add
        // a villager or other collector entity
        return true;
    }
}