package com.bdcraft.plugin.modules.economy;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.config.ConfigType;
import com.bdcraft.plugin.modules.ModuleManager;
import com.bdcraft.plugin.modules.SubmoduleBase;
import com.bdcraft.plugin.modules.economy.modules.auction.BDAuctionModule;
import com.bdcraft.plugin.modules.economy.modules.market.BDMarketModule;
import com.bdcraft.plugin.modules.economy.modules.market.MarketManager;
import com.bdcraft.plugin.modules.economy.modules.market.ItemManager;
import com.bdcraft.plugin.modules.economy.modules.villager.BDVillagerModule;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Main economy module that manages all economy-related features.
 */
public class BDEconomyModule implements ModuleManager {
    private final BDCraft plugin;
    private boolean enabled = false;
    private final Map<String, SubmoduleBase> submodules = new HashMap<>();
    
    /**
     * Creates a new economy module.
     * 
     * @param plugin The plugin instance
     */
    public BDEconomyModule(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getName() {
        return "Economy";
    }
    
    @Override
    public void enable(BDCraft plugin) {
        if (enabled) {
            return;
        }
        
        plugin.getLogger().info("Enabling BDEconomy module");
        
        // Register and enable submodules
        registerSubmodules();
        
        enabled = true;
    }
    
    @Override
    public void disable() {
        if (!enabled) {
            return;
        }
        
        plugin.getLogger().info("Disabling BDEconomy module");
        
        // Disable submodules
        for (SubmoduleBase submodule : submodules.values()) {
            try {
                submodule.disable();
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error disabling submodule " + submodule.getName(), e);
            }
        }
        
        enabled = false;
    }
    
    @Override
    public void reload() {
        // Reload submodules
        for (SubmoduleBase submodule : submodules.values()) {
            try {
                submodule.reload();
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error reloading submodule " + submodule.getName(), e);
            }
        }
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    @Override
    public Object getSubmodule(String name) {
        return submodules.get(name);
    }
    
    @Override
    public void registerSubmodule(Object submodule) {
        if (!(submodule instanceof SubmoduleBase)) {
            throw new IllegalArgumentException("Submodule must implement SubmoduleBase");
        }
        
        SubmoduleBase base = (SubmoduleBase) submodule;
        submodules.put(base.getName(), base);
        
        if (enabled) {
            // Enable the submodule if the parent module is already enabled
            base.enable(this);
        }
    }
    
    /**
     * Registers all submodules.
     */
    private void registerSubmodules() {
        // Register market module
        registerSubmodule(new BDMarketModule(plugin));
        
        // Register auction module
        registerSubmodule(new BDAuctionModule(plugin));
        
        // Register villager module
        registerSubmodule(new BDVillagerModule(plugin));
        
        // Enable all submodules
        for (SubmoduleBase submodule : submodules.values()) {
            boolean enableSubmodule = plugin.getConfig(ConfigType.CONFIG)
                    .getBoolean("modules.economy." + submodule.getName().toLowerCase() + ".enabled", true);
            
            if (enableSubmodule) {
                try {
                    submodule.enable(this);
                    plugin.getLogger().info("Enabled " + submodule.getName() + " submodule");
                } catch (Exception e) {
                    plugin.getLogger().log(Level.SEVERE, "Error enabling submodule " + submodule.getName(), e);
                }
            }
        }
    }
    
    /**
     * Gets the BDMarketModule instance.
     * 
     * @return The market module, or null if not registered
     */
    public BDMarketModule getMarketModule() {
        return (BDMarketModule) getSubmodule("Market");
    }
    
    /**
     * Gets the BDAuctionModule instance.
     * 
     * @return The auction module, or null if not registered
     */
    public BDAuctionModule getAuctionModule() {
        return (BDAuctionModule) getSubmodule("Auction");
    }
    
    /**
     * Gets the BDVillagerModule instance.
     * 
     * @return The villager module, or null if not registered
     */
    public BDVillagerModule getVillagerModule() {
        return (BDVillagerModule) getSubmodule("Villager");
    }
    
    /**
     * Gets the item manager from the appropriate submodule.
     * 
     * @return The item manager, or null if not available
     */
    public ItemManager getItemManager() {
        // For now, we'll use the market module's item manager
        BDMarketModule marketModule = getMarketModule();
        if (marketModule != null) {
            return marketModule.getItemManager();
        }
        return null;
    }
    
    /**
     * Gets the market manager from the appropriate submodule.
     * 
     * @return The market manager, or null if not available
     */
    public MarketManager getMarketManager() {
        BDMarketModule marketModule = getMarketModule();
        if (marketModule != null) {
            return marketModule.getMarketManager();
        }
        return null;
    }
    
    /**
     * Checks if a player has enough emeralds for a transaction.
     * 
     * @param player The player to check
     * @param amount The amount of emeralds needed
     * @return True if the player has enough emeralds, false otherwise
     */
    public boolean hasEnoughEmeralds(org.bukkit.entity.Player player, int amount) {
        ItemManager itemManager = getItemManager();
        if (itemManager == null) {
            return false;
        }
        
        return itemManager.getPlayerEmeralds(player) >= amount;
    }
    
    /**
     * Removes emeralds from a player's inventory.
     * 
     * @param player The player to remove emeralds from
     * @param amount The amount of emeralds to remove
     * @return True if successful, false otherwise
     */
    public boolean removeEmeralds(org.bukkit.entity.Player player, int amount) {
        ItemManager itemManager = getItemManager();
        if (itemManager == null || !hasEnoughEmeralds(player, amount)) {
            return false;
        }
        
        return itemManager.removePlayerEmeralds(player, amount);
    }
}