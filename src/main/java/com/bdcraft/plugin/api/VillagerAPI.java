package com.bdcraft.plugin.api;

import org.bukkit.Location;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * API for interacting with the BDCraft villager system.
 */
public interface VillagerAPI {
    /**
     * Creates a BD Dealer villager at the specified location.
     * @param location The location to spawn the villager
     * @param marketId The market ID, or null if it's a natural dealer
     * @return The spawned villager
     */
    Villager createDealer(Location location, String marketId);
    
    /**
     * Creates a BD Collector villager at the specified location.
     * @param location The location to spawn the villager
     * @param marketId The market ID
     * @return The spawned villager
     */
    Villager createCollector(Location location, String marketId);
    
    /**
     * Creates a Market Owner villager at the specified location.
     * @param location The location to spawn the villager
     * @param marketId The market ID
     * @return The spawned villager
     */
    Villager createMarketOwner(Location location, String marketId);
    
    /**
     * Creates a Seasonal BD Trader villager at the specified location.
     * @param location The location to spawn the villager
     * @param marketId The market ID, or null if it's not in a market
     * @return The spawned villager
     */
    Villager createSeasonalTrader(Location location, String marketId);
    
    /**
     * Checks if a villager is a BD villager.
     * @param villager The villager to check
     * @return True if it's a BD villager
     */
    boolean isBDVillager(Villager villager);
    
    /**
     * Gets the type of BD villager.
     * @param villager The villager to check
     * @return The BD villager type, or null if it's not a BD villager
     */
    String getBDVillagerType(Villager villager);
    
    /**
     * Gets a player's reputation with a specific villager.
     * @param player The player
     * @param villager The villager
     * @return The reputation value
     */
    int getReputation(Player player, Villager villager);
    
    /**
     * Sets a player's reputation with a specific villager.
     * @param player The player
     * @param villager The villager
     * @param reputation The reputation value
     */
    void setReputation(Player player, Villager villager, int reputation);
    
    /**
     * Changes a player's reputation with a specific villager.
     * @param player The player
     * @param villager The villager
     * @param change The amount to change the reputation by
     * @return The new reputation value
     */
    int changeReputation(Player player, Villager villager, int change);
    
    /**
     * Gets a player's average reputation in a market.
     * @param player The player
     * @param marketId The market ID
     * @return The average reputation value
     */
    int getMarketReputation(Player player, String marketId);
    
    /**
     * Registers a villager with a market.
     * @param villager The villager
     * @param marketId The market ID
     * @param type The BD villager type
     * @return True if the registration was successful
     */
    boolean registerVillager(Villager villager, String marketId, String type);
    
    /**
     * Unregisters a villager from a market.
     * @param villager The villager
     * @return True if the unregistration was successful
     */
    boolean unregisterVillager(Villager villager);
    
    /**
     * Gets the market ID for a BD villager.
     * @param villager The villager
     * @return The market ID, or null if not in a market
     */
    String getMarketId(Villager villager);
}