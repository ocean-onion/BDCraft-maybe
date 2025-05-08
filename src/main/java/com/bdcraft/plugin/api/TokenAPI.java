package com.bdcraft.plugin.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * API for BD tokens operations.
 */
public interface TokenAPI {
    /**
     * Gets the token type of an item.
     * @param item The item to check
     * @return The token type, or null if not a token
     */
    String getTokenType(ItemStack item);
    
    /**
     * Checks if an item is a valid BD token.
     * @param item The item to check
     * @return Whether the item is a BD token
     */
    boolean isBDToken(ItemStack item);
    
    /**
     * Creates a market token item.
     * @param amount The amount to create
     * @return The market token item
     */
    ItemStack createMarketToken(int amount);
    
    /**
     * Creates a house token item.
     * @param amount The amount to create
     * @return The house token item
     */
    ItemStack createHouseToken(int amount);
    
    /**
     * Creates a trade token item.
     * @param amount The amount to create
     * @return The trade token item
     */
    ItemStack createTradeToken(int amount);
    
    /**
     * Uses a market token to create a market.
     * @param player The player using the token
     * @param location The location to place the market
     * @return Whether the token was used successfully
     */
    boolean useMarketToken(Player player, Location location);
    
    /**
     * Uses a house token to create a collector house.
     * @param player The player using the token
     * @param location The location to place the collector
     * @param marketId The market ID
     * @return Whether the token was used successfully
     */
    boolean useHouseToken(Player player, Location location, String marketId);
    
    /**
     * Uses a trade token to unlock special trades.
     * @param player The player using the token
     * @param location The location to apply the token
     * @return Whether the token was used successfully
     */
    boolean useTradeToken(Player player, Location location);
}