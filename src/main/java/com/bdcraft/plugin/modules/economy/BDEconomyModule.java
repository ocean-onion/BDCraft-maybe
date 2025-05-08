package com.bdcraft.plugin.modules.economy;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.Module;
import com.bdcraft.plugin.modules.economy.items.BDItemManager;
import com.bdcraft.plugin.modules.economy.listeners.CropGrowthListener;
import com.bdcraft.plugin.modules.economy.listeners.HouseTokenListener;
import com.bdcraft.plugin.modules.economy.listeners.MarketTokenListener;
import com.bdcraft.plugin.modules.economy.listeners.VillagerTradeListener;
import com.bdcraft.plugin.modules.economy.market.MarketManager;
import com.bdcraft.plugin.modules.economy.villager.BDVillagerManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The BD Economy Module.
 */
public class BDEconomyModule implements Module {
    private final BDCraft plugin;
    private final Logger logger;
    private boolean enabled = false;
    
    private BDItemManager itemManager;
    private BDVillagerManager villagerManager;
    private MarketManager marketManager;
    
    private final Map<UUID, Integer> playerBalances = new HashMap<>();
    private final File economyFile;
    private FileConfiguration economyConfig;
    
    /**
     * Creates a new BD economy module.
     * @param plugin The plugin instance
     */
    public BDEconomyModule(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.economyFile = new File(plugin.getDataFolder(), "economy.yml");
        
        // Ensure file exists
        if (!economyFile.exists()) {
            try {
                if (economyFile.createNewFile()) {
                    logger.info("Created economy.yml file");
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Could not create economy.yml file", e);
            }
        }
        
        // Load config
        this.economyConfig = YamlConfiguration.loadConfiguration(economyFile);
    }
    
    @Override
    public String getName() {
        return "Economy";
    }
    
    @Override
    public void enable() {
        if (enabled) {
            return;
        }
        
        logger.info("Enabling BD Economy Module");
        
        // Initialize managers
        this.itemManager = new BDItemManager(plugin);
        this.villagerManager = new BDVillagerManager(plugin);
        this.marketManager = new MarketManager(plugin);
        
        // Register listeners
        plugin.getServer().getPluginManager().registerEvents(new MarketTokenListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new HouseTokenListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new CropGrowthListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new VillagerTradeListener(plugin), plugin);
        
        // Load player balances
        loadPlayerBalances();
        
        enabled = true;
    }
    
    @Override
    public void disable() {
        if (!enabled) {
            return;
        }
        
        logger.info("Disabling BD Economy Module");
        
        // Save player balances
        savePlayerBalances();
        
        // Save markets
        marketManager.saveMarkets();
        
        enabled = false;
    }
    
    /**
     * Loads player balances from the configuration.
     */
    private void loadPlayerBalances() {
        if (!economyConfig.contains("balances")) {
            return;
        }
        
        for (String uuidStr : economyConfig.getConfigurationSection("balances").getKeys(false)) {
            try {
                UUID playerUuid = UUID.fromString(uuidStr);
                int balance = economyConfig.getInt("balances." + uuidStr);
                
                playerBalances.put(playerUuid, balance);
            } catch (IllegalArgumentException e) {
                logger.warning("Invalid UUID in economy.yml: " + uuidStr);
            }
        }
    }
    
    /**
     * Saves player balances to the configuration.
     */
    public void savePlayerBalances() {
        // Clear existing balances
        economyConfig.set("balances", null);
        
        // Add balances
        for (Map.Entry<UUID, Integer> entry : playerBalances.entrySet()) {
            UUID playerUuid = entry.getKey();
            int balance = entry.getValue();
            
            economyConfig.set("balances." + playerUuid.toString(), balance);
        }
        
        // Save config
        try {
            economyConfig.save(economyFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not save economy.yml file", e);
        }
    }
    
    /**
     * Gets a player's balance.
     * @param player The player
     * @return The balance
     */
    public int getPlayerBalance(Player player) {
        return playerBalances.getOrDefault(player.getUniqueId(), 0);
    }
    
    /**
     * Adds to a player's balance.
     * @param player The player
     * @param amount The amount to add
     * @return The new balance
     */
    public int addPlayerBalance(Player player, int amount) {
        int currentBalance = getPlayerBalance(player);
        int newBalance = currentBalance + amount;
        
        playerBalances.put(player.getUniqueId(), newBalance);
        
        return newBalance;
    }
    
    /**
     * Removes from a player's balance.
     * @param player The player
     * @param amount The amount to remove
     * @return The new balance, or -1 if the player doesn't have enough
     */
    public int removePlayerBalance(Player player, int amount) {
        int currentBalance = getPlayerBalance(player);
        
        if (currentBalance < amount) {
            return -1;
        }
        
        int newBalance = currentBalance - amount;
        playerBalances.put(player.getUniqueId(), newBalance);
        
        return newBalance;
    }
    
    /**
     * Gets the item manager.
     * @return The item manager
     */
    public BDItemManager getItemManager() {
        return itemManager;
    }
    
    /**
     * Gets the villager manager.
     * @return The villager manager
     */
    public BDVillagerManager getVillagerManager() {
        return villagerManager;
    }
    
    /**
     * Gets the market manager.
     * @return The market manager
     */
    public MarketManager getMarketManager() {
        return marketManager;
    }
}