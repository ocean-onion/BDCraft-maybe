package com.bdcraft.plugin.modules.economy.villager;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.villagers.SeasonalBDTraderVillager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Villager;

import java.util.UUID;

/**
 * Compatibility bridge class to handle transition from old BD villager system to new one.
 * This allows existing code to continue working while we migrate to the new architecture.
 */
public class BDSeasonalTrader extends BDVillager {
    public static final String TYPE = "SEASONAL_TRADER";
    private SeasonalBDTraderVillager delegate;
    
    /**
     * Creates a new BD seasonal trader.
     * @param plugin The plugin instance
     * @param location The spawn location
     */
    public BDSeasonalTrader(BDCraft plugin, Location location) {
        super(plugin, location, "Seasonal BD Trader", Villager.Profession.NITWIT, Villager.Type.PLAINS);
        this.delegate = new SeasonalBDTraderVillager(plugin, villager.getUniqueId(), villager);
    }
    
    /**
     * Creates a new BD seasonal trader from an existing villager.
     * @param plugin The plugin instance
     * @param villager The villager
     */
    public BDSeasonalTrader(BDCraft plugin, Villager villager) {
        super(plugin, villager);
        this.delegate = new SeasonalBDTraderVillager(plugin, villager.getUniqueId(), villager);
    }
    
    /**
     * Gets the current season.
     * @return The current season name as string
     */
    public String getCurrentSeason() {
        if (delegate != null) {
            return delegate.getCurrentSeason().name();
        }
        return "SPRING";
    }
    
    /**
     * Sets the current season by updating trades.
     * @param season The season
     */
    public void setCurrentSeason(String season) {
        // Updating the trades is now handled by the delegate
        if (delegate != null) {
            delegate.setupTrades();
        }
    }
    
    @Override
    protected void initializeTrades() {
        // Trades are now handled by the delegate
        if (delegate != null) {
            delegate.setupTrades();
        }
    }
    
    @Override
    public String getVillagerType() {
        return TYPE;
    }
    
    /**
     * Gets the name color for this villager type.
     * 
     * @return The name color
     */
    @Override
    protected ChatColor getNameColor() {
        return ChatColor.LIGHT_PURPLE;
    }
}