package com.example.paperplugin.modules.economy;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Interface for the economy service.
 * Provides methods for other modules to interact with the economy.
 */
public interface EconomyService {
    
    /**
     * Gets a player's balance.
     * 
     * @param player The player
     * @return The player's balance
     */
    double getBalance(Player player);
    
    /**
     * Gets a player's balance by UUID.
     * 
     * @param playerId The player UUID
     * @return The player's balance
     */
    double getBalance(UUID playerId);
    
    /**
     * Adds to a player's balance.
     * 
     * @param player The player
     * @param amount The amount to add
     * @return The new balance
     */
    double addBalance(Player player, double amount);
    
    /**
     * Adds to a player's balance by UUID.
     * 
     * @param playerId The player UUID
     * @param amount The amount to add
     * @return The new balance
     */
    double addBalance(UUID playerId, double amount);
    
    /**
     * Subtracts from a player's balance.
     * 
     * @param player The player
     * @param amount The amount to subtract
     * @return true if the transaction was successful, false if insufficient funds
     */
    boolean subtractBalance(Player player, double amount);
    
    /**
     * Subtracts from a player's balance by UUID.
     * 
     * @param playerId The player UUID
     * @param amount The amount to subtract
     * @return true if the transaction was successful, false if insufficient funds
     */
    boolean subtractBalance(UUID playerId, double amount);
    
    /**
     * Gets the formatted currency name (singular).
     * 
     * @return The formatted currency name
     */
    String getCurrencyName();
    
    /**
     * Gets the formatted currency name (plural).
     * 
     * @return The formatted currency name
     */
    String getCurrencyNamePlural();
    
    /**
     * Gets the currency format.
     * 
     * @param amount The amount to format
     * @return The formatted amount with currency
     */
    String format(double amount);
}