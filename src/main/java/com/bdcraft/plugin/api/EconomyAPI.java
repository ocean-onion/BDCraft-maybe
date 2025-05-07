package com.bdcraft.plugin.api;

import org.bukkit.entity.Player;

/**
 * API for interacting with the BDCraft economy system.
 */
public interface EconomyAPI {
    /**
     * Gets a player's BD currency balance.
     * @param player The player
     * @return The player's balance
     */
    double getBalance(Player player);
    
    /**
     * Checks if a player has at least the specified amount of BD currency.
     * @param player The player
     * @param amount The amount to check
     * @return True if the player has at least the specified amount
     */
    boolean has(Player player, double amount);
    
    /**
     * Deposits BD currency to a player's account.
     * @param player The player
     * @param amount The amount to deposit
     * @return True if the transaction was successful
     */
    boolean depositCurrency(Player player, double amount);
    
    /**
     * Withdraws BD currency from a player's account.
     * @param player The player
     * @param amount The amount to withdraw
     * @return True if the transaction was successful
     */
    boolean withdrawCurrency(Player player, double amount);
    
    /**
     * Transfers BD currency from one player to another.
     * @param from The player to transfer from
     * @param to The player to transfer to
     * @param amount The amount to transfer
     * @return True if the transaction was successful
     */
    boolean transferCurrency(Player from, Player to, double amount);
    
    /**
     * Gets the formatted currency name (singular).
     * @return The formatted currency name
     */
    String getCurrencyName();
    
    /**
     * Gets the formatted currency name (plural).
     * @return The formatted currency name
     */
    String getCurrencyNamePlural();
    
    /**
     * Formats a currency amount as a string.
     * @param amount The amount to format
     * @return The formatted amount
     */
    String formatCurrency(double amount);
}