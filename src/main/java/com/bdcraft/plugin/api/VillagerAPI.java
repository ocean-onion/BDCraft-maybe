package com.bdcraft.plugin.api;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

/**
 * API for working with the custom villager system in BDCraft.
 */
public interface VillagerAPI {
    /**
     * Creates a dealer villager at the specified location.
     * Dealer villagers sell BD seeds for emeralds.
     * 
     * @param location The location to spawn the villager
     * @return The created villager entity wrapper
     */
    Object createDealer(Location location);
    
    /**
     * Creates a collector villager at the specified location.
     * Collector villagers buy harvested BD crops for emeralds and BD currency.
     * 
     * @param location The location to spawn the villager
     * @return The created villager entity wrapper
     */
    Object createCollector(Location location);
    
    /**
     * Creates a seasonal trader at the specified location.
     * Seasonal traders appear periodically with unique items.
     * 
     * @param location The location to spawn the villager
     * @param season The season type (e.g., "SUMMER", "WINTER")
     * @return The created villager entity wrapper
     */
    Object createSeasonalTrader(Location location, String season);
    
    /**
     * Creates a market owner at the specified location.
     * Market owners manage player-created markets.
     * 
     * @param location The location to spawn the villager
     * @param marketId The ID of the market this villager is associated with
     * @return The created villager entity wrapper
     */
    Object createMarketOwner(Location location, String marketId);
    
    /**
     * Adds a trade offer to a villager.
     * 
     * @param villager The villager entity wrapper
     * @param inputItem The item the player must provide
     * @param outputItem The item the player will receive
     * @return True if the trade was added successfully
     */
    boolean addTrade(Object villager, ItemStack inputItem, ItemStack outputItem);
    
    /**
     * Adds a trade offer to a villager with a secondary input item.
     * 
     * @param villager The villager entity wrapper
     * @param inputItem1 The first item the player must provide
     * @param inputItem2 The second item the player must provide (can be null)
     * @param outputItem The item the player will receive
     * @return True if the trade was added successfully
     */
    boolean addTrade(Object villager, ItemStack inputItem1, ItemStack inputItem2, ItemStack outputItem);
    
    /**
     * Removes all trades from a villager.
     * 
     * @param villager The villager entity wrapper
     * @return True if the trades were cleared successfully
     */
    boolean clearTrades(Object villager);
    
    /**
     * Gets the type of a custom villager.
     * 
     * @param villager The villager entity wrapper
     * @return The villager type (e.g., "DEALER", "COLLECTOR")
     */
    String getVillagerType(Object villager);
}