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
    
    @Override
    public double getBalance(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            return economyModule.getPlayerBalance(player);
        }
        
        // Would need to implement offline player balance checks
        return 0;
    }
    
    @Override
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
    
    @Override
    public boolean depositMoney(UUID uuid, double amount) {
        if (amount <= 0) {
            return false;
        }
        
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            economyModule.addPlayerBalance(player, (int) amount);
            return true;
        }
        
        // Deposit to offline player
        economyModule.addOfflinePlayerCoins(uuid, (int) amount);
        return true;
    }
    
    @Override
    public boolean withdrawMoney(UUID uuid, double amount) {
        if (amount <= 0) {
            return false;
        }
        
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            int result = economyModule.removePlayerBalance(player, (int) amount);
            return result >= 0;
        }
        
        // For offline players, we'd need to implement a separate system
        return false;
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
    
    @Override
    public String formatCurrency(double amount) {
        return currencySymbol + " " + currencyFormat.format(amount);
    }
    
    @Override
    public String getCurrencyName() {
        return currencyName;
    }
    
    @Override
    public String getCurrencyNamePlural() {
        return currencyNamePlural;
    }
    
    @Override
    public String getCurrencySymbol() {
        return currencySymbol;
    }
}