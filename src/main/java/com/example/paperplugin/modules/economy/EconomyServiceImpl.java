package com.example.paperplugin.modules.economy;

import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.UUID;

/**
 * Implementation of the EconomyService interface.
 * Provides the economy service functionality.
 */
public class EconomyServiceImpl implements EconomyService {

    private final EconomyModule economyModule;
    private final DecimalFormat formatter;
    
    // Default currency settings
    private String currencyName = "BD";
    private String currencyNamePlural = "BDs";
    private String currencyFormat = "{amount} {currency}";
    
    /**
     * Creates a new EconomyServiceImpl.
     *
     * @param economyModule The economy module
     */
    public EconomyServiceImpl(EconomyModule economyModule) {
        this.economyModule = economyModule;
        this.formatter = new DecimalFormat("#,##0.00");
        
        // Load currency settings from config
        loadCurrencySettings();
    }
    
    /**
     * Loads currency settings from the economy config.
     */
    private void loadCurrencySettings() {
        // Load from config if available
        if (economyModule.getEconomyConfig().contains("currency.name")) {
            currencyName = economyModule.getEconomyConfig().getString("currency.name", currencyName);
        }
        
        if (economyModule.getEconomyConfig().contains("currency.name-plural")) {
            currencyNamePlural = economyModule.getEconomyConfig().getString("currency.name-plural", currencyNamePlural);
        }
        
        if (economyModule.getEconomyConfig().contains("currency.format")) {
            currencyFormat = economyModule.getEconomyConfig().getString("currency.format", currencyFormat);
        }
    }

    @Override
    public double getBalance(Player player) {
        return getBalance(player.getUniqueId());
    }

    @Override
    public double getBalance(UUID playerId) {
        return economyModule.getBalance(playerId);
    }

    @Override
    public double addBalance(Player player, double amount) {
        return addBalance(player.getUniqueId(), amount);
    }

    @Override
    public double addBalance(UUID playerId, double amount) {
        return economyModule.addBalance(playerId, amount);
    }

    @Override
    public boolean subtractBalance(Player player, double amount) {
        return subtractBalance(player.getUniqueId(), amount);
    }

    @Override
    public boolean subtractBalance(UUID playerId, double amount) {
        return economyModule.subtractBalance(playerId, amount);
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
    public String format(double amount) {
        String formattedAmount = formatter.format(amount);
        String currency = (amount == 1.0) ? currencyName : currencyNamePlural;
        
        return currencyFormat
                .replace("{amount}", formattedAmount)
                .replace("{currency}", currency);
    }
}