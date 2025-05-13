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
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.entity.Player;

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
    
    /**
     * Gets a player's balance.
     *
     * @param player The player
     * @return The player's balance
     */
    public int getPlayerBalance(Player player) {
        return getCurrency(player.getUniqueId());
    }
    
    /**
     * Gets a player's currency by UUID.
     *
     * @param uuid The player's UUID
     * @return The currency amount
     */
    public int getCurrency(UUID uuid) {
        // Get the MarketModule to handle currency operations
        BDMarketModule marketModule = getMarketModule();
        if (marketModule != null) {
            return marketModule.getPlayerCurrency(uuid);
        }
        return 0;
    }
    
    /**
     * Resets a player's balance to zero.
     *
     * @param player The player
     */
    public void resetPlayerBalance(Player player) {
        setCurrency(player.getUniqueId(), 0);
    }
    
    /**
     * Adds currency to a player's balance.
     *
     * @param player The player
     * @param amount The amount to add
     * @return Whether the operation was successful
     */
    public boolean addPlayerBalance(Player player, int amount) {
        return addOfflinePlayerCoins(player.getUniqueId(), amount);
    }
    
    /**
     * Sets a player's currency by UUID.
     *
     * @param uuid The player's UUID
     * @param amount The amount to set
     */
    public void setCurrency(UUID uuid, int amount) {
        // Get the MarketModule to handle currency operations
        BDMarketModule marketModule = getMarketModule();
        if (marketModule != null) {
            marketModule.setPlayerCurrency(uuid, amount);
        }
    }
    
    /**
     * Adds coins to an offline player's account.
     *
     * @param playerUuid The player's UUID
     * @param amount The amount to add
     * @return Whether the operation was successful
     */
    public boolean addOfflinePlayerCoins(UUID playerUuid, int amount) {
        int currentBalance = getCurrency(playerUuid);
        setCurrency(playerUuid, currentBalance + amount);
        return true;
    }
    
    /**
     * Removes currency from a player's balance.
     *
     * @param player The player
     * @param amount The amount to remove
     * @return Whether the operation was successful
     */
    public boolean removePlayerBalance(Player player, int amount) {
        UUID uuid = player.getUniqueId();
        int currentBalance = getCurrency(uuid);
        
        if (currentBalance >= amount) {
            setCurrency(uuid, currentBalance - amount);
            return true;
        }
        
        return false;
    }
    
    /**
     * Checks if a player has enough coins.
     *
     * @param player The player
     * @param amount The amount to check
     * @return Whether the player has enough coins
     */
    public boolean hasCoins(Player player, int amount) {
        int currentBalance = getCurrency(player.getUniqueId());
        return currentBalance >= amount;
    }
    
    /**
     * Gets the villager manager.
     *
     * @return The villager manager
     */
    public com.bdcraft.plugin.modules.economy.modules.villager.impl.VillagerManager getVillagerManager() {
        BDVillagerModule villagerModule = getVillagerModule();
        return villagerModule != null ? villagerModule.getVillagerManager() : null;
    }
}