package com.bdcraft.plugin.modules.economy;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.api.EconomyAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Implementation of the EconomyAPI for BD economy operations.
 * This class provides a comprehensive interface for managing player currency, 
 * including deposits, withdrawals, balance checking, and currency formatting.
 * 
 * The implementation handles both online and offline players properly, with
 * special handling for offline players to ensure consistent economy operations.
 * 
 * Currency operations are validated to prevent negative balances and ensure
 * transactional consistency in the economy system.
 */
public class BDEconomyAPI implements EconomyAPI {
    private final BDCraft plugin;
    private final Logger logger;
    private final BDEconomyModule economyModule;
    private final DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");
    
    // Currency configuration
    private final String currencyName = "BDCoin";
    private final String currencyNamePlural = "BDCoins";
    private final String currencySymbol = "§6BD§r";
    
    /**
     * Creates a new BD economy API.
     * @param plugin The plugin instance
     * @param economyModule The economy module
     */
    public BDEconomyAPI(BDCraft plugin, BDEconomyModule economyModule) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.economyModule = economyModule;
    }
    
    /**
     * Gets the current balance of a player's account.
     * For online players, this returns the active balance from the economy module.
     * For offline players, this retrieves the stored balance from persistent storage.
     *
     * @param uuid The UUID of the player
     * @return The current balance of the player's account
     */
    @Override
    public double getPlayerBalance(UUID uuid) {
        // Use the non-deprecated method to fetch the player
        Player player = Bukkit.getServer().getPlayer(uuid);
        if (player != null && player.isOnline()) {
            return economyModule.getPlayerBalance(player);
        }
        
        // Fallback to stored player data for offline players
        return economyModule.getCurrency(uuid);
    }
    
    /**
     * Sets a player's balance to a specific amount.
     * This method resets the current balance and then adds the specified amount.
     * If the player is offline, the method sets the value in persistent storage.
     *
     * @param uuid The UUID of the player
     * @param amount The new balance amount (must be >= 0)
     */
    public void setBalance(UUID uuid, double amount) {
        if (amount < 0) {
            logger.warning("Attempted to set negative balance for player " + uuid + ": " + amount);
            amount = 0; // Prevent negative balances
        }
        
        // Use the non-deprecated method to fetch the player
        Player player = Bukkit.getServer().getPlayer(uuid);
        if (player != null && player.isOnline()) {
            // Since our economy module doesn't have a direct set method, 
            // we'll reset and then add the new amount
            economyModule.resetPlayerBalance(player);
            if (amount > 0) {
                economyModule.addPlayerBalance(player, (int) amount);
            }
        } else {
            // Handle offline player
            economyModule.setCurrency(uuid, (int) amount);
        }
    }
    
    /**
     * Deposits money into a player's account.
     * This method validates the deposit amount (must be positive) and handles
     * both online and offline players appropriately.
     *
     * @param playerUuid The player UUID
     * @param amount The amount to deposit
     * @return The new balance after the deposit
     */
    @Override
    public double depositPlayer(UUID playerUuid, double amount) {
        // Validate the deposit amount
        if (amount <= 0) {
            logger.fine("Attempted to deposit non-positive amount: " + amount);
            return getPlayerBalance(playerUuid);
        }
        
        // Use the non-deprecated method to fetch the player
        Player player = Bukkit.getServer().getPlayer(playerUuid);
        if (player != null && player.isOnline()) {
            return economyModule.addPlayerBalance(player, (int) amount);
        }
        
        // Deposit to offline player
        return economyModule.addOfflinePlayerCoins(playerUuid, (int) amount);
    }
    
    /**
     * Withdraws money from a player's account.
     * This method validates the withdrawal amount and handles both online
     * and offline players, ensuring the player has sufficient funds.
     * 
     * If the player does not have enough balance, the withdrawal fails
     * and returns -1 without altering the player's balance.
     *
     * @param playerUuid The player UUID
     * @param amount The amount to withdraw
     * @return The new balance after withdrawal, or -1 if the player doesn't have enough funds
     */
    @Override
    public double withdrawPlayer(UUID playerUuid, double amount) {
        // Validate the withdrawal amount
        if (amount <= 0) {
            logger.fine("Attempted to withdraw non-positive amount: " + amount);
            return getPlayerBalance(playerUuid);
        }
        
        // Use the non-deprecated method to fetch the player
        Player player = Bukkit.getServer().getPlayer(playerUuid);
        if (player != null && player.isOnline()) {
            return economyModule.removePlayerBalance(player, (int) amount);
        }
        
        // For offline players, check if they have sufficient funds
        int currentBalance = economyModule.getCurrency(playerUuid);
        if (currentBalance >= amount) {
            int newBalance = currentBalance - (int)amount;
            economyModule.setCurrency(playerUuid, newBalance);
            return newBalance;
        }
        
        // Insufficient funds
        logger.fine("Player " + playerUuid + " has insufficient funds: " + currentBalance + " < " + amount);
        return -1;
    }
    
    /**
     * Checks if a player has enough balance for a specified amount.
     * This method works for both online and offline players.
     *
     * @param uuid The player UUID
     * @param amount The amount to check against the player's balance
     * @return true if the player has enough funds, false otherwise
     */
    @Override
    public boolean hasEnough(UUID uuid, double amount) {
        // Validate the amount
        if (amount <= 0) {
            return true; // Always have enough for zero or negative amounts
        }
        
        // Use the non-deprecated method to fetch the player
        Player player = Bukkit.getServer().getPlayer(uuid);
        if (player != null && player.isOnline()) {
            return economyModule.hasCoins(player, (int) amount);
        }
        
        // For offline players, check stored currency
        int currentBalance = economyModule.getCurrency(uuid);
        return currentBalance >= amount;
    }
    
    /**
     * Formats currency amount.
     *
     * @param amount The amount
     * @return The formatted currency amount
     */
    public String formatCurrency(double amount) {
        return currencySymbol + " " + currencyFormat.format(amount);
    }
    
    /**
     * Gets the currency name.
     *
     * @return The currency name
     */
    public String getCurrencyName() {
        return currencyName;
    }
    
    /**
     * Gets the plural currency name.
     *
     * @return The plural currency name
     */
    public String getCurrencyNamePlural() {
        return currencyNamePlural;
    }
    
    /**
     * Gets the currency symbol.
     *
     * @return The currency symbol
     */
    public String getCurrencySymbol() {
        return currencySymbol;
    }
}