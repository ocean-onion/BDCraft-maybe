package com.bdcraft.plugin.modules.progression.commands;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.market.BDMarket;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Command for viewing economic insights and predictions.
 */
public class BDInsightCommand implements CommandExecutor, TabCompleter {
    private final BDCraft plugin;
    
    // Available insight modes
    private final String[] INSIGHT_MODES = {
        "market",
        "economy",
        "trends"
    };
    
    /**
     * Creates a new insight command.
     *
     * @param plugin The plugin instance
     */
    public BDInsightCommand(BDCraft plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("bdcraft.insight")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }
        
        // Check rebirth level and cooldown for advanced insights
        if (args.length > 0 && (args[0].equalsIgnoreCase("trends") || args[0].equalsIgnoreCase("economy"))) {
            int rebirthLevel = plugin.getProgressionModule().getPlayerRebirthLevel(player);
            
            if (rebirthLevel < 1 && !player.hasPermission("bdcraft.admin")) {
                player.sendMessage(ChatColor.RED + "You must be reborn to access advanced economic insights.");
                return true;
            }
            
            // Check cooldown for advanced insights
            long lastUseTime = plugin.getProgressionModule().getRebirthManager().getLastCommandUse(player.getUniqueId(), "bdinsight");
            long currentTime = System.currentTimeMillis();
            long cooldown = 60 * 60 * 1000; // 1 hour
            
            if (lastUseTime > 0 && currentTime - lastUseTime < cooldown && !player.hasPermission("bdcraft.admin")) {
                long remainingMinutes = (cooldown - (currentTime - lastUseTime)) / (60 * 1000);
                
                player.sendMessage(ChatColor.RED + "You must wait " + remainingMinutes + 
                        " minutes before using advanced economic insights again.");
                return true;
            }
            
            // Record command use for advanced insights
            plugin.getProgressionModule().getRebirthManager().recordCommandUse(player.getUniqueId(), "bdinsight");
        }
        
        // Show insights based on the mode
        if (args.length == 0 || args[0].equalsIgnoreCase("market")) {
            showMarketInsights(player);
        } else if (args[0].equalsIgnoreCase("economy")) {
            showEconomyInsights(player);
        } else if (args[0].equalsIgnoreCase("trends")) {
            showTrendInsights(player);
        } else {
            player.sendMessage(ChatColor.RED + "Usage: /bdinsight [market|economy|trends]");
        }
        
        return true;
    }
    
    /**
     * Shows market insights.
     *
     * @param player The player
     */
    private void showMarketInsights(Player player) {
        player.sendMessage(ChatColor.GOLD + "======= " + ChatColor.GREEN + "Market Insights" + ChatColor.GOLD + " =======");
        
        // Get all markets
        List<BDMarket> markets = plugin.getMarketManager().getMarkets();
        
        if (markets.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + "There are no markets to analyze.");
            return;
        }
        
        // Count markets by level
        int[] marketsByLevel = new int[5]; // 0 index unused, levels are 1-4
        
        for (BDMarket market : markets) {
            marketsByLevel[market.getLevel()]++;
        }
        
        // Show total markets
        player.sendMessage(ChatColor.YELLOW + "Total Markets: " + ChatColor.WHITE + markets.size());
        
        // Show markets by level
        for (int i = 1; i <= 4; i++) {
            player.sendMessage(ChatColor.YELLOW + "Level " + i + " Markets: " + 
                    ChatColor.WHITE + marketsByLevel[i] + 
                    ChatColor.GRAY + " (" + String.format("%.1f", (marketsByLevel[i] * 100.0 / markets.size())) + "%)");
        }
        
        // Show markets owned by the player
        List<BDMarket> playerMarkets = plugin.getMarketManager().getPlayerMarkets(player);
        
        if (!playerMarkets.isEmpty()) {
            player.sendMessage("");
            player.sendMessage(ChatColor.YELLOW + "Your Markets: " + ChatColor.WHITE + playerMarkets.size());
            
            for (int i = 0; i < playerMarkets.size(); i++) {
                BDMarket market = playerMarkets.get(i);
                player.sendMessage(ChatColor.YELLOW + (i + 1) + ". " + 
                        ChatColor.WHITE + market.getName() + 
                        ChatColor.GRAY + " (Level " + market.getLevel() + ", " + 
                        market.getCollectorsCount() + "/" + market.getMaxCollectors() + " collectors)");
            }
        }
    }
    
    /**
     * Shows economy insights.
     *
     * @param player The player
     */
    private void showEconomyInsights(Player player) {
        player.sendMessage(ChatColor.GOLD + "======= " + ChatColor.GREEN + "Economy Insights" + ChatColor.GOLD + " =======");
        
        // Get crop values and demand
        Map<String, Double> cropValues = plugin.getEconomyModule().getCurrentCropValues();
        Map<String, Integer> cropDemand = plugin.getEconomyModule().getCurrentCropDemand();
        
        if (cropValues.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + "No economic data is available.");
            return;
        }
        
        // Sort crops by value
        List<Map.Entry<String, Double>> sortedCrops = new ArrayList<>(cropValues.entrySet());
        sortedCrops.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        
        // Show current values
        player.sendMessage(ChatColor.YELLOW + "Current Crop Values:");
        
        for (Map.Entry<String, Double> entry : sortedCrops) {
            String cropName = entry.getKey();
            double value = entry.getValue();
            int demand = cropDemand.getOrDefault(cropName, 50);
            
            // Determine demand indicator
            String demandIndicator;
            if (demand >= 80) {
                demandIndicator = ChatColor.GREEN + "↑↑ High";
            } else if (demand >= 60) {
                demandIndicator = ChatColor.GREEN + "↑ Good";
            } else if (demand >= 40) {
                demandIndicator = ChatColor.YELLOW + "→ Average";
            } else if (demand >= 20) {
                demandIndicator = ChatColor.RED + "↓ Low";
            } else {
                demandIndicator = ChatColor.RED + "↓↓ Very Low";
            }
            
            player.sendMessage(ChatColor.WHITE + cropName + ": " + 
                    ChatColor.YELLOW + String.format("%.2f", value) + 
                    ChatColor.GRAY + " (Demand: " + demandIndicator + ChatColor.GRAY + ")");
        }
        
        // Show economy statistics
        player.sendMessage("");
        player.sendMessage(ChatColor.YELLOW + "Economy Statistics:");
        
        double inflationRate = plugin.getEconomyModule().getInflationRate() * 100;
        double avgMarketValue = plugin.getEconomyModule().getAverageMarketValue();
        int totalTradesLastDay = plugin.getEconomyModule().getTotalTradesInPeriod(24 * 60 * 60 * 1000);
        
        player.sendMessage(ChatColor.WHITE + "Inflation Rate: " + 
                ChatColor.YELLOW + String.format("%.1f", inflationRate) + "%");
        player.sendMessage(ChatColor.WHITE + "Average Market Value: " + 
                ChatColor.YELLOW + String.format("%.2f", avgMarketValue));
        player.sendMessage(ChatColor.WHITE + "Total Trades (24h): " + 
                ChatColor.YELLOW + totalTradesLastDay);
    }
    
    /**
     * Shows trend insights.
     *
     * @param player The player
     */
    private void showTrendInsights(Player player) {
        player.sendMessage(ChatColor.GOLD + "======= " + ChatColor.GREEN + "Market Trends & Predictions" + ChatColor.GOLD + " =======");
        
        // Get price predictions
        Map<String, Double> predictions = plugin.getEconomyModule().getPricePredictions();
        Map<String, Double> currentValues = plugin.getEconomyModule().getCurrentCropValues();
        
        if (predictions.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + "No prediction data is available.");
            return;
        }
        
        // Sort by predicted change percentage
        List<Map.Entry<String, Double>> sortedPredictions = new ArrayList<>();
        
        for (Map.Entry<String, Double> entry : predictions.entrySet()) {
            String cropName = entry.getKey();
            double predicted = entry.getValue();
            double current = currentValues.getOrDefault(cropName, 1.0);
            
            double change = (predicted - current) / current * 100;
            
            // Store the change percentage in the value
            sortedPredictions.add(Map.entry(cropName, change));
        }
        
        // Sort by change percentage (descending)
        sortedPredictions.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        
        player.sendMessage(ChatColor.YELLOW + "Predicted Price Changes (Next Week):");
        
        for (Map.Entry<String, Double> entry : sortedPredictions) {
            String cropName = entry.getKey();
            double changePercent = entry.getValue();
            double current = currentValues.getOrDefault(cropName, 1.0);
            double predicted = current * (1 + changePercent / 100);
            
            // Determine trend indicator
            String trendIndicator;
            if (changePercent >= 10) {
                trendIndicator = ChatColor.GREEN + "↑↑ Strong Increase";
            } else if (changePercent >= 3) {
                trendIndicator = ChatColor.GREEN + "↑ Increase";
            } else if (changePercent >= -3) {
                trendIndicator = ChatColor.YELLOW + "→ Stable";
            } else if (changePercent >= -10) {
                trendIndicator = ChatColor.RED + "↓ Decrease";
            } else {
                trendIndicator = ChatColor.RED + "↓↓ Strong Decrease";
            }
            
            player.sendMessage(ChatColor.WHITE + cropName + ": " + 
                    ChatColor.YELLOW + String.format("%.2f", current) + 
                    ChatColor.GRAY + " → " + 
                    ChatColor.YELLOW + String.format("%.2f", predicted) + 
                    ChatColor.GRAY + " (" + trendIndicator + ChatColor.GRAY + ")");
        }
        
        // Investment tips
        player.sendMessage("");
        player.sendMessage(ChatColor.YELLOW + "Investment Tips:");
        
        // Find best increases and decreases
        if (!sortedPredictions.isEmpty()) {
            String bestIncrease = sortedPredictions.get(0).getKey();
            String worstDecrease = sortedPredictions.get(sortedPredictions.size() - 1).getKey();
            
            player.sendMessage(ChatColor.GREEN + "Best Buy: " + ChatColor.WHITE + bestIncrease);
            player.sendMessage(ChatColor.RED + "Consider Selling: " + ChatColor.WHITE + worstDecrease);
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            
            return Arrays.stream(INSIGHT_MODES)
                    .filter(mode -> mode.startsWith(input))
                    .collect(Collectors.toList());
        }
        
        return new ArrayList<>();
    }
}