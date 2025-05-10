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
     * Implements EconomyAPI.getPlayerBalance
     */
    @Override
    public double getPlayerBalance(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            return economyModule.getPlayerBalance(player);
        }
        
        // Fallback to stored player data
        return economyModule.getCurrency(uuid);
    }
    
    /**
     * Sets a player's balance.
     *
     * @param uuid The player UUID
     * @param amount The amount
     */
    public void setBalance(UUID uuid, double amount) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            // Since our economy module doesn't have a direct set method, 
            // we'll reset and then add the new amount
            economyModule.resetPlayerBalance(player);
            if (amount > 0) {
                economyModule.addPlayerBalance(player, (int) amount);
            }
        }
    }
    
    /**
     * Deposits money into a player's account.
     *
     * @param playerUuid The player UUID
     * @param amount The amount
     * @return The new balance
     */
    @Override
    public double depositPlayer(UUID playerUuid, double amount) {
        if (amount <= 0) {
            return getPlayerBalance(playerUuid);
        }
        
        Player player = Bukkit.getPlayer(playerUuid);
        if (player != null) {
            return economyModule.addPlayerBalance(player, (int) amount);
        }
        
        // Deposit to offline player
        return economyModule.addOfflinePlayerCoins(playerUuid, (int) amount);
    }
    
    /**
     * Withdraws money from a player's account.
     *
     * @param playerUuid The player UUID
     * @param amount The amount
     * @return The new balance, or -1 if the player doesn't have enough money
     */
    @Override
    public double withdrawPlayer(UUID playerUuid, double amount) {
        if (amount <= 0) {
            return getPlayerBalance(playerUuid);
        }
        
        Player player = Bukkit.getPlayer(playerUuid);
        if (player != null) {
            return economyModule.removePlayerBalance(player, (int) amount);
        }
        
        // For offline players
        int currentBalance = economyModule.getCurrency(playerUuid);
        if (currentBalance >= amount) {
            int newBalance = currentBalance - (int)amount;
            economyModule.setCurrency(playerUuid, newBalance);
            return newBalance;
        }
        return -1;
    }
    
    @Override
    public boolean hasEnough(UUID uuid, double amount) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            return economyModule.hasCoins(player, (int) amount);
        }
        
        // For offline players, we'd need to implement a separate system
        return false;
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