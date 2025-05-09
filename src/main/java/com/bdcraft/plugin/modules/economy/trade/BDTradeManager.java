package com.bdcraft.plugin.modules.economy.trade;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.BDEconomyModule;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Manages trades in the BDCraft economy system.
 */
public class BDTradeManager {
    private final BDCraft plugin;
    private final BDEconomyModule economyModule;
    private File tradesFile;
    private FileConfiguration tradesData;
    
    private final List<BDTrade> recentTrades;
    private final Map<UUID, Integer> playerTradeCounts;
    
    /**
     * Creates a new trade manager.
     *
     * @param plugin The plugin instance
     * @param economyModule The economy module
     */
    public BDTradeManager(BDCraft plugin, BDEconomyModule economyModule) {
        this.plugin = plugin;
        this.economyModule = economyModule;
        this.recentTrades = new ArrayList<>();
        this.playerTradeCounts = new HashMap<>();
        
        // Load trade data
        loadTradeData();
    }
    
    /**
     * Loads trade data from file.
     */
    private void loadTradeData() {
        // Clear existing data
        recentTrades.clear();
        playerTradeCounts.clear();
        
        // Create data directory if it doesn't exist
        File dataDir = new File(plugin.getDataFolder(), "data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        
        // Create or load trades file
        tradesFile = new File(dataDir, "trades.yml");
        if (!tradesFile.exists()) {
            try {
                tradesFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to create trades data file", e);
            }
        }
        
        // Load data
        tradesData = YamlConfiguration.loadConfiguration(tradesFile);
        
        // Load recent trades
        if (tradesData.contains("recent-trades")) {
            for (String key : tradesData.getConfigurationSection("recent-trades").getKeys(false)) {
                String tradeData = tradesData.getString("recent-trades." + key);
                BDTrade trade = BDTrade.deserialize(tradeData);
                
                if (trade != null) {
                    recentTrades.add(trade);
                } else {
                    plugin.getLogger().warning("Failed to load trade: " + key);
                }
            }
        }
        
        // Load player trade counts
        if (tradesData.contains("player-trades")) {
            for (String uuidString : tradesData.getConfigurationSection("player-trades").getKeys(false)) {
                try {
                    UUID playerId = UUID.fromString(uuidString);
                    int tradeCount = tradesData.getInt("player-trades." + uuidString);
                    playerTradeCounts.put(playerId, tradeCount);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid UUID in trade data: " + uuidString);
                }
            }
        }
        
        plugin.getLogger().info("Loaded " + recentTrades.size() + " recent trades and " + 
                playerTradeCounts.size() + " player trade records.");
    }
    
    /**
     * Saves trade data to file.
     */
    private void saveTradeData() {
        if (tradesData == null || tradesFile == null) {
            return;
        }
        
        // Clear existing data
        tradesData.set("recent-trades", null);
        tradesData.set("player-trades", null);
        
        // Save recent trades (keep only the most recent 1000)
        int count = 0;
        for (BDTrade trade : recentTrades) {
            if (count >= 1000) {
                break;
            }
            
            tradesData.set("recent-trades." + trade.getTradeId().toString(), trade.serialize());
            count++;
        }
        
        // Save player trade counts
        for (Map.Entry<UUID, Integer> entry : playerTradeCounts.entrySet()) {
            tradesData.set("player-trades." + entry.getKey().toString(), entry.getValue());
        }
        
        // Save data file
        try {
            tradesData.save(tradesFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save trades data", e);
        }
    }
    
    /**
     * Records a new trade.
     *
     * @param player The player making the trade
     * @param cropType The crop type being traded
     * @param quantity The quantity of crops
     * @param emeraldsEarned The emeralds earned
     * @param bdCurrencyEarned The BD currency earned
     * @param isPremium Whether this is a premium trade
     * @return The created trade
     */
    public BDTrade recordTrade(Player player, String cropType, int quantity, 
                              double emeraldsEarned, int bdCurrencyEarned, boolean isPremium) {
        UUID tradeId = UUID.randomUUID();
        UUID playerId = player.getUniqueId();
        long timestamp = System.currentTimeMillis();
        
        // Create the trade
        BDTrade trade = new BDTrade(tradeId, playerId, cropType, quantity, 
                                   emeraldsEarned, bdCurrencyEarned, timestamp, isPremium);
        
        // Add to recent trades
        recentTrades.add(0, trade); // Add at the beginning
        
        // Update player trade count
        int currentCount = playerTradeCounts.getOrDefault(playerId, 0);
        playerTradeCounts.put(playerId, currentCount + 1);
        
        // Save data
        saveTradeData();
        
        return trade;
    }
    
    /**
     * Gets recent trades.
     *
     * @return The list of recent trades
     */
    public List<BDTrade> getRecentTrades() {
        return new ArrayList<>(recentTrades);
    }
    
    /**
     * Gets a player's total trade count.
     *
     * @param playerId The player's UUID
     * @return The number of trades made by the player
     */
    public int getPlayerTradeCount(UUID playerId) {
        return playerTradeCounts.getOrDefault(playerId, 0);
    }
    
    /**
     * Gets a player's recent trades.
     *
     * @param playerId The player's UUID
     * @param limit The maximum number of trades to return
     * @return The list of recent trades by the player
     */
    public List<BDTrade> getPlayerRecentTrades(UUID playerId, int limit) {
        return recentTrades.stream()
                .filter(trade -> trade.getPlayerId().equals(playerId))
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    /**
     * Gets the total emeralds earned by a player.
     *
     * @param playerId The player's UUID
     * @return The total emeralds earned
     */
    public double getPlayerTotalEmeraldsEarned(UUID playerId) {
        return recentTrades.stream()
                .filter(trade -> trade.getPlayerId().equals(playerId))
                .mapToDouble(BDTrade::getEmeraldsEarned)
                .sum();
    }
    
    /**
     * Gets the total BD currency earned by a player.
     *
     * @param playerId The player's UUID
     * @return The total BD currency earned
     */
    public int getPlayerTotalBdCurrencyEarned(UUID playerId) {
        return recentTrades.stream()
                .filter(trade -> trade.getPlayerId().equals(playerId))
                .mapToInt(BDTrade::getBdCurrencyEarned)
                .sum();
    }
    
    /**
     * Gets the most traded crop overall.
     *
     * @return The most traded crop type
     */
    public String getMostTradedCrop() {
        Map<String, Integer> cropCounts = new HashMap<>();
        
        for (BDTrade trade : recentTrades) {
            String cropType = trade.getCropType();
            int currentCount = cropCounts.getOrDefault(cropType, 0);
            cropCounts.put(cropType, currentCount + trade.getQuantity());
        }
        
        if (cropCounts.isEmpty()) {
            return "WHEAT"; // Default if no trades
        }
        
        return cropCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("WHEAT");
    }
    
    /**
     * Clears old trades (older than 30 days).
     */
    public void clearOldTrades() {
        long cutoffTime = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000); // 30 days
        
        List<BDTrade> tradesToKeep = recentTrades.stream()
                .filter(trade -> trade.getTimestamp() > cutoffTime)
                .collect(Collectors.toList());
        
        if (tradesToKeep.size() < recentTrades.size()) {
            recentTrades.clear();
            recentTrades.addAll(tradesToKeep);
            saveTradeData();
            
            plugin.getLogger().info("Cleared " + (recentTrades.size() - tradesToKeep.size()) + " old trades.");
        }
    }
}