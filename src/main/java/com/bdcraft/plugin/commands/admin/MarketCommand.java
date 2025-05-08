package com.bdcraft.plugin.commands.admin;

import com.bdcraft.plugin.BDCraft;
import com.bdcraft.plugin.modules.economy.market.BDMarket;
import com.bdcraft.plugin.modules.economy.market.MarketManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
 * Command for managing BD Markets.
 */
public class MarketCommand implements CommandExecutor, TabCompleter {
    private final BDCraft plugin;
    
    /**
     * Creates a new market command.
     * @param plugin The plugin instance
     */
    public MarketCommand(BDCraft plugin) {
        this.plugin = plugin;
        plugin.getCommand("bdmarket").setExecutor(this);
        plugin.getCommand("bdmarket").setTabCompleter(this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("bdcraft.admin.market")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }
        
        if (args.length == 0) {
            sendUsage(player);
            return true;
        }
        
        MarketManager marketManager = plugin.getEconomyModule().getMarketManager();
        
        switch (args[0].toLowerCase()) {
            case "create":
                createMarket(player, marketManager);
                break;
            case "upgrade":
                upgradeMarket(player, args, marketManager);
                break;
            case "info":
                getMarketInfo(player, marketManager);
                break;
            case "list":
                listMarkets(player, marketManager);
                break;
            case "delete":
                deleteMarket(player, args, marketManager);
                break;
            default:
                sendUsage(player);
                break;
        }
        
        return true;
    }
    
    /**
     * Creates a market at the player's location.
     * @param player The player
     * @param marketManager The market manager
     */
    private void createMarket(Player player, MarketManager marketManager) {
        Location location = player.getLocation();
        
        // Check if near existing market
        if (marketManager.getMarketAt(location) != null) {
            player.sendMessage(ChatColor.RED + "There is already a market in this area.");
            return;
        }
        
        // Create market
        BDMarket market = marketManager.createMarket(location, player);
        
        if (market != null) {
            player.sendMessage(ChatColor.GREEN + "Market created successfully!");
        } else {
            player.sendMessage(ChatColor.RED + "Failed to create market.");
        }
    }
    
    /**
     * Upgrades a market.
     * @param player The player
     * @param args The command arguments
     * @param marketManager The market manager
     */
    private void upgradeMarket(Player player, String[] args, MarketManager marketManager) {
        // Check if in a market
        BDMarket market = marketManager.getMarketAt(player.getLocation());
        
        if (market == null) {
            player.sendMessage(ChatColor.RED + "You are not in a market.");
            return;
        }
        
        // Check if already max level
        if (market.getLevel() >= 4) {
            player.sendMessage(ChatColor.RED + "This market is already at maximum level.");
            return;
        }
        
        // Upgrade market
        if (marketManager.upgradeMarket(market)) {
            player.sendMessage(ChatColor.GREEN + "Market upgraded to level " + market.getLevel() + "!");
        } else {
            player.sendMessage(ChatColor.RED + "Failed to upgrade market.");
        }
    }
    
    /**
     * Gets information about a market.
     * @param player The player
     * @param marketManager The market manager
     */
    private void getMarketInfo(Player player, MarketManager marketManager) {
        // Check if in a market
        BDMarket market = marketManager.getMarketAt(player.getLocation());
        
        if (market == null) {
            player.sendMessage(ChatColor.RED + "You are not in a market.");
            return;
        }
        
        // Send info
        player.sendMessage(ChatColor.GOLD + "=== Market Information ===");
        player.sendMessage(ChatColor.YELLOW + "ID: " + ChatColor.WHITE + market.getId());
        player.sendMessage(ChatColor.YELLOW + "Founder: " + ChatColor.WHITE + market.getFounderName());
        player.sendMessage(ChatColor.YELLOW + "Level: " + ChatColor.WHITE + market.getLevel());
        player.sendMessage(ChatColor.YELLOW + "Traders: " + ChatColor.WHITE + market.getTraders().size());
        player.sendMessage(ChatColor.YELLOW + "Collectors: " + ChatColor.WHITE + market.getTraderCount("COLLECTOR") + 
                "/" + getMaxCollectors(market));
        player.sendMessage(ChatColor.YELLOW + "Price Modifier: " + ChatColor.WHITE + 
                String.format("%.2fx", market.getPriceModifier()));
        player.sendMessage(ChatColor.YELLOW + "Center: " + ChatColor.WHITE + 
                formatLocation(market.getCenter()));
    }
    
    /**
     * Lists all markets.
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
        
        for (BDMarket market : markets) {
            player.sendMessage(ChatColor.YELLOW + "ID: " + ChatColor.WHITE + market.getId() + 
                    ChatColor.YELLOW + " | Founder: " + ChatColor.WHITE + market.getFounderName() + 
                    ChatColor.YELLOW + " | Level: " + ChatColor.WHITE + market.getLevel() + 
                    ChatColor.YELLOW + " | Location: " + ChatColor.WHITE + 
                    formatLocation(market.getCenter()));
        }
    }
    
    /**
     * Deletes a market.
     * @param player The player
     * @param args The command arguments
     * @param marketManager The market manager
     */
    private void deleteMarket(Player player, String[] args, MarketManager marketManager) {
        // Check if in a market
        BDMarket market = marketManager.getMarketAt(player.getLocation());
        
        if (market == null) {
            player.sendMessage(ChatColor.RED + "You are not in a market.");
            return;
        }
        
        // Confirm deletion
        if (args.length < 2 || !args[1].equalsIgnoreCase("confirm")) {
            player.sendMessage(ChatColor.RED + "WARNING: This will permanently delete this market and remove all associated villagers.");
            player.sendMessage(ChatColor.RED + "To confirm, type: /bdmarket delete confirm");
            return;
        }
        
        // Delete market
        marketManager.removeMarket(market);
        player.sendMessage(ChatColor.GREEN + "Market deleted successfully!");
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
    private String formatLocation(Location location) {
        return location.getBlockX() + ", " + 
                location.getBlockY() + ", " + 
                location.getBlockZ();
    }
    
    /**
     * Sends usage information to a player.
     * @param player The player
     */
    private void sendUsage(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== BD Market Commands ===");
        player.sendMessage(ChatColor.YELLOW + "/bdmarket create" + ChatColor.WHITE + " - Creates a market at your location");
        player.sendMessage(ChatColor.YELLOW + "/bdmarket upgrade" + ChatColor.WHITE + " - Upgrades the market you're in");
        player.sendMessage(ChatColor.YELLOW + "/bdmarket info" + ChatColor.WHITE + " - Shows information about the market you're in");
        player.sendMessage(ChatColor.YELLOW + "/bdmarket list" + ChatColor.WHITE + " - Lists all markets in the current world");
        player.sendMessage(ChatColor.YELLOW + "/bdmarket delete" + ChatColor.WHITE + " - Deletes the market you're in");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = Arrays.asList("create", "upgrade", "info", "list", "delete");
            
            return completions.stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        } else if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
            return Arrays.asList("confirm");
        }
        
        return new ArrayList<>();
    }
}