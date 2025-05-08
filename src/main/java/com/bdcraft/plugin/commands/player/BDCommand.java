package com.bdcraft.plugin.commands.player;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.BDEconomyModule;
import com.bdcraft.plugin.modules.economy.market.BDMarket;
import com.bdcraft.plugin.modules.economy.market.MarketManager;
import com.bdcraft.plugin.modules.progression.BDRankManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main command for BD Plugin.
 */
public class BDCommand implements CommandExecutor, TabCompleter {
    private final BDCraft plugin;
    
    /**
     * Creates a new BD command.
     * @param plugin The plugin instance
     */
    public BDCommand(BDCraft plugin) {
        this.plugin = plugin;
        plugin.getCommand("bd").setExecutor(this);
        plugin.getCommand("bd").setTabCompleter(this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            sendMainMenu(player);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "balance":
            case "bal":
                showBalance(player);
                break;
            case "market":
                handleMarketCommand(player, args);
                break;
            case "rank":
                showRank(player);
                break;
            case "help":
                sendHelp(player);
                break;
            default:
                sendMainMenu(player);
                break;
        }
        
        return true;
    }
    
    /**
     * Shows a player's balance.
     * @param player The player
     */
    private void showBalance(Player player) {
        BDEconomyModule economyModule = plugin.getEconomyModule();
        int balance = economyModule.getPlayerBalance(player);
        
        player.sendMessage(ChatColor.GOLD + "=== Your BD Balance ===");
        player.sendMessage(ChatColor.GREEN + "Balance: " + balance + " BD Currency");
    }
    
    /**
     * Handles the market subcommand.
     * @param player The player
     * @param args The command arguments
     */
    private void handleMarketCommand(Player player, String[] args) {
        if (args.length < 2) {
            sendMarketHelp(player);
            return;
        }
        
        MarketManager marketManager = plugin.getEconomyModule().getMarketManager();
        
        switch (args[1].toLowerCase()) {
            case "info":
                showMarketInfo(player, marketManager);
                break;
            case "list":
                listMarkets(player, marketManager);
                break;
            default:
                sendMarketHelp(player);
                break;
        }
    }
    
    /**
     * Shows information about the market a player is in.
     * @param player The player
     * @param marketManager The market manager
     */
    private void showMarketInfo(Player player, MarketManager marketManager) {
        BDMarket market = marketManager.getMarketAt(player.getLocation());
        
        if (market == null) {
            player.sendMessage(ChatColor.RED + "You are not in a market.");
            return;
        }
        
        // Show market info
        player.sendMessage(ChatColor.GOLD + "=== Market Information ===");
        player.sendMessage(ChatColor.YELLOW + "Founder: " + ChatColor.WHITE + market.getFounderName());
        player.sendMessage(ChatColor.YELLOW + "Level: " + ChatColor.WHITE + market.getLevel());
        player.sendMessage(ChatColor.YELLOW + "Collectors: " + ChatColor.WHITE + market.getTraderCount("COLLECTOR") + 
                "/" + getMaxCollectors(market));
    }
    
    /**
     * Lists markets in the player's world.
     * @param player The player
     * @param marketManager The market manager
     */
    private void listMarkets(Player player, MarketManager marketManager) {
        List<BDMarket> markets = marketManager.getMarketsInWorld(player.getWorld().getName());
        
        if (markets.isEmpty()) {
            player.sendMessage(ChatColor.RED + "There are no markets in this world.");
            return;
        }
        
        player.sendMessage(ChatColor.GOLD + "=== Markets in " + player.getWorld().getName() + " ===");
        
        for (int i = 0; i < Math.min(markets.size(), 5); i++) {
            BDMarket market = markets.get(i);
            player.sendMessage(ChatColor.YELLOW + "Founder: " + ChatColor.WHITE + market.getFounderName() + 
                    ChatColor.YELLOW + " | Level: " + ChatColor.WHITE + market.getLevel() + 
                    ChatColor.YELLOW + " | Location: " + ChatColor.WHITE + 
                    formatLocation(market.getCenter()));
        }
        
        if (markets.size() > 5) {
            player.sendMessage(ChatColor.GRAY + "...and " + (markets.size() - 5) + " more.");
        }
    }
    
    /**
     * Shows a player's rank.
     * @param player The player
     */
    private void showRank(Player player) {
        BDRankManager rankManager = plugin.getProgressionModule().getRankManager();
        String rankName = rankManager.getPlayerRankName(player);
        int experience = rankManager.getPlayerExperience(player);
        int nextRankExp = rankManager.getNextRankExperience(player);
        
        player.sendMessage(ChatColor.GOLD + "=== Your BD Rank ===");
        player.sendMessage(ChatColor.GREEN + "Current Rank: " + rankName);
        player.sendMessage(ChatColor.GREEN + "Experience: " + experience + 
                (nextRankExp > 0 ? " / " + nextRankExp + " for next rank" : " (Max Rank)"));
        
        // Show rebirth status if applicable
        if (rankManager.hasRebirthStatus(player)) {
            int rebirthLevel = rankManager.getRebirthLevel(player);
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Rebirth Level: " + rebirthLevel);
        }
    }
    
    /**
     * Gets the maximum number of collectors for a market.
     * @param market The market
     * @return The maximum number of collectors
     */
    private int getMaxCollectors(BDMarket market) {
        int level = market.getLevel();
        
        switch (level) {
            case 1:
                return 3;
            case 2:
                return 5;
            case 3:
                return 7;
            case 4:
                return 10;
            default:
                return 3;
        }
    }
    
    /**
     * Formats a location for display.
     * @param location The location
     * @return The formatted location
     */
    private String formatLocation(org.bukkit.Location location) {
        return location.getBlockX() + ", " + 
                location.getBlockY() + ", " + 
                location.getBlockZ();
    }
    
    /**
     * Sends the main menu to a player.
     * @param player The player
     */
    private void sendMainMenu(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== BD Plugin Commands ===");
        player.sendMessage(ChatColor.YELLOW + "/bd balance" + ChatColor.WHITE + " - Shows your BD currency balance");
        player.sendMessage(ChatColor.YELLOW + "/bd market" + ChatColor.WHITE + " - Market management commands");
        player.sendMessage(ChatColor.YELLOW + "/bd rank" + ChatColor.WHITE + " - Shows your current rank and progress");
        player.sendMessage(ChatColor.YELLOW + "/bd help" + ChatColor.WHITE + " - Shows this help message");
    }
    
    /**
     * Sends market help to a player.
     * @param player The player
     */
    private void sendMarketHelp(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== BD Market Commands ===");
        player.sendMessage(ChatColor.YELLOW + "/bd market info" + ChatColor.WHITE + " - Shows information about the market you're in");
        player.sendMessage(ChatColor.YELLOW + "/bd market list" + ChatColor.WHITE + " - Lists all markets in the current world");
    }
    
    /**
     * Sends help to a player.
     * @param player The player
     */
    private void sendHelp(Player player) {
        sendMainMenu(player);
        
        // Additional help
        player.sendMessage(ChatColor.GOLD + "=== BD Plugin Help ===");
        player.sendMessage(ChatColor.YELLOW + "• Use BD Seeds to grow special crops");
        player.sendMessage(ChatColor.YELLOW + "• Sell crops to BD Collectors for currency");
        player.sendMessage(ChatColor.YELLOW + "• Create your own market with a Market Token");
        player.sendMessage(ChatColor.YELLOW + "• Place a House Token in your market to add a Collector");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = Arrays.asList("balance", "market", "rank", "help");
            
            return completions.stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        } else if (args.length == 2 && args[0].equalsIgnoreCase("market")) {
            List<String> completions = Arrays.asList("info", "list");
            
            return completions.stream()
                    .filter(s -> s.startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        return new ArrayList<>();
    }
}