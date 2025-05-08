package com.bdcraft.plugin.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * API for market operations.
 */
public interface MarketAPI {
    /**
     * Creates a new market.
     * @param location The central location of the market
     * @param owner The owner of the market
     * @return The market ID if successful, null otherwise
     */
    String createMarket(Location location, Player owner);
    
    /**
     * Removes a market.
     * @param marketId The market ID
     * @return Whether the market was successfully removed
     */
    boolean removeMarket(String marketId);
    
    /**
     * Checks if a location is within a market.
     * @param location The location to check
     * @return The market ID if within a market, null otherwise
     */
    String getMarketAt(Location location);
    
    /**
     * Gets the owner of a market.
     * @param marketId The market ID
     * @return The UUID of the market owner
     */
    UUID getMarketOwner(String marketId);
    
    /**
     * Sets the owner of a market.
     * @param marketId The market ID
     * @param owner The new owner's UUID
     * @return Whether the owner was successfully changed
     */
    boolean setMarketOwner(String marketId, UUID owner);
    
    /**
     * Gets the associates of a market.
     * @param marketId The market ID
     * @return A list of associate UUIDs
     */
    List<UUID> getMarketAssociates(String marketId);
    
    /**
     * Adds an associate to a market.
     * @param marketId The market ID
     * @param associate The associate's UUID
     * @return Whether the associate was successfully added
     */
    boolean addMarketAssociate(String marketId, UUID associate);
    
    /**
     * Removes an associate from a market.
     * @param marketId The market ID
     * @param associate The associate's UUID
     * @return Whether the associate was successfully removed
     */
    boolean removeMarketAssociate(String marketId, UUID associate);
    
    /**
     * Gets the level of a market.
     * @param marketId The market ID
     * @return The market level (1-4)
     */
    int getMarketLevel(String marketId);
    
    /**
     * Upgrades a market to the next level.
     * @param marketId The market ID
     * @param player The player performing the upgrade
     * @return Whether the upgrade was successful
     */
    boolean upgradeMarket(String marketId, Player player);
    
    /**
     * Displays the market boundaries to a player.
     * @param marketId The market ID
     * @param player The player to show the boundaries to
     */
    void showMarketBoundaries(String marketId, Player player);
    
    /**
     * Gets the central location of a market.
     * @param marketId The market ID
     * @return The central location
     */
    Location getMarketCenter(String marketId);
    
    /**
     * Gets the radius of a market based on its level.
     * @param marketId The market ID
     * @return The market radius in blocks
     */
    int getMarketRadius(String marketId);
    
    /**
     * Checks if a player is the owner or an associate of a market.
     * @param marketId The market ID
     * @param player The player to check
     * @return Whether the player is an owner or associate
     */
    boolean hasMarketPermission(String marketId, Player player);
    
    /**
     * Gets the collector count in a market.
     * @param marketId The market ID
     * @return The number of collectors
     */
    int getCollectorCount(String marketId);
    
    /**
     * Gets the maximum collector count for a market based on level.
     * @param marketId The market ID
     * @return The maximum number of collectors
     */
    int getMaxCollectorCount(String marketId);
}