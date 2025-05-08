package com.bdcraft.plugin.modules.progression.commands;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.market.Market;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Command that provides seasonal market insights to players with high rebirth levels.
 */
public class BDInsightCommand implements CommandExecutor {
    private final BDCraft plugin;
    private static final long COOLDOWN_TIME = 60 * 60 * 1000; // 1 hour cooldown
    private static final int MIN_REBIRTH_LEVEL = 10;

    /**
     * Creates a new BDInsightCommand instance.
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

        // Check permission
        if (!player.hasPermission("bdcraft.deity.insight")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        // Check rebirth level
        int rebirthLevel = plugin.getProgressionModule().getRebirthManager().getPlayerRebirthLevel(player);
        if (rebirthLevel < MIN_REBIRTH_LEVEL) {
            player.sendMessage(ChatColor.RED + "You need rebirth level " + MIN_REBIRTH_LEVEL + " to use seasonal insight.");
            return true;
        }

        // Check cooldown
        long lastUseTime = plugin.getProgressionModule().getRebirthManager().getLastCommandUse(player.getUniqueId(), "bdinsight");
        long currentTime = System.currentTimeMillis();
        
        if (lastUseTime > 0 && (currentTime - lastUseTime) < COOLDOWN_TIME) {
            long timeLeft = (lastUseTime + COOLDOWN_TIME - currentTime) / 1000;
            int minutes = (int) (timeLeft / 60);
            int seconds = (int) (timeLeft % 60);
            
            player.sendMessage(ChatColor.RED + "Seasonal Insight is on cooldown. Available in " + 
                    minutes + "m " + seconds + "s.");
            return true;
        }

        // Record the use time
        plugin.getProgressionModule().getRebirthManager().recordCommandUse(player.getUniqueId(), "bdinsight");

        // Provide market insights
        provideMarketInsights(player);
        
        return true;
    }

    /**
     * Provides market and economic insights to the player.
     * 
     * @param player The player
     */
    private void provideMarketInsights(Player player) {
        player.sendMessage(ChatColor.LIGHT_PURPLE + "=== " + ChatColor.GOLD + "Seasonal Market Insights" + 
                ChatColor.LIGHT_PURPLE + " ===");
        
        // Get current date and time for the header
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = now.format(formatter);
        
        player.sendMessage(ChatColor.GRAY + "Market Analysis as of " + formattedDateTime);
        player.sendMessage("");

        // 1. Top-performing markets
        player.sendMessage(ChatColor.GOLD + "► Market Performance:");
        
        List<Market> markets = plugin.getEconomyModule().getMarketManager().getMarkets();
        
        if (markets.isEmpty()) {
            player.sendMessage(ChatColor.GRAY + "  No markets found in the world.");
        } else {
            // Find best and worst performing markets
            Market bestMarket = null;
            Market worstMarket = null;
            int bestSales = -1;
            int worstSales = Integer.MAX_VALUE;
            
            for (Market market : markets) {
                int sales = market.getTotalSales();
                
                if (sales > bestSales) {
                    bestSales = sales;
                    bestMarket = market;
                }
                
                if (sales < worstSales && sales > 0) {
                    worstSales = sales;
                    worstMarket = market;
                }
            }
            
            if (bestMarket != null) {
                player.sendMessage(ChatColor.GREEN + "  Best Market: " + ChatColor.WHITE + 
                        bestMarket.getName() + ChatColor.GRAY + " (Owner: " + bestMarket.getOwnerName() + ")");
                player.sendMessage(ChatColor.GREEN + "  Recent Sales: " + ChatColor.WHITE + bestMarket.getTotalSales());
            }
            
            if (worstMarket != null) {
                player.sendMessage(ChatColor.RED + "  Underperforming: " + ChatColor.WHITE + 
                        worstMarket.getName() + ChatColor.GRAY + " (Owner: " + worstMarket.getOwnerName() + ")");
                player.sendMessage(ChatColor.RED + "  Recent Sales: " + ChatColor.WHITE + worstMarket.getTotalSales());
            }
        }
        
        player.sendMessage("");
        
        // 2. Current crop values and trends
        player.sendMessage(ChatColor.GOLD + "► Crop Value Trends:");
        
        Map<String, Double> cropValues = plugin.getEconomyModule().getCurrentCropValues();
        Map<String, Integer> cropDemand = plugin.getEconomyModule().getCurrentCropDemand();
        
        if (cropValues.isEmpty()) {
            player.sendMessage(ChatColor.GRAY + "  No crop data available.");
        } else {
            // Get top 3 most valuable crops
            List<Map.Entry<String, Double>> sortedCrops = new ArrayList<>(cropValues.entrySet());
            sortedCrops.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
            
            List<Map.Entry<String, Double>> topCrops = sortedCrops.stream()
                    .limit(3)
                    .collect(Collectors.toList());
            
            player.sendMessage(ChatColor.YELLOW + "  Most Valuable Crops:");
            
            for (Map.Entry<String, Double> crop : topCrops) {
                String cropName = crop.getKey();
                double value = crop.getValue();
                int demand = cropDemand.getOrDefault(cropName, 0);
                
                // Format the crop name to look nicer
                String formattedName = cropName.substring(0, 1).toUpperCase() + 
                        cropName.substring(1).toLowerCase().replace("_", " ");
                
                String demandLabel;
                if (demand > 80) {
                    demandLabel = ChatColor.GREEN + "High";
                } else if (demand > 40) {
                    demandLabel = ChatColor.YELLOW + "Medium";
                } else {
                    demandLabel = ChatColor.RED + "Low";
                }
                
                player.sendMessage(ChatColor.WHITE + "  • " + formattedName + ": " + 
                        ChatColor.GOLD + value + " BD" + ChatColor.GRAY + " (Demand: " + demandLabel + ChatColor.GRAY + ")");
            }
        }
        
        player.sendMessage("");
        
        // 3. Economic indicators
        player.sendMessage(ChatColor.GOLD + "► Economic Indicators:");
        
        double inflationRate = plugin.getEconomyModule().getInflationRate() * 100;
        double avgMarketValue = plugin.getEconomyModule().getAverageMarketValue();
        int totalTradesLastDay = plugin.getEconomyModule().getTotalTradesInPeriod(24 * 60 * 60 * 1000);
        
        player.sendMessage(ChatColor.WHITE + "  Inflation Rate: " + ChatColor.YELLOW + 
                String.format("%.1f", inflationRate) + "%");
        player.sendMessage(ChatColor.WHITE + "  Avg. Market Value: " + ChatColor.YELLOW + 
                String.format("%.0f", avgMarketValue) + " BD");
        player.sendMessage(ChatColor.WHITE + "  Daily Trades: " + ChatColor.YELLOW + totalTradesLastDay);
        
        player.sendMessage("");
        
        // 4. Predictions for future crop values
        player.sendMessage(ChatColor.GOLD + "► Future Price Predictions:");
        
        Map<String, Double> predictions = plugin.getEconomyModule().getPricePredictions();
        
        if (predictions.isEmpty()) {
            player.sendMessage(ChatColor.GRAY + "  No predictions available.");
        } else {
            List<Map.Entry<String, Double>> sortedPredictions = new ArrayList<>(predictions.entrySet());
            // Sort by prediction value (highest increase first)
            sortedPredictions.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
            
            List<Map.Entry<String, Double>> topPredictions = sortedPredictions.stream()
                    .limit(4)
                    .collect(Collectors.toList());
            
            for (Map.Entry<String, Double> prediction : topPredictions) {
                String cropName = prediction.getKey();
                double changePercent = prediction.getValue() * 100;
                
                // Format the crop name to look nicer
                String formattedName = cropName.substring(0, 1).toUpperCase() + 
                        cropName.substring(1).toLowerCase().replace("_", " ");
                
                ChatColor trendColor = changePercent >= 0 ? ChatColor.GREEN : ChatColor.RED;
                String trendSymbol = changePercent >= 0 ? "↑" : "↓";
                String trendText = String.format("%.1f", Math.abs(changePercent)) + "%";
                
                player.sendMessage(ChatColor.WHITE + "  • " + formattedName + ": " + 
                        trendColor + trendSymbol + " " + trendText);
            }
        }
        
        // Footer
        player.sendMessage("");
        player.sendMessage(ChatColor.LIGHT_PURPLE + "This insight will be available again in 1 hour.");
        
        // Play special effect sound
        player.playSound(player.getLocation(), "entity.experience_orb.pickup", 1.0f, 0.5f);
    }
}