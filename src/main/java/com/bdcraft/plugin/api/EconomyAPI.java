package com.bdcraft.plugin.api;

import java.util.UUID;

/**
 * API for economy operations.
 * This implementation overrides and blocks other economy plugins such as Vault.
 */
public interface EconomyAPI {
    /**
     * Gets a player's balance.
     * @param uuid The player's UUID
     * @return The player's balance
     */
    double getBalance(UUID uuid);
    
    /**
     * Sets a player's balance.
     * @param uuid The player's UUID
     * @param amount The new balance
     */
    void setBalance(UUID uuid, double amount);
    
    /**
     * Adds money to a player's balance.
     * @param uuid The player's UUID
     * @param amount The amount to add
     * @return Whether the deposit was successful
     */
    boolean depositMoney(UUID uuid, double amount);
    
    /**
     * Removes money from a player's balance.
     * @param uuid The player's UUID
     * @param amount The amount to remove
     * @return Whether the withdrawal was successful
     */
    boolean withdrawMoney(UUID uuid, double amount);
    
    /**
     * Checks if a player has enough money.
     * @param uuid The player's UUID
     * @param amount The amount to check
     * @return Whether the player has enough money
     */
    boolean hasEnough(UUID uuid, double amount);
    
    /**
     * Formats an amount of currency.
     * @param amount The amount
     * @return The formatted currency string
     */
    String formatCurrency(double amount);
    
    /**
     * Gets the currency name.
     * @return The currency name
     */
    String getCurrencyName();
    
    /**
     * Gets the currency name (plural form).
     * @return The currency name (plural)
     */
    String getCurrencyNamePlural();
    
    /**
     * Gets the currency symbol.
     * @return The currency symbol
     */
    String getCurrencySymbol();
}