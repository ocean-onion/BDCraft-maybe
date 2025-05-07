package com.bdcraft.plugin.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

/**
 * API for BD villager operations.
 */
public interface VillagerAPI {
    /**
     * Creates a BD dealer villager.
     * @param location The location
     * @param marketId The market ID
     * @return The created villager
     */
    Villager createDealer(Location location, String marketId);
    
    /**
     * Creates a BD collector villager.
     * @param location The location
     * @param marketId The market ID
     * @return The created villager
     */
    Villager createCollector(Location location, String marketId);
    
    /**
     * Creates a BD market owner villager.
     * @param location The location
     * @param marketId The market ID
     * @return The created villager
     */
    Villager createMarketOwner(Location location, String marketId);
    
    /**
     * Creates a BD seasonal trader villager.
     * @param location The location
     * @param marketId The market ID
     * @return The created villager
     */
    Villager createSeasonalTrader(Location location, String marketId);
    
    /**
     * Checks if a villager is a BD villager.
     * @param villager The villager
     * @return Whether the villager is a BD villager
     */
    boolean isBDVillager(Villager villager);
    
    /**
     * Gets the BD villager type.
     * @param villager The villager
     * @return The BD villager type, or null if not a BD villager
     */
    String getBDVillagerType(Villager villager);
    
    /**
     * Gets a player's reputation with a villager.
     * @param player The player
     * @param villager The villager
     * @return The reputation value
     */
    int getReputation(Player player, Villager villager);
    
    /**
     * Sets a player's reputation with a villager.
     * @param player The player
     * @param villager The villager
     * @param reputation The new reputation value
     */
    void setReputation(Player player, Villager villager, int reputation);
    
    /**
     * Changes a player's reputation with a villager.
     * @param player The player
     * @param villager The villager
     * @param change The reputation change (positive or negative)
     * @return The new reputation value
     */
    int changeReputation(Player player, Villager villager, int change);
    
    /**
     * Gets a player's reputation in a market.
     * @param player The player
     * @param marketId The market ID
     * @return The average reputation across all market villagers
     */
    int getMarketReputation(Player player, String marketId);
    
    /**
     * Registers an existing villager as a BD villager.
     * @param villager The villager
     * @param marketId The market ID
     * @param type The BD villager type
     * @return Whether the registration was successful
     */
    boolean registerVillager(Villager villager, String marketId, String type);
    
    /**
     * Unregisters a BD villager.
     * @param villager The villager
     * @return Whether the unregistration was successful
     */
    boolean unregisterVillager(Villager villager);
    
    /**
     * Gets the market ID of a villager.
     * @param villager The villager
     * @return The market ID, or null if not in a market
     */
    String getMarketId(Villager villager);
}