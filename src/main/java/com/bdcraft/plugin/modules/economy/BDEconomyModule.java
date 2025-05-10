package com.bdcraft.plugin.modules.economy;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.BDModule;
import com.bdcraft.plugin.modules.economy.items.BDItemManager;
import com.bdcraft.plugin.modules.economy.market.MarketManager;
import com.bdcraft.plugin.modules.economy.trade.BDTrade;
import com.bdcraft.plugin.modules.economy.trade.BDTradeManager;
import com.bdcraft.plugin.modules.economy.villager.VillagerManager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Module for managing the BD economy, including currency and markets.
 */
public class BDEconomyModule extends BDModule {
    private final BDCraft plugin;
    private File dataFile;
    private FileConfiguration data;
    
    // Managers
    private MarketManager marketManager;
    private VillagerManager villagerManager;
    private BDItemManager itemManager;
    private com.bdcraft.plugin.modules.economy.auction.AuctionManager auctionManager;
    
    // Player balances
    private final Map<UUID, Integer> playerBalances;
    
    // Player trade counts
    private final Map<UUID, Integer> playerTrades;
    
    /**
     * Creates a new economy module.
     *
     * @param plugin The plugin instance
     */
    public BDEconomyModule(BDCraft plugin) {
        super(plugin, "economy");
        this.plugin = plugin;
        this.playerBalances = new HashMap<>();
        this.playerTrades = new HashMap<>();
    }
    
    @Override
    public void onEnable() {
        // Load data
        loadData();
        
        // Initialize managers
        itemManager = new BDItemManager(plugin);
        marketManager = new MarketManager(plugin, this);
        villagerManager = new VillagerManager(plugin);
        auctionManager = new com.bdcraft.plugin.modules.economy.auction.AuctionManager(plugin, this);
        
        // Load villagers
        villagerManager.loadAllVillagers();
        
        // Register commands
        // Will be implemented in the future
        
        // Register events
        // Will be implemented in the future
        
        plugin.getLogger().info("Economy module enabled.");
    }
    
    @Override
    public void onDisable() {
        // Save data
        saveData();
        
        plugin.getLogger().info("Economy module disabled.");
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
     * Gets the villager manager.
     *
     * @return The villager manager
     */
    public VillagerManager getVillagerManager() {
        return villagerManager;
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
     * Gets the auction manager.
     *
     * @return The auction manager
     */
    public com.bdcraft.plugin.modules.economy.auction.AuctionManager getAuctionManager() {
        return auctionManager;
    }
    
    /**
     * Loads player economy data.
     */
    private void loadData() {
        // Create data directory if it doesn't exist
        File dataDir = new File(plugin.getDataFolder(), "data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        
        // Create or load data file
        dataFile = new File(dataDir, "economy.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to create economy data file", e);
            }
        }
        
        // Load data
        data = YamlConfiguration.loadConfiguration(dataFile);
        
        // Load player balances
        if (data.contains("balances")) {
            for (String uuidString : data.getConfigurationSection("balances").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidString);
                    int balance = data.getInt("balances." + uuidString);
                    playerBalances.put(uuid, balance);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid UUID in economy data: " + uuidString);
                }
            }
        }
        
        plugin.getLogger().info("Loaded economy data for " + playerBalances.size() + " players.");
    }
    
    /**
     * Saves player economy data.
     */
    private void saveData() {
        if (data == null || dataFile == null) {
            return;
        }
        
        // Save player balances
        for (Map.Entry<UUID, Integer> entry : playerBalances.entrySet()) {
            data.set("balances." + entry.getKey().toString(), entry.getValue());
        }
        
        // Save data file
        try {
            data.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save economy data", e);
        }
    }
    
    /**
     * Gets a player's balance.
     *
     * @param player The player
     * @return The player's balance
     */
    public int getPlayerBalance(Player player) {
        return playerBalances.getOrDefault(player.getUniqueId(), 0);
    }
    
    /**
     * Sets a player's balance.
     *
     * @param player The player
     * @param amount The new balance
     */
    public void setPlayerBalance(Player player, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        
        playerBalances.put(player.getUniqueId(), amount);
        saveData();
    }
    
    /**
     * Adds currency to a player's balance.
     *
     * @param player The player
     * @param amount The amount to add
     * @return The new balance
     */
    public int addPlayerBalance(Player player, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        
        int balance = getPlayerBalance(player);
        int newBalance = balance + amount;
        
        setPlayerBalance(player, newBalance);
        
        return newBalance;
    }
    
    /**
     * Removes currency from a player's balance.
     *
     * @param player The player
     * @param amount The amount to remove
     * @return The new balance, or -1 if the player doesn't have enough
     */
    public int removePlayerBalance(Player player, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        
        int balance = getPlayerBalance(player);
        
        if (balance < amount) {
            return -1;
        }
        
        int newBalance = balance - amount;
        setPlayerBalance(player, newBalance);
        
        return newBalance;
    }
    
    /**
     * Checks if a player has enough currency.
     *
     * @param player The player
     * @param amount The amount to check
     * @return True if the player has at least this amount
     */
    public boolean hasCoins(Player player, int amount) {
        return getPlayerBalance(player) >= amount;
    }
    
    /**
     * Checks if a player has enough currency.
     *
     * @param playerId The player's UUID
     * @param amount The amount to check
     * @return True if the player has at least this amount
     */
    public boolean hasCurrency(UUID playerId, int amount) {
        return playerBalances.getOrDefault(playerId, 0) >= amount;
    }
    
    /**
     * Checks if a player has enough currency.
     *
     * @param player The player
     * @param amount The amount to check
     * @return True if the player has at least this amount
     */
    public boolean hasCurrency(Player player, int amount) {
        if (player == null) {
            return false;
        }
        
        return hasCurrency(player.getUniqueId(), amount);
    }
    
    /**
     * Gets player currency.
     *
     * @param playerId The player's UUID
     * @return The player's currency amount
     */
    public int getCurrency(UUID playerId) {
        return playerBalances.getOrDefault(playerId, 0);
    }
    
    /**
     * Sets a player's currency.
     *
     * @param playerId The player's UUID
     * @param amount The new currency amount
     */
    public void setCurrency(UUID playerId, int amount) {
        if (amount < 0) {
            amount = 0;
        }
        
        playerBalances.put(playerId, amount);
        saveData();
    }
    
    /**
     * Resets a player's balance to 0.
     *
     * @param player The player
     */
    public void resetPlayerBalance(Player player) {
        setPlayerBalance(player, 0);
    }
    
    /**
     * Adds currency to an offline player's balance.
     *
     * @param uuid The player's UUID
     * @param amount The amount to add
     * @return The new balance
     */
    public int addOfflinePlayerCoins(UUID uuid, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        
        int balance = playerBalances.getOrDefault(uuid, 0);
        int newBalance = balance + amount;
        
        playerBalances.put(uuid, newBalance);
        saveData();
        
        return newBalance;
    }
    
    /**
     * Gets the total currency in circulation.
     *
     * @return The total amount of currency held by all players
     */
    public int getTotalCurrency() {
        int total = 0;
        
        for (int balance : playerBalances.values()) {
            total += balance;
        }
        
        return total;
    }
    
    /**
     * Reloads economy data.
     */
    public void reloadData() {
        // Clear current data
        playerBalances.clear();
        
        // Load data
        loadData();
        
        // Reload market manager
        if (marketManager != null) {
            marketManager.reloadData();
        }
        
        plugin.getLogger().info("Reloaded economy data.");
    }
    
    // Trade system
    private BDTradeManager tradeManager;
    private Map<String, Double> cropValues = new HashMap<>();
    private Map<String, Integer> cropDemand = new HashMap<>();
    private double inflationRate = 0.02; // 2% base inflation rate
    
    /**
     * Gets the current values for crops.
     * 
     * @return Map of crop names to their market values
     */
    public Map<String, Double> getCurrentCropValues() {
        if (cropValues.isEmpty()) {
            initializeEconomyData();
        }
        return new HashMap<>(cropValues);
    }
    
    /**
     * Gets the current demand for crops.
     * 
     * @return Map of crop names to their demand levels (0-100)
     */
    public Map<String, Integer> getCurrentCropDemand() {
        if (cropDemand.isEmpty()) {
            initializeEconomyData();
        }
        return new HashMap<>(cropDemand);
    }
    
    /**
     * Initializes the economy data with default values.
     */
    private void initializeEconomyData() {
        // Default crop values
        cropValues.put("WHEAT", 1.0);
        cropValues.put("CARROT", 1.5);
        cropValues.put("POTATO", 1.2);
        cropValues.put("BEETROOT", 2.0);
        cropValues.put("MELON", 0.5);
        cropValues.put("PUMPKIN", 2.5);
        cropValues.put("SUGAR_CANE", 1.8);
        cropValues.put("COCOA", 3.0);
        
        // Default crop demand
        cropDemand.put("WHEAT", 70);
        cropDemand.put("CARROT", 60);
        cropDemand.put("POTATO", 65);
        cropDemand.put("BEETROOT", 40);
        cropDemand.put("MELON", 50);
        cropDemand.put("PUMPKIN", 45);
        cropDemand.put("SUGAR_CANE", 55);
        cropDemand.put("COCOA", 35);
        
        // Load from config if available
        ConfigurationSection cropSection = plugin.getConfig().getConfigurationSection("economy.crops");
        if (cropSection != null) {
            for (String cropName : cropSection.getKeys(false)) {
                double value = cropSection.getDouble(cropName + ".value", 1.0);
                int demand = cropSection.getInt(cropName + ".demand", 50);
                
                cropValues.put(cropName, value);
                cropDemand.put(cropName, demand);
            }
        }
        
        // Initialize trade manager
        tradeManager = new BDTradeManager(plugin, this);
    }
    
    /**
     * Updates the crop values based on market activity.
     */
    public void updateCropValues() {
        Random random = new Random();
        
        // Update demand with small random fluctuations
        for (String crop : cropDemand.keySet()) {
            int currentDemand = cropDemand.get(crop);
            int change = random.nextInt(11) - 5; // -5 to +5
            
            int newDemand = Math.max(10, Math.min(100, currentDemand + change));
            cropDemand.put(crop, newDemand);
            
            // Adjust value based on demand
            double demandFactor = (newDemand - 50) / 100.0; // -0.4 to +0.5
            double currentValue = cropValues.get(crop);
            double newValue = currentValue * (1 + (demandFactor * 0.1)); // Up to 10% change
            
            // Apply inflation
            newValue *= (1 + inflationRate / 365.0); // Daily inflation rate
            
            cropValues.put(crop, Math.max(0.1, newValue));
        }
    }
    
    /**
     * Gets the current inflation rate.
     * 
     * @return The inflation rate as a decimal (e.g., 0.02 for 2%)
     */
    public double getInflationRate() {
        return inflationRate;
    }
    
    /**
     * Gets the average market value across all crops.
     * 
     * @return The average market value
     */
    public double getAverageMarketValue() {
        if (cropValues.isEmpty()) {
            initializeEconomyData();
        }
        
        double total = 0;
        for (double value : cropValues.values()) {
            total += value;
        }
        
        return total / cropValues.size();
    }
    
    /**
     * Gets the total number of trades in a given time period.
     * 
     * @param periodMillis The time period in milliseconds
     * @return The number of trades in that period
     */
    public int getTotalTradesInPeriod(long periodMillis) {
        if (tradeManager == null) {
            initializeEconomyData();
        }
        
        long cutoffTime = System.currentTimeMillis() - periodMillis;
        List<BDTrade> recentTrades = tradeManager.getRecentTrades();
        
        int count = 0;
        for (BDTrade trade : recentTrades) {
            if (trade.getTimestamp() > cutoffTime) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Gets price predictions for the next week.
     * 
     * @return Map of crop names to their predicted values
     */
    public Map<String, Double> getPricePredictions() {
        if (cropValues.isEmpty()) {
            initializeEconomyData();
        }
        
        Map<String, Double> predictions = new HashMap<>();
        
        for (Map.Entry<String, Double> entry : cropValues.entrySet()) {
            String crop = entry.getKey();
            double currentValue = entry.getValue();
            int demand = cropDemand.get(crop);
            
            // Predict based on current demand trend
            double demandTrend = (demand - 50) / 50.0; // -1.0 to +1.0
            double predictedChange = demandTrend * 0.15; // Up to 15% change
            
            // Apply inflation
            double weeklyInflation = inflationRate / 52.0; // Weekly inflation
            
            double predictedValue = currentValue * (1 + predictedChange + weeklyInflation);
            predictions.put(crop, predictedValue);
        }
        
        return predictions;
    }
}