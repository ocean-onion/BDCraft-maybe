package com.bdcraft.plugin.modules.economy;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.api.EconomyAPI;
import com.bdcraft.plugin.modules.BDModule;
import com.bdcraft.plugin.modules.economy.market.MarketManager;
import com.bdcraft.plugin.modules.economy.market.BDMarketManager;
import com.bdcraft.plugin.modules.economy.auction.AuctionManager;
import com.bdcraft.plugin.modules.economy.auction.AuctionHouseGUI;
import com.bdcraft.plugin.modules.economy.items.BDItemManager;
import com.bdcraft.plugin.modules.economy.gui.MarketManagementGUI;
import com.bdcraft.plugin.modules.economy.villager.BDVillagerManager;
import com.bdcraft.plugin.modules.economy.listeners.RecipeValidationListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Module for handling BDCraft economy, currency, markets and trading.
 */
public class BDEconomyModule implements BDModule, EconomyAPI {
    private final BDCraft plugin;
    private final Logger logger;
    private final Map<UUID, Integer> playerBalances;
    private MarketManager marketManager;
    private BDMarketManager bdMarketManager;
    
    // Crop economy data
    private final Map<String, Double> cropValues;
    private final Map<String, Integer> cropDemand;
    private final Map<String, Double> pricePredictions;
    
    // Market statistics data
    private double inflationRate;
    private double averageMarketValue;
    private int totalTradesInDay;
    private long lastTradeUpdateTime;
    
    // Other managers
    private AuctionManager auctionManager;
    private AuctionHouseGUI auctionHouseGUI;
    private BDItemManager itemManager;
    private MarketManagementGUI marketManagementGUI;
    private BDVillagerManager bdVillagerManager;
    
    /**
     * Creates a new BD economy module.
     * @param plugin The plugin instance
     */
    public BDEconomyModule(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.playerBalances = new HashMap<>();
        this.cropValues = new HashMap<>();
        this.cropDemand = new HashMap<>();
        this.pricePredictions = new HashMap<>();
        this.inflationRate = 0.0;
        this.averageMarketValue = 100.0;
        this.totalTradesInDay = 0;
        this.lastTradeUpdateTime = System.currentTimeMillis();
    }
    
    @Override
    public void onEnable() {
        logger.info("Enabling BD Economy Module...");
        
        // Initialize market manager
        marketManager = new MarketManager(plugin);
        
        // Initialize BD market manager
        bdMarketManager = new BDMarketManager(plugin, marketManager);
        
        // Initialize auction manager
        auctionManager = new AuctionManager(plugin);
        
        // Initialize auction house GUI
        auctionHouseGUI = new AuctionHouseGUI(plugin, auctionManager);
        
        // Initialize item manager
        itemManager = new BDItemManager(plugin);
        
        // Initialize market management GUI
        marketManagementGUI = new MarketManagementGUI(plugin);
        
        // Initialize villager manager
        bdVillagerManager = new BDVillagerManager(plugin);
        
        // Register API interface
        plugin.setEconomyAPI(this);
        
        // Register listeners
        registerListeners();
        
        // Load economy data
        loadEconomyData();
        
        // Initialize crop values
        initializeCropValues();
        
        logger.info("BD Economy Module enabled.");
    }
    
    /**
     * Registers all listeners for the economy module.
     */
    private void registerListeners() {
        // Register recipe validation listener
        RecipeValidationListener recipeListener = new RecipeValidationListener(plugin);
        plugin.getServer().getPluginManager().registerEvents(recipeListener, plugin);
        
        logger.info("Registered economy module listeners.");
    }
    
    @Override
    public void onDisable() {
        logger.info("Disabling BD Economy Module...");
        
        // Save economy data
        saveEconomyData();
        
        logger.info("BD Economy Module disabled.");
    }
    
    @Override
    public void onReload() {
        logger.info("Reloading BD Economy Module...");
        
        // Reload economy data
        loadEconomyData();
        
        logger.info("BD Economy Module reloaded.");
    }
    
    @Override
    public List<String> getDependencies() {
        return Arrays.asList();
    }
    
    @Override
    public String getName() {
        return "Economy";
    }
    
    /**
     * Loads economy data from configuration.
     */
    private void loadEconomyData() {
        FileConfiguration config = plugin.getConfig();
        
        // Load economy configuration
        inflationRate = config.getDouble("economy.inflation_rate", 0.02);
        averageMarketValue = config.getDouble("economy.average_market_value", 100.0);
    }
    
    /**
     * Saves economy data to configuration.
     */
    private void saveEconomyData() {
        FileConfiguration config = plugin.getConfig();
        
        // Save economy configuration
        config.set("economy.inflation_rate", inflationRate);
        config.set("economy.average_market_value", averageMarketValue);
        
        plugin.saveConfig();
    }
    
    /**
     * Initializes crop values.
     */
    private void initializeCropValues() {
        // Base crop values
        cropValues.put("wheat", 5.0);
        cropValues.put("carrot", 6.0);
        cropValues.put("potato", 6.0);
        cropValues.put("beetroot", 8.0);
        cropValues.put("melon", 4.0);
        cropValues.put("pumpkin", 10.0);
        cropValues.put("sugar_cane", 3.0);
        cropValues.put("cactus", 3.0);
        cropValues.put("cocoa_beans", 12.0);
        cropValues.put("sweet_berries", 7.0);
        cropValues.put("red_mushroom", 15.0);
        cropValues.put("brown_mushroom", 12.0);
        cropValues.put("nether_wart", 20.0);
        cropValues.put("chorus_fruit", 25.0);
        
        // Base crop demand
        cropDemand.put("wheat", 100);
        cropDemand.put("carrot", 80);
        cropDemand.put("potato", 80);
        cropDemand.put("beetroot", 60);
        cropDemand.put("melon", 70);
        cropDemand.put("pumpkin", 50);
        cropDemand.put("sugar_cane", 90);
        cropDemand.put("cactus", 30);
        cropDemand.put("cocoa_beans", 40);
        cropDemand.put("sweet_berries", 45);
        cropDemand.put("red_mushroom", 25);
        cropDemand.put("brown_mushroom", 30);
        cropDemand.put("nether_wart", 20);
        cropDemand.put("chorus_fruit", 15);
        
        // Initial price predictions
        updatePricePredictions();
    }
    
    /**
     * Updates price predictions based on current trends.
     */
    private void updatePricePredictions() {
        // Simulate market trends
        LocalDateTime now = LocalDateTime.now();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        
        // Clear existing predictions
        pricePredictions.clear();
        
        // Create seasonal trends
        if (month >= 3 && month <= 5) {
            // Spring trends
            pricePredictions.put("carrot", 0.15);
            pricePredictions.put("potato", 0.10);
            pricePredictions.put("wheat", 0.05);
            pricePredictions.put("beetroot", 0.20);
        } else if (month >= 6 && month <= 8) {
            // Summer trends
            pricePredictions.put("melon", 0.25);
            pricePredictions.put("sugar_cane", 0.15);
            pricePredictions.put("cactus", 0.10);
            pricePredictions.put("sweet_berries", 0.20);
        } else if (month >= 9 && month <= 11) {
            // Fall trends
            pricePredictions.put("pumpkin", 0.30);
            pricePredictions.put("cocoa_beans", 0.15);
            pricePredictions.put("red_mushroom", 0.25);
            pricePredictions.put("brown_mushroom", 0.20);
        } else {
            // Winter trends
            pricePredictions.put("nether_wart", 0.15);
            pricePredictions.put("chorus_fruit", 0.25);
            pricePredictions.put("sweet_berries", -0.10);
            pricePredictions.put("pumpkin", -0.15);
        }
        
        // Add some random predictions based on day of month
        if (day % 5 == 0) {
            pricePredictions.put("wheat", 0.10);
        } else if (day % 5 == 1) {
            pricePredictions.put("carrot", 0.12);
        } else if (day % 5 == 2) {
            pricePredictions.put("potato", 0.08);
        } else if (day % 5 == 3) {
            pricePredictions.put("melon", 0.15);
        } else {
            pricePredictions.put("pumpkin", 0.11);
        }
    }
    
    /**
     * Gets the market manager.
     * 
     * @return The market manager
     */
    public MarketManager getMarketManager() {
        return marketManager;
    }
    
    /**
     * Gets the auction manager.
     * 
     * @return The auction manager
     */
    public AuctionManager getAuctionManager() {
        return auctionManager;
    }
    
    /**
     * Gets the item manager.
     * 
     * @return The item manager
     */
    public BDItemManager getItemManager() {
        return itemManager;
    }
    
    /**
     * Gets the BD market manager.
     * 
     * @return The BD market manager
     */
    public BDMarketManager getBDMarketManager() {
        return bdMarketManager;
    }
    
    /**
     * Gets the market management GUI.
     * 
     * @return The market management GUI
     */
    public MarketManagementGUI getMarketManagementGUI() {
        return marketManagementGUI;
    }
    
    /**
     * Gets the BD villager manager.
     * 
     * @return The BD villager manager
     */
    public BDVillagerManager getBDVillagerManager() {
        return bdVillagerManager;
    }
    
    /**
     * Gets the auction house GUI.
     * 
     * @return The auction house GUI
     */
    public AuctionHouseGUI getAuctionHouseGUI() {
        return auctionHouseGUI;
    }
    
    /**
     * Gets a player's balance.
     * 
     * @param player The player
     * @return The balance
     */
    public int getPlayerBalance(Player player) {
        return playerBalances.getOrDefault(player.getUniqueId(), 0);
    }
    
    /**
     * Sets a player's balance.
     * 
     * @param player The player
     * @param balance The new balance
     */
    public void setPlayerBalance(Player player, int balance) {
        playerBalances.put(player.getUniqueId(), balance);
    }
    
    /**
     * Resets a player's balance.
     * 
     * @param player The player
     */
    public void resetPlayerBalance(Player player) {
        playerBalances.put(player.getUniqueId(), 0);
    }
    
    /**
     * Adds to a player's balance.
     * 
     * @param player The player
     * @param amount The amount to add
     */
    public void addPlayerBalance(Player player, int amount) {
        int balance = getPlayerBalance(player);
        setPlayerBalance(player, balance + amount);
    }
    
    /**
     * Removes from a player's balance.
     * 
     * @param player The player
     * @param amount The amount to remove
     * @return The amount actually removed
     */
    public int removePlayerBalance(Player player, int amount) {
        int balance = getPlayerBalance(player);
        
        // Ensure we don't go negative
        if (balance < amount) {
            amount = balance;
        }
        
        setPlayerBalance(player, balance - amount);
        return amount;
    }
    
    /**
     * Adds coins to an offline player.
     * 
     * @param uuid The player UUID
     * @param amount The amount to add
     */
    public void addOfflinePlayerCoins(UUID uuid, int amount) {
        int balance = (int) getBalance(uuid);
        playerBalances.put(uuid, balance + amount);
    }
    
    /**
     * Checks if a player has coins.
     * 
     * @param player The player
     * @param amount The amount to check
     * @return True if player has enough coins
     */
    public boolean hasCoins(Player player, int amount) {
        return getPlayerBalance(player) >= amount;
    }
    
    /**
     * Gets the current crop values.
     * 
     * @return The crop values
     */
    public Map<String, Double> getCurrentCropValues() {
        return new HashMap<>(cropValues);
    }
    
    /**
     * Gets the current crop demand.
     * 
     * @return The crop demand
     */
    public Map<String, Integer> getCurrentCropDemand() {
        return new HashMap<>(cropDemand);
    }
    
    /**
     * Gets the current price predictions.
     * 
     * @return The price predictions
     */
    public Map<String, Double> getPricePredictions() {
        return new HashMap<>(pricePredictions);
    }
    
    /**
     * Gets the current inflation rate.
     * 
     * @return The inflation rate
     */
    public double getInflationRate() {
        return inflationRate;
    }
    
    /**
     * Gets the average market value.
     * 
     * @return The average market value
     */
    public double getAverageMarketValue() {
        return averageMarketValue;
    }
    
    /**
     * Gets the total trades in a period.
     * 
     * @param period The period in milliseconds
     * @return The total trades
     */
    public int getTotalTradesInPeriod(long period) {
        // For now, return a simulated value
        // In a real implementation, this would look at actual trade history
        return totalTradesInDay;
    }
    
    // EconomyAPI implementation
    
    @Override
    public double getBalance(UUID uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player != null) {
            return getPlayerBalance(player);
        }
        return 0;
    }
    
    @Override
    public void setBalance(UUID uuid, double amount) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player != null) {
            setPlayerBalance(player, (int) amount);
        }
    }
    
    @Override
    public boolean hasEnough(UUID uuid, double amount) {
        return getBalance(uuid) >= amount;
    }
    
    @Override
    public boolean depositMoney(UUID uuid, double amount) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player != null) {
            int newBalance = getPlayerBalance(player) + (int) amount;
            setPlayerBalance(player, newBalance);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean withdrawMoney(UUID uuid, double amount) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player != null && hasEnough(uuid, amount)) {
            int newBalance = getPlayerBalance(player) - (int) amount;
            setPlayerBalance(player, newBalance);
            return true;
        }
        return false;
    }
    
    @Override
    public String formatCurrency(double amount) {
        return getCurrencySymbol() + " " + String.format("%.2f", amount);
    }
    
    @Override
    public String getCurrencyName() {
        return "BDCoin";
    }
    
    @Override
    public String getCurrencyNamePlural() {
        return "BDCoins";
    }
    
    @Override
    public String getCurrencySymbol() {
        return "§6BD§r";
    }
    
    // Custom methods not part of the EconomyAPI interface
    public boolean transferMoney(UUID fromUuid, UUID toUuid, double amount) {
        if (hasEnough(fromUuid, amount)) {
            withdrawMoney(fromUuid, amount);
            depositMoney(toUuid, amount);
            return true;
        }
        return false;
    }
    
    public String getFormattedBalance(UUID uuid) {
        double balance = getBalance(uuid);
        return String.format("%.2f BD", balance);
    }
    
    public void setCurrency(UUID uuid, double amount) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player != null) {
            setPlayerBalance(player, (int) amount);
        }
    }
    
    public boolean hasCurrency(Player player, int amount) {
        return getPlayerBalance(player) >= amount;
    }
    
    public double getCurrencyValue(String itemType) {
        return cropValues.getOrDefault(itemType, 0.0);
    }
    
    public Map<String, Double> getAllCurrencyValues() {
        return getCurrentCropValues();
    }
    
    public boolean setCurrencyValue(String itemType, double value) {
        cropValues.put(itemType, value);
        return true;
    }
    
    public List<UUID> getTopBalances(int limit) {
        // This would require a proper database implementation
        // Placeholder for now
        return new ArrayList<>();
    }
}