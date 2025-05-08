package com.bdcraft.plugin.modules.economy;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.Module;
import com.bdcraft.plugin.modules.economy.farming.BDCropManager;
import com.bdcraft.plugin.modules.economy.items.BDItemManager;
import com.bdcraft.plugin.modules.economy.listeners.CropGrowthListener;
import com.bdcraft.plugin.modules.economy.listeners.HouseTokenListener;
import com.bdcraft.plugin.modules.economy.listeners.MarketTokenListener;
import com.bdcraft.plugin.modules.economy.listeners.VillagerTradeListener;
import com.bdcraft.plugin.modules.economy.crafting.BDRecipeManager;
import com.bdcraft.plugin.modules.economy.crafting.BDRecipeListener;
import com.bdcraft.plugin.modules.economy.auction.AuctionManager;
import com.bdcraft.plugin.modules.economy.auction.AuctionHouseGUI;
import com.bdcraft.plugin.modules.economy.auction.AuctionListener;
import com.bdcraft.plugin.modules.economy.market.MarketManager;
import com.bdcraft.plugin.modules.economy.market.gui.MarketManagementGUI;
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
    private BDCropManager cropManager;
    private BDRecipeManager recipeManager;
    private AuctionManager auctionManager;
    private AuctionHouseGUI auctionHouseGUI;
    private MarketManagementGUI marketManagementGUI;
    
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
        this.cropManager = new BDCropManager(plugin);
        this.recipeManager = new BDRecipeManager(plugin);
        this.auctionManager = new AuctionManager(plugin);
        this.auctionHouseGUI = new AuctionHouseGUI(plugin, auctionManager);
        
        // Register listeners
        plugin.getServer().getPluginManager().registerEvents(new MarketTokenListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new HouseTokenListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new CropGrowthListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new VillagerTradeListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new BDRecipeListener(plugin, recipeManager), plugin);
        plugin.getServer().getPluginManager().registerEvents(new AuctionListener(plugin, auctionHouseGUI), plugin);
        
        // Register recipes
        recipeManager.registerRecipes();
        
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
        
        // Unregister recipes
        recipeManager.unregisterRecipes();
        
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
     * Resets a player's balance (used for rebirth).
     * @param player The player
     */
    public void resetPlayerBalance(Player player) {
        playerBalances.put(player.getUniqueId(), 0);
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
    
    /**
     * Gets the crop manager.
     * @return The crop manager
     */
    public BDCropManager getCropManager() {
        return cropManager;
    }
    
    /**
     * Gets the recipe manager.
     * @return The recipe manager
     */
    public BDRecipeManager getRecipeManager() {
        return recipeManager;
    }
    
    /**
     * Gets the auction manager.
     * @return The auction manager
     */
    public AuctionManager getAuctionManager() {
        return auctionManager;
    }
    
    /**
     * Gets the auction house GUI.
     * @return The auction house GUI
     */
    public AuctionHouseGUI getAuctionHouseGUI() {
        return auctionHouseGUI;
    }
    
    /**
     * Gets the market management GUI.
     * @return The market management GUI
     */
    public MarketManagementGUI getMarketManagementGUI() {
        if (marketManagementGUI == null) {
            marketManagementGUI = new MarketManagementGUI(plugin, marketManager);
        }
        return marketManagementGUI;
    }
    
    /**
     * Checks if a player has enough coins.
     * 
     * @param player The player
     * @param amount The amount to check
     * @return True if the player has at least the specified amount
     */
    public boolean hasCoins(Player player, int amount) {
        return getPlayerBalance(player) >= amount;
    }
    
    /**
     * Adds coins to a player.
     * 
     * @param player The player
     * @param amount The amount to add
     * @return The new balance
     */
    public int addCoins(Player player, int amount) {
        return addPlayerBalance(player, amount);
    }
    
    /**
     * Removes coins from a player.
     * 
     * @param player The player
     * @param amount The amount to remove
     * @return The new balance, or -1 if the player doesn't have enough
     */
    public int removeCoins(Player player, int amount) {
        return removePlayerBalance(player, amount);
    }
    
    /**
     * Adds coins to an offline player.
     * 
     * @param playerUUID The player's UUID
     * @param amount The amount to add
     */
    public void addOfflinePlayerCoins(UUID playerUUID, int amount) {
        int currentBalance = playerBalances.getOrDefault(playerUUID, 0);
        playerBalances.put(playerUUID, currentBalance + amount);
        savePlayerBalances();
    }
}